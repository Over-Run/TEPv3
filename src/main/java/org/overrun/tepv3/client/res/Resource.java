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

package org.overrun.tepv3.client.res;

import org.overrun.tepv3.util.Identifier;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author squid233
 * @since 3.0.1
 */
public abstract class Resource implements AutoCloseable {
    private final ResourceType type;
    private final Identifier id;
    private final boolean isBinary;
    private final String content;
    private final ByteBuffer data;

    public Resource(ResourceType type,
                    Identifier id,
                    boolean isBinary,
                    String content,
                    ByteBuffer data) {
        this.type = type;
        this.id = id;
        this.isBinary = isBinary;
        this.content = content;
        this.data = data;
    }

    /**
     * Get the resource content when this isn't {@link #isBinary}
     * @return The content
     */
    public String content() {
        return content;
    }

    /**
     * Get the resource data when this is {@link #isBinary}
     * @return The binary data
     */
    public ByteBuffer data() {
        return data;
    }

    public ResourceType type() {
        return type;
    }

    public Identifier id() {
        return id;
    }

    public boolean isBinary() {
        return isBinary;
    }

    @Override
    public abstract void close() throws Exception;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Resource) obj;
        return Objects.equals(this.type, that.type) &&
            Objects.equals(this.id, that.id) &&
            this.isBinary == that.isBinary &&
            Objects.equals(this.content, that.content) &&
            Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, isBinary, content, data);
    }

    @Override
    public String toString() {
        return "Resource[" +
            "type=" + type + ", " +
            "id=" + id + ", " +
            "isBinary=" + isBinary + ", " +
            "content=" + content + ", " +
            "data=" + data + ']';
    }

}
