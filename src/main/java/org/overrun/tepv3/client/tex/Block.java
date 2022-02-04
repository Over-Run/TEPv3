/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Overrun Organization
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

package org.overrun.tepv3.client.tex;

import java.util.StringJoiner;

/**
 * Don't confuse with {@link org.overrun.tepv3.world.block.Block world.Block}
 *
 * @author squid233
 * @since 3.0.1
 */
public class Block {
    public Node fit;
    public int w, h;

    /**
     * Construct with the params fit node and position
     *
     * @param fit Fit node
     * @param w   width
     * @param h   height
     */
    public Block(Node fit,
                 int w,
                 int h) {
        this.fit = fit;
        this.w = w;
        this.h = h;
    }

    /**
     * Construct with the params position
     *
     * @param w width
     * @param h height
     */
    public Block(int w,
                 int h) {
        this.w = w;
        this.h = h;
    }

    public static Block of(int x,
                           int y,
                           int w,
                           int h) {
        return new Block(new Node(x, y, w, h), w, h);
    }

    /**
     * Construct without params
     */
    public Block() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Block.class.getSimpleName() + "[", "]")
            .add("fit=" + fit)
            .add("w=" + w)
            .add("h=" + h)
            .toString();
    }
}