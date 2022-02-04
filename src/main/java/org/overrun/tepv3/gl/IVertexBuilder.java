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

/**
 * @author squid233
 * @since 3.0.1
 */
public interface IVertexBuilder {
    /**
     * Push a vertex.
     *
     * @param x Pos x
     * @param y Pos y
     * @param z Pos z
     * @return this
     */
    IVertexBuilder vertex(float x, float y, float z);

    /**
     * Push a color.
     *
     * @param r Color red
     * @param g Color green
     * @param b Color blue
     * @param a Color alpha default to 1
     * @return this
     */
    IVertexBuilder color(float r, float g, float b, float a);

    /**
     * Push a color.
     *
     * @param r red
     * @param g green
     * @param b blue
     * @return this
     */
    IVertexBuilder color(float r, float g, float b);

    /**
     * Push a tex coord.
     *
     * @param u Coord u
     * @param v Coord v
     * @return this
     */
    IVertexBuilder tex(float u, float v);

    /**
     * Push an array.
     *
     * @param layout  The layout
     * @param rawData The data
     */
    void array(VertexLayout layout, float[] rawData);

    void next(float x,
              float y,
              float z,
              float r,
              float g,
              float b,
              float a,
              float u,
              float v);

    void next();
}
