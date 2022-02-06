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

package org.overrun.tepv3.client.gl;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import org.overrun.tepv3.client.render.RenderSystem;
import org.overrun.tepv3.client.render.VertexFormat;
import org.overrun.tepv3.client.render.VertexFormatElement.Type;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The buffered implementation of {@link IVertexBuilder}. TODO: Color to ubyte
 *
 * @author squid233
 * @since 3.0.1
 */
public class VertexBuilder implements IVertexBuilder {
    private static final VertexBuilder INSTANCE = new VertexBuilder();
    private static int memSize = 0x80000;
    private static float[] array = new float[memSize];
    private static FloatBuffer buffer = memAllocFloat(memSize);
    private FloatList buf0;
    private float x, y, z, r, g, b, a, u, v;
    private boolean hasColor, hasTexture;
    private int pos;
    private int vertexIndex;
    private int vertexCount;
    private int primitive;

    private VertexBuilder() {
    }

    public static VertexBuilder getInstance() {
        return INSTANCE;
    }

    public void begin(int primitive) {
        buffer.clear();
        hasColor = false;
        hasTexture = false;
        pos = 0;
        vertexCount = 0;
        this.primitive = primitive;
    }

    @Override
    public VertexBuilder vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public VertexBuilder color(float r, float g, float b, float a) {
        hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    @Override
    public VertexBuilder color(float r, float g, float b) {
        return color(r, g, b, 1);
    }

    @Override
    public VertexBuilder tex(float u, float v) {
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

    public void array(float[] rawData) {
        array(RenderSystem.getProgram().getFormat(), rawData);
    }

    private void checkBufSz(int inc) {
        if (pos + inc >= memSize) {
            memSize += 0x800;
            var tmp = new float[memSize];
            System.arraycopy(array, 0, tmp, 0, array.length);
            array = tmp;
            buffer = memRealloc(buffer, memSize);
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
        var format = RenderSystem.getProgram().getFormat();
        checkBufSz(format.getIntVertexSize());
        var fmtHasColor = false;
        var fmtHasTexture = false;
        for (var element : format.getElements()) {
            if (element.isPosition()) {
                array[pos++] = x;
                array[pos++] = y;
                array[pos++] = z;
            } else if (element.getType() == Type.COLOR) {
                fmtHasColor = true;
                if (hasColor) {
                    array[pos++] = r;
                    array[pos++] = g;
                    array[pos++] = b;
                    array[pos++] = a;
                } else {
                    var color = RenderSystem.getProgramColor();
                    array[pos++] = color[0];
                    array[pos++] = color[1];
                    array[pos++] = color[2];
                    array[pos++] = color[3];
                }
            } else if (element.getType() == Type.UV) {
                fmtHasTexture = true;
                array[pos++] = u;
                array[pos++] = v;
            }
        }
        ++vertexCount;
        if (primitive == GL_QUADS) {
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
                } else {
                    var color = RenderSystem.getProgramColor();
                    buf0.add(color[0]);
                    buf0.add(color[1]);
                    buf0.add(color[2]);
                    buf0.add(color[3]);
                }
                if (hasTexture || fmtHasTexture) {
                    buf0.add(u);
                    buf0.add(v);
                }
            } else if (vertexIndex == 2) {
                array[pos++] = x;
                array[pos++] = y;
                array[pos++] = z;
                if (fmtHasColor) {
                    if (hasColor) {
                        array[pos++] = r;
                        array[pos++] = g;
                        array[pos++] = b;
                        array[pos++] = a;
                    } else {
                        var color = RenderSystem.getProgramColor();
                        array[pos++] = color[0];
                        array[pos++] = color[1];
                        array[pos++] = color[2];
                        array[pos++] = color[3];
                    }
                }
                if (hasTexture || fmtHasTexture) {
                    array[pos++] = u;
                    array[pos++] = v;
                }
            }
            ++vertexIndex;

            if (vertexIndex == 4) {
                vertexIndex = 0;
                array[pos++] = buf0.getFloat(0);
                array[pos++] = buf0.getFloat(1);
                array[pos++] = buf0.getFloat(2);
                if (fmtHasColor) {
                    if (hasColor) {
                        array[pos++] = buf0.getFloat(3);
                        array[pos++] = buf0.getFloat(4);
                        array[pos++] = buf0.getFloat(5);
                        array[pos++] = buf0.getFloat(6);
                    } else {
                        var color = RenderSystem.getProgramColor();
                        array[pos++] = color[0];
                        array[pos++] = color[1];
                        array[pos++] = color[2];
                        array[pos++] = color[3];
                    }
                }
                if (hasTexture || fmtHasTexture) {
                    if (hasColor) {
                        array[pos++] = buf0.getFloat(7);
                        array[pos++] = buf0.getFloat(8);
                        array[pos++] = buf0.getFloat(9);
                        array[pos++] = buf0.getFloat(10);
                    } else {
                        array[pos++] = buf0.getFloat(3);
                        array[pos++] = buf0.getFloat(4);
                        array[pos++] = buf0.getFloat(5);
                        array[pos++] = buf0.getFloat(6);
                    }
                }
                buf0.clear();
                vertexCount += 2;
            }
        }
    }

    @Override
    public void next() {
        next(x, y, z, r, g, b, a, u, v);
    }

    public void end() {
        buffer.clear().put(array, 0, pos).flip();
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
        var fmt = shader.getFormat();
        glBindVertexArray(fmt.getVertexArray());
        glBindBuffer(GL_ARRAY_BUFFER, fmt.getVertexBuffer());
        nglBufferData(GL_ARRAY_BUFFER, pos * 4L, memAddress(buffer), GL_DYNAMIC_DRAW);
        fmt.startDrawing();
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDrawArrays(primitive == GL_QUADS ? GL_TRIANGLES : primitive, 0, vertexCount);
        fmt.endDrawing();
        glBindVertexArray(0);
        shader.unbind();
    }

    /**
     * Free buffer. Don't call by user.
     */
    public static void freeResources() {
        memFree(buffer);
    }
}
