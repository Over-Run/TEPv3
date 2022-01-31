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

package org.overrun.tepv3.gl;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.overrun.tepv3.gl.VertexFormat.COLOR4F;
import static org.overrun.tepv3.gl.VertexFormat.VERTEX3F;

/**
 * @author squid233
 * @since 3.0.1
 */
public class VertexBuilder {
    private static final VertexBuilder INSTANCE = new VertexBuilder();
    private static int memSize = 0x80000;
    private static float[] array = new float[memSize];
    private static FloatBuffer buffer = memAllocFloat(memSize);
    /**
     * Init when call {@link #end()}
     */
    private static int vao, vbo;
    private float x, y, z, r, g, b, a;
    private boolean hasColor;
    private int pos;
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
        pos = 0;
        vertexCount = 0;
        this.primitive = primitive;
    }

    /**
     * Push a vertex.
     *
     * @param x x
     * @param y y
     * @param z z
     */
    public VertexBuilder vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Push a color.
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha default to 1
     */
    public VertexBuilder color(float r, float g, float b, float a) {
        hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    /**
     * Push a color.
     *
     * @param r red
     * @param g green
     * @param b blue
     */
    public VertexBuilder color(float r, float g, float b) {
        return color(r, g, b, 1);
    }

    public void array(float[] rawData) {
        var layout = RenderSystem.getShader().getLayout();
        for (int i = 0; i < rawData.length; ) {
            float x = 0, y = 0, z = 0, r = 0, g = 0, b = 0, a = 0;
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
                }
            }
            next(x, y, z, r, g, b, a);
        }
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

    public void next(float x,
                     float y,
                     float z,
                     float r,
                     float g,
                     float b,
                     float a) {
        var layout = RenderSystem.getShader().getLayout();
        checkBufSz(layout.getCount());
        for (var fmt : layout.getFormats()) {
            switch (fmt) {
                case VERTEX3F -> {
                    array[pos++] = x;
                    array[pos++] = y;
                    array[pos++] = z;
                }
                case COLOR4F -> {
                    if (hasColor) {
                        array[pos++] = r;
                        array[pos++] = g;
                        array[pos++] = b;
                        array[pos++] = a;
                    } else {
                        var color = RenderSystem.getShaderColor();
                        array[pos++] = color.x;
                        array[pos++] = color.y;
                        array[pos++] = color.z;
                        array[pos++] = color.w;
                    }
                }
            }
        }
        ++vertexCount;
    }

    public void next() {
        next(x, y, z, r, g, b, a);
    }

    public void end() {
        buffer.clear().put(array, 0, pos).flip();
        var shader = RenderSystem.getShader();
        shader.use();
        shader.setMatrix4("ProjMat", RenderSystem.getProjection());
        shader.setMatrix4("ModelViewMat", RenderSystem.getModelView());
        if (vao == 0) {
            vao = glGenVertexArrays();
        }
        glBindVertexArray(vao);
        if (vbo == 0) {
            vbo = glGenBuffers();
        }
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
        var vars = shader.getVariables();
        var layout = shader.getLayout();
        if (layout.hasPos()) {
            var pos = vars.get("Position");
            glEnableVertexAttribArray(pos.location);
            glVertexAttribPointer(pos.location,
                VERTEX3F.getCount(),
                GL_FLOAT,
                false,
                layout.getStride(),
                layout.getOffset(VERTEX3F));
        }
        if (layout.hasColor()) {
            var color = vars.get("Color");
            glEnableVertexAttribArray(color.location);
            glVertexAttribPointer(color.location,
                COLOR4F.getCount(),
                GL_FLOAT,
                false,
                layout.getStride(),
                layout.getOffset(COLOR4F));
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDrawArrays(primitive, 0, vertexCount);
        glBindVertexArray(0);
        shader.noUsing();
    }

    /**
     * Free buffer. Don't call by user.
     */
    public static void freeResources() {
        memFree(buffer);
        if (glIsVertexArray(vao))
            glDeleteVertexArrays(vao);
        if (glIsBuffer(vbo))
            glDeleteBuffers(vbo);
    }
}
