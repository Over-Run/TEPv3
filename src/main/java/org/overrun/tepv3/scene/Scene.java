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

/**
 * <h2>The Scene</h2>
 * In TEPv3, the scene is the most important object. It is the highest-level
 * API.
 *
 * @author squid233
 * @since 3.0.1
 */
public class Scene implements IViewport {
    /**
     * The {@link Viewport.Mutable viewport}
     */
    protected Viewport.Mutable viewport;

    @Override
    public int getWidth() {
        return viewport.getWidth();
    }

    /**
     * Set the width of the viewport.
     *
     * @param width The width.
     */
    public void setWidth(int width) {
        viewport.setWidth(width);
    }

    @Override
    public int getHeight() {
        return viewport.getHeight();
    }

    /**
     * Set the height of the viewport.
     *
     * @param height The height.
     */
    public void setHeight(int height) {
        viewport.setHeight(height);
    }

    public float getAspectRatiof() {
        return (float) getWidth() / (float) getHeight();
    }
}
