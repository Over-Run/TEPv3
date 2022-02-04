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

package org.overrun.tepv3.phys;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Objects;
import java.util.StringJoiner;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.joml.Intersectiond.testAabAab;

/**
 * This is the axis-aligned bounding box.
 *
 * @author squid233
 * @since 3.0.1
 */
public class AABBox {
    public final Vector3d min, max;

    public AABBox(Vector3dc min, Vector3dc max) {
        this(new Vector3d(min), new Vector3d(max));
    }

    public AABBox(Vector3d min, Vector3d max) {
        this.min = min;
        this.max = max;
    }

    public AABBox(double minX,
                  double minY,
                  double minZ,
                  double maxX,
                  double maxY,
                  double maxZ) {
        this(new Vector3d(minX, minY, minZ), new Vector3d(maxX, maxY, maxZ));
    }

    public AABBox() {
        this(0, 0, 0, 0, 0, 0);
    }

    /**
     * Expand dest box from single orient.
     *
     * @param x    the x addend
     * @param y    the y addend
     * @param z    the z addend
     * @param dest The dest box to be expanded
     * @return dest
     * @see #expand(double, double, double)
     */
    public AABBox expand(double x,
                         double y,
                         double z,
                         AABBox dest) {
        dest.min.set(min.x + min(x, 0),
            min.y + min(y, 0),
            min.z + min(z, 0));
        dest.max.set(max.x + max(x, 0),
            max.y + max(y, 0),
            max.z + max(z, 0));
        return dest;
    }

    /**
     * Expand this box from single orient.
     *
     * @param x the x addend
     * @param y the y addend
     * @param z the z addend
     * @return this
     * @see #expand(double, double, double, AABBox)
     */
    public AABBox expand(double x,
                         double y,
                         double z) {
        return expand(x, y, z, this);
    }

    /**
     * Grow dest box from all orients.
     *
     * @param x    the x addend
     * @param y    the y addend
     * @param z    the z addend
     * @param dest The dest box to be grown
     * @return dest
     * @see #grow(double, double, double)
     */
    public AABBox grow(double x,
                       double y,
                       double z,
                       AABBox dest) {
        dest.min.set(min).sub(x, y, z);
        dest.max.set(max).add(x, y, z);
        return dest;
    }

    /**
     * Grow this box from all orients.
     *
     * @param x the x addend
     * @param y the y addend
     * @param z the z addend
     * @return this
     * @see #grow(double, double, double, AABBox)
     */
    public AABBox grow(double x,
                       double y,
                       double z) {
        return grow(x, y, z, this);
    }

    public double clipXCollide(AABBox other,
                               double x) {
        if (other.max.y <= min.y
            || other.min.y >= max.y
            || other.max.z <= min.z
            || other.min.z >= max.z)
            return x;
        double maxX;
        if (x > 0 && other.max.x <= min.x) {
            maxX = min.x - other.max.x;
            if (maxX < x)
                x = maxX;
        }
        if (x < 0 && other.min.x >= max.x) {
            maxX = max.x - other.min.x;
            if (maxX > x)
                x = maxX;
        }
        return x;
    }

    public double clipYCollide(AABBox other,
                               double y) {
        if (other.max.x <= min.x
            || other.min.x >= max.x
            || other.max.z <= min.z
            || other.min.z >= max.z)
            return y;
        double maxY;
        if (y > 0 && other.max.y <= min.y) {
            maxY = min.y - other.max.y;
            if (maxY < y)
                y = maxY;
        }
        if (y < 0 && other.min.y >= max.y) {
            maxY = max.y - other.min.y;
            if (maxY > y)
                y = maxY;
        }
        return y;
    }

    public double clipZCollide(AABBox other,
                               double z) {
        if (other.max.x <= min.x
            || other.min.x >= max.x
            || other.max.y <= min.y
            || other.min.y >= max.y)
            return z;
        double maxZ;
        if (z > 0 && other.max.z <= min.z) {
            maxZ = min.z - other.max.z;
            if (maxZ < z)
                z = maxZ;
        }
        if (z < 0 && other.min.z >= max.z) {
            maxZ = max.z - other.min.z;
            if (maxZ > z)
                z = maxZ;
        }
        return z;
    }

    /**
     * Check if {@code this} intersects with {@code other}.
     *
     * @param other The other box
     * @return Is intersecting
     */
    public boolean intersects(AABBox other) {
        return testAabAab(min, max, other.min, other.max);
    }

    /**
     * Move dest box.
     *
     * @param x    the x addend
     * @param y    the y addend
     * @param z    the z addend
     * @param dest The dest box to be moved
     * @return dest
     * @see #move(double, double, double)
     */
    public AABBox move(double x,
                       double y,
                       double z,
                       AABBox dest) {
        dest.min.set(min).add(x, y, z);
        dest.max.set(max).add(x, y, z);
        return dest;
    }

    /**
     * Move this box.
     *
     * @param x the x addend
     * @param y the y addend
     * @param z the z addend
     * @return this
     * @see #move(double, double, double, AABBox)
     */
    public AABBox move(double x,
                       double y,
                       double z) {
        return move(x, y, z, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var box = (AABBox) o;
        return Objects.equals(min, box.min) && Objects.equals(max, box.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AABBox.class.getSimpleName() + "[", "]")
            .add("min=" + min)
            .add("max=" + max)
            .toString();
    }
}
