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

import org.overrun.commonutils.FloatArray;
import org.overrun.tepv3.gl.IVertexBuilder;
import org.overrun.tepv3.gl.RenderSystem;
import org.overrun.tepv3.gl.VertexFormat;
import org.overrun.tepv3.gl.VertexLayout;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;
import static org.overrun.tepv3.gl.VertexFormat.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Mesh implements IMesh {
    private final float[] rawData;
    private final VertexLayout layout;
    private final int vertexCount;
    private boolean built;
    private int vao, vbo;

    public static class Builder implements IVertexBuilder {
        private final FloatArray data = new FloatArray();
        private boolean quad;
        private boolean hasColor, hasTexture;
        private float x, y, z, r, g, b, a, u, v;
        private int vertexCount;
        private int vertexIndex;
        private FloatArray buf0;

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
        public void array(VertexLayout layout, float[] rawData) {
            for (int i = 0; i < rawData.length; ) {
                float x = 0, y = 0, z = 0, r = 0, g = 0, b = 0, a = 0, u = 0, v = 0;
                for (var fmt : layout.getFormats()) {
                    switch (fmt) {
                        case VERTEX3F -> {
                            x = rawData[i++];
                            y = rawData[i++];
                            z = rawData[i++];
                        }
                        case COLOR4F -> {
                            hasColor = true;
                            r = rawData[i++];
                            g = rawData[i++];
                            b = rawData[i++];
                            a = rawData[i++];
                        }
                        case TEX2F -> {
                            hasTexture = true;
                            u = rawData[i++];
                            v = rawData[i++];
                        }
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
            data.addAll(x, y, z);
            if (hasColor)
                data.addAll(r, g, b, a);
            if (hasTexture)
                data.addAll(u, v);
            ++vertexCount;
            if (quad) {
                if (buf0 == null)
                    buf0 = new FloatArray();
                if (vertexIndex == 0) {
                    buf0.addAll(x, y, z);
                    if (hasColor)
                        buf0.addAll(r, g, b, a);
                    if (hasTexture)
                        buf0.addAll(u, v);
                } else if (vertexIndex == 2) {
                    data.addAll(x, y, z);
                    if (hasColor)
                        data.addAll(r, g, b, a);
                    if (hasTexture)
                        data.addAll(u, v);
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
            var fmt = new ArrayList<VertexFormat>();
            fmt.add(VERTEX3F);
            if (hasColor)
                fmt.add(COLOR4F);
            if (hasTexture)
                fmt.add(TEX2F);
            return new Mesh(data.toFArray(),
                new VertexLayout(fmt),
                vertexCount);
        }
    }

    public Mesh(float[] rawData,
                VertexLayout layout,
                int vertexCount) {
        this.rawData = rawData;
        this.layout = layout;
        this.vertexCount = vertexCount;
    }

    @Override
    public void render() {
        var shader = RenderSystem.getShader();
        shader.use();
        shader.setUniform("ProjMat", RenderSystem.getProjection());
        shader.setUniform("ModelViewMat", RenderSystem.getModelView());
        shader.setUniform("ColorModulator", RenderSystem.getShaderColor());
        if (vao == 0)
            vao = glGenVertexArrays();
        glBindVertexArray(vao);
        if (vbo == 0)
            vbo = glGenBuffers();
        if (!built) {
            built = true;
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, rawData, GL_STATIC_DRAW);
            var vars = shader.getVariables();
            if (layout.hasPos()) {
                var pos = vars.get("Position");
                glEnableVertexAttribArray(pos.getLocation());
                glVertexAttribPointer(pos.getLocation(),
                    VERTEX3F.getCount(),
                    GL_FLOAT,
                    false,
                    layout.getStride(),
                    layout.getOffset(VERTEX3F));
            }
            if (layout.hasColor()) {
                var color = vars.get("Color");
                glEnableVertexAttribArray(color.getLocation());
                glVertexAttribPointer(color.getLocation(),
                    COLOR4F.getCount(),
                    GL_FLOAT,
                    false,
                    layout.getStride(),
                    layout.getOffset(COLOR4F));
            }
            if (layout.hasTexture()) {
                var tex = vars.get("UV0");
                shader.setUniform("Sampler0", 0);
                RenderSystem.activeTexture(0);
                glEnableVertexAttribArray(tex.getLocation());
                glVertexAttribPointer(tex.getLocation(),
                    TEX2F.getCount(),
                    GL_FLOAT,
                    false,
                    layout.getStride(),
                    layout.getOffset(TEX2F));
            }
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
        shader.noUsing();
    }

    @Override
    public float[] getRawData() {
        return rawData;
    }

    @Override
    public VertexLayout getLayout() {
        return layout;
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
