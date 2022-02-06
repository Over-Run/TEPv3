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

import org.jetbrains.annotations.Nullable;
import org.overrun.tepv3.client.render.RenderSystem;

import java.util.Locale;
import java.util.Objects;

import static org.lwjgl.opengl.GL14.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class GLBlendState {
    @Nullable
    public static GLBlendState activeBlendState;
    private final int srcRgb;
    private final int srcAlpha;
    private final int dstRgb;
    private final int dstAlpha;
    private final int func;
    private final boolean separateBlend;
    private final boolean blendDisabled;

    private GLBlendState(boolean separateBlend, boolean blendDisabled, int srcRgb, int dstRgb, int srcAlpha, int dstAlpha, int func) {
        this.separateBlend = separateBlend;
        this.srcRgb = srcRgb;
        this.dstRgb = dstRgb;
        this.srcAlpha = srcAlpha;
        this.dstAlpha = dstAlpha;
        this.blendDisabled = blendDisabled;
        this.func = func;
    }

    public GLBlendState() {
        this(false, true, GL_ONE, GL_ZERO, GL_ONE, GL_ZERO, GL_FUNC_ADD);
    }

    public GLBlendState(int srcRgb, int dstRgb, int func) {
        this(false, false, srcRgb, dstRgb, srcRgb, dstRgb, func);
    }

    public GLBlendState(int srcRgb, int dstRgb, int srcAlpha, int dstAlpha, int func) {
        this(true, false, srcRgb, dstRgb, srcAlpha, dstAlpha, func);
    }

    public void enable() {
        if (equals(activeBlendState))
            return;
        if (activeBlendState == null || blendDisabled != activeBlendState.isBlendDisabled()) {
            activeBlendState = this;
            if (blendDisabled) {
                RenderSystem.disableBlend();
                return;
            }
            RenderSystem.enableBlend();
        }
        RenderSystem.blendEquation(func);
        if (separateBlend)
            RenderSystem.blendFuncSeparate(srcRgb, dstRgb, srcAlpha, dstAlpha);
        else
            RenderSystem.blendFunc(srcRgb, dstRgb);
    }

    public boolean isBlendDisabled() {
        return blendDisabled;
    }

    public static int getFuncFromStr(String name) {
        name = name.trim().toLowerCase(Locale.ROOT).replaceAll("_", "");
        if ("add".equals(name))
            return GL_FUNC_ADD;
        if ("subtract".equals(name)) {
            return GL_FUNC_SUBTRACT;
        }
        if ("reversesubtract".equals(name)) {
            return GL_FUNC_REVERSE_SUBTRACT;
        }
        if ("min".equals(name)) {
            return GL_MIN;
        }
        if ("max".equals(name)) {
            return GL_MAX;
        }
        return GL_FUNC_ADD;
    }

    public static int getCompFromStr(String expr) {
        expr = expr.trim()
            .toLowerCase(Locale.ROOT)
            .replaceAll("_", "")
            .replaceAll("one", "1")
            .replaceAll("zero", "0")
            .replaceAll("minus", "-");
        if ("0".equals(expr))
            return GL_ZERO;
        if ("1".equals(expr)) {
            return GL_ONE;
        }
        if ("srccolor".equals(expr)) {
            return GL_SRC_COLOR;
        }
        if ("1-srccolor".equals(expr)) {
            return GL_ONE_MINUS_SRC_COLOR;
        }
        if ("dstcolor".equals(expr)) {
            return GL_DST_COLOR;
        }
        if ("1-dstcolor".equals(expr)) {
            return GL_ONE_MINUS_DST_COLOR;
        }
        if ("srcalpha".equals(expr)) {
            return GL_SRC_ALPHA;
        }
        if ("1-srcalpha".equals(expr)) {
            return GL_ONE_MINUS_SRC_ALPHA;
        }
        if ("dstalpha".equals(expr)) {
            return GL_DST_ALPHA;
        }
        if ("1-dstalpha".equals(expr)) {
            return GL_ONE_MINUS_DST_ALPHA;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GLBlendState that = (GLBlendState) o;
        return srcRgb == that.srcRgb
            && srcAlpha == that.srcAlpha
            && dstRgb == that.dstRgb
            && dstAlpha == that.dstAlpha
            && func == that.func
            && separateBlend == that.separateBlend
            && blendDisabled == that.blendDisabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcRgb, srcAlpha, dstRgb, dstAlpha, func, separateBlend, blendDisabled);
    }
}
