/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.overrun.tepv3.model;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import org.overrun.tepv3.client.gl.IVertexBuilder;
import org.overrun.tepv3.client.render.RenderSystem;
import org.overrun.tepv3.client.render.VertexFormat;
import org.overrun.tepv3.client.render.VertexFormatElement;
import org.overrun.tepv3.client.render.VertexFormatElement.Type;
import org.overrun.tepv3.client.render.VertexFormats;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Mesh implements IMesh {
    private final float[] rawData;
    private final VertexFormat format;
    private final int vertexCount;
    private boolean built;
    private int vao, vbo;

    public static class Builder implements IVertexBuilder {
        private final FloatList data = new FloatArrayList();
        private boolean quad;
        private boolean hasColor, hasTexture;
        private float x, y, z, r, g, b, a, u, v;
        private int vertexCount;
        private int vertexIndex;
        private FloatList buf0;

        /**
         * Enable quad building.
         * <p>
         * The quad vertex order:<br>
         * {@code p0 p3}<br>
         * {@code p1 p2}
         * </p>
         *
         * @return this
         */
        public Builder enableQuad() {
            quad = true;
            return this;
        }

        @Override
        public Builder vertex(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        @Override
        public Builder color(float r, float g, float b, float a) {
            hasColor = true;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            return this;
        }

        @Override
        public Builder color(float r, float g, float b) {
            return color(r, g, b, 1);
        }

        @Override
        public Builder tex(float u, float v) {
            hasTexture = true;
            this.u = u;
            this.v = v;
            return this;
        }

        @Override
        public void array(VertexFormat format, float[] rawData) {
            for (int i = 0; i < rawData.length; ) {
                float x = 0, y = 0, z = 0, r = 0, g = 0, b = 0, a = 0, u = 0, v = 0;
                for (var element : format.getElements()) {
                    if (element.isPosition()) {
                        x = rawData[i++];
                        y = rawData[i++];
                        z = rawData[i++];
                    } else if (element.getType() == Type.COLOR) {
                        hasColor = true;
                        r = rawData[i++];
                        g = rawData[i++];
                        b = rawData[i++];
                        a = rawData[i++];
                    } else if (element.getType() == Type.UV) {
                        hasTexture = true;
                        u = rawData[i++];
                        v = rawData[i++];
                    }
                }
                next(x, y, z, r, g, b, a, u, v);
            }
        }

        @Override
        public void next(float x,
                         float y,
                         float z,
                         float r,
                         float g,
                         float b,
                         float a,
                         float u,
                         float v) {
            data.add(x);
            data.add(y);
            data.add(z);
            if (hasColor) {
                data.add(r);
                data.add(g);
                data.add(b);
                data.add(a);
            }
            if (hasTexture) {
                data.add(u);
                data.add(v);
            }
            ++vertexCount;
            if (quad) {
                if (buf0 == null)
                    buf0 = new FloatArrayList();
                if (vertexIndex == 0) {
                    buf0.add(x);
                    buf0.add(y);
                    buf0.add(z);
                    if (hasColor) {
                        buf0.add(r);
                        buf0.add(g);
                        buf0.add(b);
                        buf0.add(a);
                    }
                    if (hasTexture) {
                        buf0.add(u);
                        buf0.add(v);
                    }
                } else if (vertexIndex == 2) {
                    data.add(x);
                    data.add(y);
                    data.add(z);
                    if (hasColor) {
                        data.add(r);
                        data.add(g);
                        data.add(b);
                        data.add(a);
                    }
                    if (hasTexture) {
                        data.add(u);
                        data.add(v);
                    }
                }
                ++vertexIndex;

                if (vertexIndex == 4) {
                    vertexIndex = 0;
                    data.addAll(buf0);
                    buf0.clear();
                    vertexCount += 2;
                }
            }
        }

        @Override
        public void next() {
            next(x, y, z, r, g, b, a, u, v);
        }

        public Mesh build() {
            var fmt = new ArrayList<VertexFormatElement>();
            fmt.add(VertexFormats.POSITION_ELEMENT);
            if (hasColor)
                fmt.add(VertexFormats.COLOR_ELEMENT);
            if (hasTexture)
                fmt.add(VertexFormats.TEXTURE_0_ELEMENT);
            return new Mesh(data.toFloatArray(),
                VertexFormat.fromElements(fmt),
                vertexCount);
        }
    }

    public Mesh(float[] rawData,
                VertexFormat format,
                int vertexCount) {
        this.rawData = rawData;
        this.format = format;
        this.vertexCount = vertexCount;
    }

    @Override
    public void render() {
        var shader = RenderSystem.getProgram();
        shader.addSampler("Sampler0", RenderSystem.getShaderTexture(0));
        if (shader.projMat != null) {
            shader.projMat.set(RenderSystem.getProjection());
        }
        if (shader.modelViewMat != null) {
            shader.modelViewMat.set(RenderSystem.getModelView());
        }
        if (shader.colorModulator != null) {
            shader.colorModulator.set(RenderSystem.getProgramColor());
        }
        shader.bind();
        if (vao == 0)
            vao = glGenVertexArrays();
        glBindVertexArray(vao);
        if (vbo == 0)
            vbo = glGenBuffers();
        if (!built) {
            built = true;
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, rawData, GL_STATIC_DRAW);
            format.startDrawing();
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
        shader.unbind();
    }

    @Override
    public float[] getRawData() {
        return rawData;
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    public void free() {
        if (glIsVertexArray(vao)) {
            glDeleteVertexArrays(vao);
            vao = 0;
        }
        if (glIsBuffer(vbo)) {
            glDeleteBuffers(vbo);
            vbo = 0;
        }
    }
}
