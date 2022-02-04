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

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author squid233
 * @since 3.0.1
 */
public class VoxelShapes {
    private static final VoxelShape EMPTY = new VoxelShape() {
        @Override
        public List<AABBox> getBoxes() {
            return List.of();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @NotNull
        @Override
        public Iterator<AABBox> iterator() {
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public AABBox next() {
                    throw new NoSuchElementException();
                }
            };
        }
    };
    private static final SingleVoxelShape FULL_CUBE =
        cuboid(0, 0, 0, 1, 1, 1);

    public static VoxelShape empty() {
        return EMPTY;
    }

    public static SingleVoxelShape fullCube() {
        return FULL_CUBE;
    }

    public static SingleVoxelShape cuboid(
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ
    ) {
        return new SingleVoxelShape(new AABBox(minX, minY, minZ, maxX, maxY, maxZ));
    }
}
