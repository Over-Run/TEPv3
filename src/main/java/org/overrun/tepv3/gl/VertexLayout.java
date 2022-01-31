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

import java.util.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class VertexLayout {
    /**
     * The format set
     */
    private final LinkedHashSet<VertexFormat> formats = new LinkedHashSet<>();
    private boolean hasPos, hasColor;
    private int stride = 0;
    private int mask = 0;
    private int count = 0;

    public VertexLayout(VertexFormat... formats) {
        this(Arrays.asList(formats));
    }

    public VertexLayout(Collection<VertexFormat> formats) {
        formats.forEach(format -> {
            if (format == VertexFormat.VERTEX3F)
                hasPos = true;
            if (format == VertexFormat.COLOR4F)
                hasColor = true;
            stride += format.getBytes();
            mask |= format.getMask();
            count += format.getCount();
            this.formats.add(format);
        });
    }

    public boolean hasPos() {
        return hasPos;
    }

    public boolean hasColor() {
        return hasColor;
    }

    public LinkedHashSet<VertexFormat> getFormats() {
        return formats;
    }

    /**
     * Get the offset in the layout in bytes.
     *
     * @param format The vertex format
     * @return The offset
     */
    public int getOffset(VertexFormat format) {
        int off = 0;
        for (var fmt : formats) {
            if (fmt == format)
                break;
            off += fmt.getBytes();
        }
        return off;
    }

    /**
     * Get the offset in the layout not in bytes.
     *
     * @param format The vertex format
     * @return The offset
     */
    public int getOffsetCount(VertexFormat format) {
        int off = 0;
        for (var fmt : formats) {
            if (fmt == format)
                break;
            off += fmt.getCount();
        }
        return off;
    }

    /**
     * Get layout stride in bytes.
     *
     * @return The stride
     */
    public int getStride() {
        return stride;
    }

    public int getMask() {
        return mask;
    }

    /**
     * Get formats count.
     *
     * @return The count
     * @see #getStride()
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VertexLayout.class.getSimpleName() + "[", "]")
            .add("formats=" + formats)
            .add("hasPos=" + hasPos)
            .add("hasColor=" + hasColor)
            .add("stride=" + stride)
            .add("mask=" + mask)
            .add("count=" + count)
            .toString();
    }
}
