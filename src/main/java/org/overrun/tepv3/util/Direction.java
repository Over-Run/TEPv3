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

package org.overrun.tepv3.util;

import java.util.Locale;

/**
 * @author squid233
 * @since 3.0.1
 */
public enum Direction {
    WEST(0, 1, 1, -1, 0, 0),
    EAST(1, 0, 3, 1, 0, 0),
    DOWN(2, 3, 3, 0, -1, 0),
    UP(3, 2, 1, 0, 1, 0),
    NORTH(4, 5, 0, 0, 0, -1),
    SOUTH(5, 4, 2, 0, 0, 1);
    private final int id;
    private final int oppositeId;
    private final int horizontalId;
    private final int offsetX, offsetY, offsetZ;

    Direction(int id, int oppositeId, int horizontalId, int offsetX, int offsetY, int offsetZ) {
        this.id = id;
        this.oppositeId = oppositeId;
        this.horizontalId = horizontalId;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public static Direction getById(int id) {
        return values()[id];
    }

    public static Direction getByName(String name) {
        return valueOf(name.toUpperCase(Locale.ROOT));
    }

    public int getId() {
        return id;
    }

    public int getOppositeId() {
        return oppositeId;
    }

    public Direction opposite() {
        return getById(oppositeId);
    }

    public int getHorizontalId() {
        return horizontalId;
    }

    public int getRotation() {
        return (horizontalId & 3) * 90;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }
}
