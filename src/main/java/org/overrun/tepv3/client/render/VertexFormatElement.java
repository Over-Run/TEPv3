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

package org.overrun.tepv3.client.render;

import java.util.Objects;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class VertexFormatElement {
    private final DataType dataType;
    private final Type type;
    private final int textureIndex;
    private final int length;
    /**
     * The total length of this element (in bytes).
     */
    private final int byteLength;

    public VertexFormatElement(int textureIndex, DataType dataType, Type type, int length) {
        if (!isValidType(textureIndex, type)) {
            throw new IllegalStateException("Multiple vertex elements of the same type other than UVs are not supported");
        }
        this.type = type;
        this.dataType = dataType;
        this.textureIndex = textureIndex;
        this.length = length;
        this.byteLength = dataType.getByteLength() * length;
    }

    private boolean isValidType(int index, Type type) {
        return index == 0 || type == Type.UV;
    }

    public final DataType getDataType() {
        return dataType;
    }

    public final Type getType() {
        return type;
    }

    public final int getLength() {
        return length;
    }

    public final int getTextureIndex() {
        return textureIndex;
    }

    public final int getByteLength() {
        return byteLength;
    }

    public final boolean isPosition() {
        return type == Type.POSITION;
    }

    public void startDrawing(int elementIndex, long pointer, int stride) {
        this.type.startDrawing(length, dataType.getId(), stride, pointer, textureIndex, elementIndex);
    }

    public void endDrawing(int elementIndex) {
        this.type.endDrawing(textureIndex, elementIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (VertexFormatElement) o;
        return length == that.length && textureIndex == that.textureIndex && dataType == that.dataType && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataType, type, textureIndex, length);
    }

    @Override
    public String toString() {
        return length + "," + type.getName() + "," + dataType.getName();
    }

    public enum Type {
        POSITION("position",
            (size, type, stride, pointer, textureIndex, elementIndex) -> {
                glEnableVertexAttribArray(elementIndex);
                glVertexAttribPointer(elementIndex, size, type, false, stride, pointer);
            },
            (textureIndex, elementIndex) -> glDisableVertexAttribArray(elementIndex)),
        NORMAL("Normal",
            (size, type, stride, pointer, textureIndex, elementIndex) -> {
                glEnableVertexAttribArray(elementIndex);
                glVertexAttribPointer(elementIndex, size, type, true, stride, pointer);
            },
            (textureIndex, elementIndex) -> glDisableVertexAttribArray(elementIndex)),
        COLOR("Vertex Color",
            (size, type, stride, pointer, textureIndex, elementIndex) -> {
                glEnableVertexAttribArray(elementIndex);
                glVertexAttribPointer(elementIndex, size, type, true, stride, pointer);
            },
            (textureIndex, elementIndex) -> glDisableVertexAttribArray(elementIndex)),
        UV("UV",
            (size, type, stride, pointer, textureIndex, elementIndex) -> {
                glEnableVertexAttribArray(elementIndex);
                if (type == GL_FLOAT)
                    glVertexAttribPointer(elementIndex, size, type, false, stride, pointer);
                else
                    glVertexAttribIPointer(elementIndex, size, type, stride, pointer);
            },
            (textureIndex, elementIndex) -> glDisableVertexAttribArray(elementIndex)),
        PADDING("Padding",
            (size, type, stride, pointer, textureIndex, elementIndex) -> {
            },
            (textureIndex, elementIndex) -> {
            }),
        GENERIC("Generic",
            (size, type, stride, pointer, textureIndex, elementIndex) -> {
                glEnableVertexAttribArray(elementIndex);
                glVertexAttribPointer(elementIndex, size, type, false, stride, pointer);
            },
            (textureIndex, elementIndex) -> glDisableVertexAttribArray(elementIndex));

        private final String name;
        private final Starter starter;
        private final Finisher finisher;

        Type(String name, Starter starter, Finisher finisher) {
            this.name = name;
            this.starter = starter;
            this.finisher = finisher;
        }

        public void startDrawing(int size, int type, int stride, long pointer, int textureIndex, int elementIndex) {
            starter.setupBufferState(size, type, stride, pointer, textureIndex, elementIndex);
        }

        public void endDrawing(int textureIndex, int elementIndex) {
            finisher.clearBufferState(textureIndex, elementIndex);
        }

        public String getName() {
            return name;
        }

        @FunctionalInterface
        interface Starter {
            void setupBufferState(int size, int type, int stride, long pointer, int textureIndex, int elementIndex);
        }

        @FunctionalInterface
        interface Finisher {
            void clearBufferState(int textureIndex, int elementIndex);
        }
    }

    public enum DataType {
        FLOAT(4, "Float", GL_FLOAT),
        UBYTE(1, "Unsigned Byte", GL_UNSIGNED_BYTE),
        BYTE(1, "Byte", GL_BYTE),
        USHORT(2, "Unsigned Short", GL_UNSIGNED_SHORT),
        SHORT(2, "Short", GL_SHORT),
        UINT(4, "Unsigned Int", GL_UNSIGNED_INT),
        INT(4, "Int", GL_INT);

        private final int byteLength;
        private final String name;
        private final int id;

        DataType(int byteCount, String name, int id) {
            this.byteLength = byteCount;
            this.name = name;
            this.id = id;
        }

        public int getByteLength() {
            return byteLength;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}
