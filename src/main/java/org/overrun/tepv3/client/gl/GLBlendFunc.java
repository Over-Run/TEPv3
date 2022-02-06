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

import static org.lwjgl.opengl.GL14.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public enum GLBlendFunc {
    CONSTANT_ALPHA(GL_CONSTANT_ALPHA),
    CONSTANT_COLOR(GL_CONSTANT_COLOR),
    DST_ALPHA(GL_DST_ALPHA),
    DST_COLOR(GL_DST_COLOR),
    ONE(GL_ONE),
    ONE_MINUS_CONSTANT_ALPHA(GL_ONE_MINUS_CONSTANT_ALPHA),
    ONE_MINUS_CONSTANT_COLOR(GL_ONE_MINUS_CONSTANT_COLOR),
    ONE_MINUS_DST_ALPHA(GL_ONE_MINUS_DST_ALPHA),
    ONE_MINUS_DST_COLOR(GL_ONE_MINUS_DST_COLOR),
    ONE_MINUS_SRC_ALPHA(GL_ONE_MINUS_SRC_ALPHA),
    ONE_MINUS_SRC_COLOR(GL_ONE_MINUS_SRC_COLOR),
    SRC_ALPHA(GL_SRC_ALPHA),
    SRC_ALPHA_SATURATE(GL_SRC_ALPHA_SATURATE),
    SRC_COLOR(GL_SRC_COLOR),
    ZERO(GL_ZERO);

    private final int func;

    GLBlendFunc(int func) {
        this.func = func;
    }

    public int getValue() {
        return func;
    }
}
