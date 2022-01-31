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

import org.lwjgl.glfw.GLFWErrorCallback;

/**
 * <h2>The configs</h2>
 * Contains all configs to init the engine.
 *
 * @author squid233
 * @since 3.0.1
 */
public class Configs {
    /**
     * The error callback for GLFW.
     * <p>If not set, it will auto set to
     * {@link GLFWErrorCallback#createPrint(java.io.PrintStream)
     * createPrint(System.err)}.</p>
     */
    public static GLFWErrorCallback glfwErrorCallback;
    /**
     * Function on GLFW initializing failed.
     */
    public static Runnable glfwInitFailed = () -> {
        throw new IllegalStateException("Unable to initialize GLFW");
    };
    /**
     * Function on GLFW window creating failed.
     */
    public static Runnable glfwWindowCreateFailed = () -> {
        throw new RuntimeException("Failed to create the GLFW window");
    };
    /**
     * Set GLFW swap interval.
     * <p>
     * The final swapping interval in rendering is:<br>
     * {@code swapInterval * MonitorFlushFrequency}
     * </p>
     */
    public static int swapInterval = 0;
    /**
     * Ticks per seconds.
     */
    public static int tps = 20;
}
