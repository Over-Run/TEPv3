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
import static org.overrun.tepv3.gl.VertexFormat.COLOR4F;
import static org.overrun.tepv3.gl.VertexFormat.VERTEX3F;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Mesh32 implements IMesh {
    private final float[] rawData;
    private final VertexLayout layout;
    private final int vertexCount;
    private boolean built;
    private int vao, vbo;

    public static class Builder implements IVertexBuilder {
        private final FloatArray data = new FloatArray();
        private boolean hasColor;
        private float x, y, z, r, g, b, a;
        private int vertexCount;

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
        public void array(VertexLayout layout, float[] rawData) {
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

        @Override
        public void next(float x,
                         float y,
                         float z,
                         float r,
                         float g,
                         float b,
                         float a) {
            data.add(x);
            data.add(y);
            data.add(z);
            if (hasColor) {
                data.add(r);
                data.add(g);
                data.add(b);
                data.add(a);
            }
            ++vertexCount;
        }

        @Override
        public void next() {
            next(x, y, z, r, g, b, a);
        }

        public Mesh32 build() {
            var fmt = new ArrayList<VertexFormat>();
            fmt.add(VertexFormat.VERTEX3F);
            if (hasColor)
                fmt.add(VertexFormat.COLOR4F);
            return new Mesh32(data.toFArray(), new VertexLayout(fmt), vertexCount);
        }
    }

    public Mesh32(float[] rawData,
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
        shader.setMatrix4("ProjMat", RenderSystem.getProjection());
        shader.setMatrix4("ModelViewMat", RenderSystem.getModelView());
        if (vao == 0) {
            vao = glGenVertexArrays();
        }
        glBindVertexArray(vao);
        if (vbo == 0) {
            vbo = glGenBuffers();
        }
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
}
