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

package org.overrun.tepv3.client.render;

import org.joml.Matrix4fStack;
import org.overrun.tepv3.client.tex.TextureUtil;
import org.overrun.tepv3.util.Identifier;

import java.util.Arrays;

import static org.lwjgl.opengl.GL13.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class RenderSystem {
    private static final float[] programColor = {1, 1, 1, 1};
    private static Program program;
    private static final Matrix4fStack projection =
        new Matrix4fStack(4);
    private static final Matrix4fStack modelView =
        new Matrix4fStack(32);
    private static boolean depthTest;
    private static int depthFunc = GL_LESS;
    private static boolean multiSample;
    private static boolean cullFace;
    private static final TextureState[] TEXTURES = new TextureState[32];
    private static int activeTexture;
    private static final int[] shaderTextures = new int[32];
    private static int clearMask;

    private static class TextureState {
        public boolean isEnabled;
        public int boundTexture;
    }

    static {
        Arrays.fill(TEXTURES, new TextureState());
    }

    public static void setViewport(int x,
                                   int y,
                                   int w,
                                   int h) {
        glViewport(x, y, w, h);
    }

    public static void setClearColor(float r,
                                     float g,
                                     float b,
                                     float a) {
        glClearColor(r, g, b, a);
    }

    public static void clearColorBuf() {
        clearMask |= GL_COLOR_BUFFER_BIT;
    }

    public static void clearDepthBuf() {
        clearMask |= GL_DEPTH_BUFFER_BIT;
    }

    /**
     * Call {@link org.lwjgl.opengl.GL11#glClear(int) glClear} with previous set
     * clear mask.
     */
    public static void clear() {
        glClear(clearMask);
        clearMask = 0;
    }

    public static void enableDepthTest() {
        if (!depthTest) {
            depthTest = true;
            glEnable(GL_DEPTH_TEST);
        }
    }

    public static void depthFunc(int func) {
        if (depthFunc != func) {
            depthFunc = func;
            glDepthFunc(func);
        }
    }

    public static void enableMultiSample() {
        if (!multiSample) {
            multiSample = true;
            glEnable(GL_MULTISAMPLE);
        }
    }

    public static void enableCullFace() {
        if (!cullFace) {
            cullFace = true;
            glEnable(GL_CULL_FACE);
        }
    }

    public static void bindTexture(int id) {
        if (TEXTURES[activeTexture].boundTexture != id) {
            TEXTURES[activeTexture].boundTexture = id;
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    public static void setShaderTexture(int unit, Identifier id) {
        setShaderTexture(unit, TextureUtil.findTexture(id));
    }

    public static void setShaderTexture(int unit, int id) {
        if (unit >= 0 && unit < shaderTextures.length) {
            shaderTextures[unit] = id;
        }
    }

    public static int getShaderTexture(int unit) {
        if (unit >= 0 && unit < shaderTextures.length) {
            return shaderTextures[unit];
        }
        return 0;
    }

    public static void bindTexture(Identifier id) {
        bindTexture(TextureUtil.findTexture(id));
    }

    public static void enableTexture() {
        TEXTURES[activeTexture].isEnabled = true;
    }

    public static void disableTexture() {
        TEXTURES[activeTexture].isEnabled = false;
    }

    public static void activeTexture(int unit) {
        if (activeTexture != unit) {
            activeTexture = unit;
            glActiveTexture(GL_TEXTURE0 + getActiveTexture());
        }
    }

    public static int getActiveTexture() {
        return activeTexture;
    }

    public static void setProgramColor(float r,
                                       float g,
                                       float b,
                                       float a) {
        programColor[0] = r;
        programColor[1] = g;
        programColor[2] = b;
        programColor[3] = a;
    }

    public static void setProgram(Program program) {
        RenderSystem.program = program;
    }

    public static Program getProgram() {
        return program;
    }

    public static float[] getProgramColor() {
        return programColor;
    }

    public static Matrix4fStack getProjection() {
        return projection;
    }

    public static Matrix4fStack getModelView() {
        return modelView;
    }
}
