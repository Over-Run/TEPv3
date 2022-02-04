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

import java.util.StringJoiner;

/**
 * @author squid233
 * @since 3.0.1
 */
public enum VertexFormat {
    VERTEX3F(12, 1, 3),
    COLOR4F(16, 1 << 1, 4),
    TEX2F(8, 1 << 2, 2);

    private final int bytes;
    private final int mask;
    private final int count;

    VertexFormat(int bytes,
                 int mask,
                 int count) {
        this.bytes = bytes;
        this.mask = mask;
        this.count = count;
    }

    /**
     * Get all bytes. For example: 4 floats are {@code (4*4)=16} bytes
     *
     * @return The bytes
     */
    public int getBytes() {
        return bytes;
    }

    /**
     * Get the mask
     *
     * @return The mask
     */
    public int getMask() {
        return mask;
    }

    /**
     * Get the type count. For example: 3F is 3, 4F is 4
     *
     * @return The type count
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VertexFormat.class.getSimpleName() + "[", "]")
            .add("bytes=" + bytes)
            .add("mask=" + mask)
            .add("count=" + count)
            .toString();
    }
}
