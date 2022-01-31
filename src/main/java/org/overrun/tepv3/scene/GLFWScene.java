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

package org.overrun.tepv3.scene;

import org.lwjgl.opengl.GL;
import org.overrun.tepv3.Window;
import org.overrun.tepv3.event.InputEvent;
import org.overrun.tepv3.gl.ShaderProgram;
import org.overrun.tepv3.gl.VertexBuilder;
import org.overrun.tepv3.util.Timer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.createPrint;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.overrun.tepv3.Configs.*;
import static org.overrun.tepv3.gl.GLStateMgr.forceGL20;

/**
 * The scene implemented using GLFW.
 * <h2>The running pipeline</h2>
 * <table>
 *     <tr><th>The method</th>
 *         <th>The description</th>
 *     </tr>
 *     <tr><td>{@link #onStarting()}</td>
 *         <td>It will be called on calling {@link #start()}.</td>
 *     </tr>
 *     <tr><td>{@link #init()}</td>
 *         <td>It will be called after {@link GL#createCapabilities(boolean) creating} OpenGL capabilities.</td>
 *     </tr>
 *     <tr><td>{@link #onRunning()}</td>
 *         <td>It will be called on calling {@link #run()}.</td>
 *     </tr>
 *     <tr><td>{@link #tick()}</td>
 *         <td>It will be called on ticking.</td>
 *     </tr>
 *     <tr><td>{@link #render(double)}</td>
 *         <td>It will be called on rendering.</td>
 *     </tr>
 *     <tr><td>{@link #onExiting()}</td>
 *         <td>It will be called on calling {@link #exit()}.</td>
 *     </tr>
 * </table>
 * Any methods are overridable.
 *
 * @author squid233
 * @since 3.0.1
 */
public class GLFWScene extends Scene implements Runnable {
    private final Window window = new Window();
    /**
     * The timer with config tps
     */
    protected final Timer timer = new Timer(tps);

    public GLFWScene(int width, int height, String title) {
        viewport = new Viewport.Mutable(width, height);
        window.setTitle(title);
    }

    public final void start() {
        onStarting();
        if (glfwErrorCallback == null) {
            glfwErrorCallback = createPrint(System.err);
        }
        glfwErrorCallback.set();
        if (!glfwInit())
            glfwInitFailed.run();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, forceGL20 ? 2 : 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, forceGL20 ? 0 : 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, forceGL20
            ? GLFW_OPENGL_ANY_PROFILE
            : GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window.create(getWidth(), getHeight(), window.getTitle(), NULL, NULL);
        window.check(glfwWindowCreateFailed);
        window.onKey(InputEvent::send);
        window.onResizing((handle, width, height) -> {
            viewport.setWidth(width);
            viewport.setHeight(height);
            resize(width, height);
        });
        window.makeCtxCurrent();
        glfwSwapInterval(swapInterval);
        GL.createCapabilities(true);
        resize(getWidth(), getHeight());
        ShaderProgram.initialize();
        init();
        window.show();
        run();
        exit();
    }

    @Override
    public final void run() {
        onRunning();
        while (!window.shouldClose()) {
            timer.advanceTime();
            for (int i = 0; i < timer.ticks; i++) {
                tick();
            }
            render(timer.delta);
            window.swapBuffers();
            glfwPollEvents();
        }
    }

    public final void exit() {
        onExiting();
        VertexBuilder.freeResources();
        ShaderProgram.freeResources();
        window.freeCallbacks();
        window.destroy();
        glfwTerminate();
        var pCb = glfwSetErrorCallback(null);
        if (pCb != null)
            pCb.free();
    }

    /**
     * It will be called on calling {@link #start()}.
     */
    public void onStarting() {
    }

    /**
     * It will be called after {@link GL#createCapabilities(boolean) creating} OpenGL capabilities.
     */
    public void init() {
    }

    /**
     * It will be called on calling {@link #run()}.
     */
    public void onRunning() {
    }

    /**
     * It will be called on ticking.
     */
    public void tick() {
    }

    /**
     * It will be called on rendering.
     *
     * @param delta The timer delta
     */
    public void render(double delta) {
    }

    public void resize(int width, int height) {
    }

    /**
     * It will be called on calling {@link #exit()}.
     */
    public void onExiting() {
    }

    public Window getWindow() {
        return window;
    }

    public void setTitle(String title) {
        window.setTitle(title);
    }

    public String getTitle() {
        return window.getTitle();
    }
}
