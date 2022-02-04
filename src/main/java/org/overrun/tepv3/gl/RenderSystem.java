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

import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.overrun.tepv3.util.Identifier;
import org.overrun.tepv3.client.tex.TextureMgr;

import static org.lwjgl.opengl.GL13.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class RenderSystem {
    private static final Vector4f shaderColor =
        new Vector4f();
    private static ShaderProgram shaderProgram;
    private static final Matrix4fStack projection =
        new Matrix4fStack(4);
    private static final Matrix4fStack modelView =
        new Matrix4fStack(32);
    private static boolean depthTest;
    private static int depthFunc = GL_LESS;
    private static boolean multiSample;
    private static boolean cullFace;
    private static int currentTex2D;

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

    public static void setShaderTexture(int id) {
        if (currentTex2D != id) {
            currentTex2D = id;
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    public static void setShaderTexture(Identifier id) {
        setShaderTexture(TextureMgr.findTexture(id));
    }

    public static void activeTexture(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
    }

    public static void setShaderColor(float r,
                                      float g,
                                      float b,
                                      float a) {
        shaderColor.set(r, g, b, a);
    }

    public static void useShader(ShaderProgram shader) {
        shaderProgram = shader;
    }

    public static ShaderProgram getShader() {
        return shaderProgram;
    }

    public static Vector4f getShaderColor() {
        return shaderColor;
    }

    public static Matrix4fStack getProjection() {
        return projection;
    }

    public static Matrix4fStack getModelView() {
        return modelView;
    }
}
