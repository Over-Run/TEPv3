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

package org.overrun.tepv3.game.client;

import org.lwjgl.opengl.GLUtil;
import org.overrun.tepv3.gl.RenderSystem;
import org.overrun.tepv3.gl.ShaderProgram;
import org.overrun.tepv3.gl.VertexBuilder;
import org.overrun.tepv3.scene.GLFWScene;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.tepv3.game.client.RunArgs.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class TEPv3Game extends GLFWScene {
    private static TEPv3Game instance;

    public TEPv3Game() {
        super(INIT_WIDTH, INIT_HEIGHT, INIT_TITLE);
    }

    @Override
    public void init() {
        final var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null)
            getWindow().setPos((vidMode.width() - viewport.getWidth()) / 2,
                (vidMode.height() - viewport.getHeight()) / 2);
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        if (false) //#ifdef _DEBUG
            GLUtil.setupDebugMessageCallback(System.err);
    }

    @Override
    public void render(double delta) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        RenderSystem.useShader(ShaderProgram.POS_COLOR);
        var gl = VertexBuilder.getInstance();
        gl.begin(GL_TRIANGLES);
        gl.color(1, 0, 0).vertex(0, 0.5f, 0).next();
        gl.color(0, 1, 0).vertex(-0.5f, -0.5f, 0).next();
        gl.color(0, 0, 1).vertex(0.5f, -0.5f, 0).next();
        gl.end();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    public static TEPv3Game getInstance() {
        if (instance == null)
            instance = new TEPv3Game();
        return instance;
    }
}
