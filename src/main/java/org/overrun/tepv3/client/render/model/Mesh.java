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

package org.overrun.tepv3.client.render.model;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.overrun.tepv3.client.gl.IVertexBuilder;
import org.overrun.tepv3.client.render.RenderSystem;
import org.overrun.tepv3.client.render.VertexFormat;
import org.overrun.tepv3.client.render.VertexFormatElement;
import org.overrun.tepv3.client.render.VertexFormatElement.Type;
import org.overrun.tepv3.client.render.VertexFormats;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.overrun.tepv3.client.gl.GLColor.toUbyte;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Mesh implements IMesh {
    private final ByteBuffer rawData;
    private final int[] elementIndices;
    private final VertexFormat format;
    private final int vertexCount;
    private boolean built;
    private int vao, vbo, ebo;

    public static class Builder implements IVertexBuilder {
        private int dataSz = 0x30000;
        private ByteBuffer data = memAlloc(dataSz);
        private IntList indices;
        private boolean quad;
        private boolean hasColor, hasTexture;
        private float x, y, z, r, g, b, a, u, v;
        private int vertexCount;
        private int vertexIndex;

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
            if (data.capacity() - data.position() < 32) {
                dataSz += 0x30000;
                data = memRealloc(data, dataSz);
            }
            data.putFloat(x)
                .putFloat(y)
                .putFloat(z);
            if (hasColor) {
                data.put((byte) toUbyte(r))
                    .put((byte) toUbyte(g))
                    .put((byte) toUbyte(b))
                    .put((byte) toUbyte(a));
            }
            if (hasTexture) {
                data.putFloat(u)
                    .putFloat(v);
            }
            ++vertexCount;
            if (quad) {
                if (indices == null)
                    indices = new IntArrayList();
                ++vertexIndex;

                if (vertexIndex == 4) {
                    vertexIndex = 0;
                    vertexCount += 2;
                    indices.add(vertexCount - 6);
                    indices.add(vertexCount - 5);
                    indices.add(vertexCount - 4);
                    indices.add(vertexCount - 4);
                    indices.add(vertexCount - 3);
                    indices.add(vertexCount - 6);
                }
            }
        }

        @Override
        public void next() {
            next(x, y, z, r, g, b, a, u, v);
        }

        public Mesh build() {
            var fmtList = new ArrayList<VertexFormatElement>();
            fmtList.add(VertexFormats.POSITION_ELEMENT);
            if (hasColor)
                fmtList.add(VertexFormats.COLOR_ELEMENT);
            if (hasTexture)
                fmtList.add(VertexFormats.TEXTURE_0_ELEMENT);
            var fmt = VertexFormat.fromElements(fmtList);
            return new Mesh(data.flip(),
                indices != null ? indices.toIntArray() : null,
                fmt,
                vertexCount);
        }
    }

    public Mesh(ByteBuffer rawData,
                int[] elementIndices,
                VertexFormat format,
                int vertexCount) {
        this.rawData = rawData;
        this.elementIndices = elementIndices;
        this.format = format;
        this.vertexCount = vertexCount;
    }

    @Override
    public void render() {
        var shader = RenderSystem.getProgram();
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
            if (elementIndices != null) {
                if (ebo == 0)
                    ebo = glGenBuffers();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementIndices, GL_STATIC_DRAW);
            }
            format.startDrawing();
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        if (elementIndices != null)
            glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        else
            glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
        shader.unbind();
    }

    @Override
    public ByteBuffer getRawData() {
        return rawData;
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    public void free() {
        memFree(rawData);
        if (glIsVertexArray(vao)) {
            glDeleteVertexArrays(vao);
            vao = 0;
        }
        if (glIsBuffer(vbo)) {
            glDeleteBuffers(vbo);
            vbo = 0;
        }
        if (glIsBuffer(ebo)) {
            glDeleteBuffers(ebo);
            ebo = 0;
        }
    }
}
