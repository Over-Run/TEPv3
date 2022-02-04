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

import org.joml.Vector3i;

import java.util.Objects;

/**
 * @author squid233
 * @since 3.0.1
 */
public class BlockPos {
    protected final Vector3i pos;

    public BlockPos(BlockPos pos) {
        this.pos = pos.pos;
    }

    public BlockPos(Vector3i pos) {
        this.pos = pos;
    }

    public BlockPos(int x, int y, int z) {
        this(new Vector3i(x, y, z));
    }

    public BlockPos() {
        this(new Vector3i());
    }

    public static class Mutable extends BlockPos {
        public Mutable(BlockPos pos) {
            super(pos);
        }

        public Mutable(Vector3i pos) {
            super(pos);
        }

        public Mutable(int x, int y, int z) {
            super(x, y, z);
        }

        public Mutable() {
        }

        public void setPos(BlockPos pos) {
            this.pos.set(pos.pos);
        }

        public void setPos(Vector3i pos) {
            this.pos.set(pos);
        }

        public void setPos(int x, int y, int z) {
            this.pos.set(x, y, z);
        }
    }

    /**
     * Get the long value from a position.
     * <p>
     * The pos must be smaller than 2097152 (exclusive).
     * </p>
     *
     * @param x The pos x
     * @param y The pos y
     * @param z The pos z
     * @return The long value
     */
    public static long getLongValue(int x, int y, int z) {
        long l = z;
        l |= (long) y << 21;
        l |= (long) x << 42;
        return l;
    }

    public static int xFromLong(long l) {
        return (int) (l >> 42L);
    }

    public static int yFromLong(long l) {
        return (int) (l >> 21L << 42L >> 42L);
    }

    public static int zFromLong(long l) {
        return (int) (l << 42L >> 42L);
    }

    public static BlockPos fromLong(long l) {
        return new BlockPos(xFromLong(l), yFromLong(l), zFromLong(l));
    }

    public long asLong() {
        return getLongValue(getX(), getY(), getZ());
    }

    public int getX() {
        return pos.x;
    }

    public int getY() {
        return pos.y;
    }

    public int getZ() {
        return pos.z;
    }

    public Mutable toMutable() {
        return new Mutable(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var blockPos = (BlockPos) o;
        return Objects.equals(pos, blockPos.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}
