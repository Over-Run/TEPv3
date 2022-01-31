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

package org.overrun.tepv3;

import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Window {
    private boolean created = false;
    private String title;
    private long handle = NULL;

    public void create(final int width,
                       final int height,
                       final CharSequence title,
                       final long monitor,
                       final long share) {
        if (!created) {
            handle = glfwCreateWindow(width, height, title, monitor, share);
            this.title = title.toString();
        }
        created = true;
    }

    public void check(Runnable chkFunc) {
        if (handle == NULL)
            chkFunc.run();
    }

    public void onKey(GLFWKeyCallbackI cb) {
        glfwSetKeyCallback(handle, cb);
    }

    public void onResizing(GLFWFramebufferSizeCallbackI cb) {
        glfwSetFramebufferSizeCallback(handle, cb);
    }

    public void makeCtxCurrent() {
        glfwMakeContextCurrent(handle);
    }

    public void show() {
        glfwShowWindow(handle);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void swapBuffers() {
        glfwSwapBuffers(handle);
    }

    public void getSize(IntBuffer wp, IntBuffer hp) {
        glfwGetWindowSize(handle, wp, hp);
    }

    public void setPos(int x, int y) {
        glfwSetWindowPos(handle, x, y);
    }

    public void freeCallbacks() {
        glfwFreeCallbacks(handle);
    }

    public void destroy() {
        glfwDestroyWindow(handle);
    }

    public long getHandle() {
        return handle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (created)
            glfwSetWindowTitle(handle, title);
    }
}
