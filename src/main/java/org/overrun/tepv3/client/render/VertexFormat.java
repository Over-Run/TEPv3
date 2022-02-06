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

import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class VertexFormat {
    private static final Map<String, VertexFormat> FORMAT_MAP = new HashMap<>();
    private final List<VertexFormatElement> elements;
    private final Map<String, VertexFormatElement> elementMap;
    private final IntArrayList offsets = new IntArrayList();
    private final int size;
    private int vao, vbo, ebo;

    public VertexFormat(Map<String, VertexFormatElement> elementMap) {
        this.elementMap = elementMap;
        var values = elementMap.values();
        elements = values.stream().toList();
        FORMAT_MAP.put(elements.toString(), this);
        int i = 0;
        for (var element : elements) {
            offsets.add(i);
            i += element.getByteLength();
        }
        size = i;
    }

    @Nullable
    public static VertexFormat fromElements(List<VertexFormatElement> elements) {
        return FORMAT_MAP.get(elements.toString());
    }

    @Override
    public String toString() {
        return "format: " + elementMap.size() + " elements: " + elementMap.entrySet().stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    public int getIntVertexSize() {
        return getVertexSize() / 4;
    }

    public int getVertexSize() {
        return size;
    }

    public List<VertexFormatElement> getElements() {
        return elements;
    }

    public List<String> getShaderAttributes() {
        return elementMap.keySet().stream().toList();
    }

    public void startDrawing() {
        innerStartDrawing();
    }

    private void innerStartDrawing() {
        int i = getVertexSize();
        var list = getElements();
        for (int j = 0; j < list.size(); j++) {
            list.get(j).startDrawing(j, offsets.getInt(j), i);
        }
    }

    public void endDrawing() {
        innerEndDrawing();
    }

    private void innerEndDrawing() {
        var list = getElements();
        for (int i = 0; i < list.size(); i++) {
            var element = list.get(i);
            element.endDrawing(i);
        }
    }

    public int getVertexArray() {
        if (vao == 0)
            vao = glGenVertexArrays();
        return vao;
    }

    public int getVertexBuffer() {
        if (vbo == 0)
            vbo = glGenBuffers();
        return vbo;
    }

    public int getElementBuffer() {
        if (ebo == 0)
            ebo = glGenBuffers();
        return ebo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (VertexFormat) o;
        return size == that.size && Objects.equals(elementMap, that.elementMap);
    }

    @Override
    public int hashCode() {
        return elementMap.hashCode();
    }

    public enum DrawMode {
        LINES(GL_TRIANGLES, 2, 2),
        LINE_STRIP(GL_TRIANGLE_STRIP, 2, 1),
        DEBUG_LINES(GL_LINES, 2, 2),
        DEBUG_LINE_STRIP(GL_LINE_STRIP, 2, 1),
        TRIANGLES(GL_TRIANGLES, 3, 3),
        TRIANGLE_STRIP(GL_TRIANGLE_STRIP, 3, 1),
        TRIANGLE_FAN(GL_TRIANGLE_FAN, 3, 1),
        QUADS(GL_TRIANGLES, 4, 4);

        public final int mode;
        public final int vertexCount;
        public final int size;

        DrawMode(int mode, int vertexCount, int size) {
            this.mode = mode;
            this.vertexCount = vertexCount;
            this.size = size;
        }

        public int getSize(int vertexCount) {
            return switch (this) {
                case LINE_STRIP, DEBUG_LINES, DEBUG_LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> vertexCount;
                case LINES, QUADS -> vertexCount / 4 * 6;
            };
        }
    }

    public enum IntType {
        BYTE(GL_UNSIGNED_BYTE, 1),
        SHORT(GL_UNSIGNED_SHORT, 2),
        INT(GL_UNSIGNED_INT, 4);

        public final int count;
        public final int size;

        IntType(int count, int size) {
            this.count = count;
            this.size = size;
        }

        public static IntType getSmallestTypeFor(int number) {
            if ((number & 0xFFFF0000) != 0) {
                return INT;
            }
            if ((number & 0xFF00) != 0) {
                return SHORT;
            }
            return BYTE;
        }
    }
}
