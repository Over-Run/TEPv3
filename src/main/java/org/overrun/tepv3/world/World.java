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

package org.overrun.tepv3.world;

import org.overrun.tepv3.world.block.Block;
import org.overrun.tepv3.world.block.Blocks;
import org.overrun.tepv3.phys.AABBox;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author squid233
 * @since 3.0.1
 */
public class World {
    public final long seed;
    public final int width, height, depth;
    private final Block[] blocks;
    private final int[] lightmap;
    private final ArrayList<IWorldListener> worldListeners = new ArrayList<>();
    private final Random random;

    public World(long seed,
                 int width,
                 int height,
                 int depth) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.depth = depth;
        blocks = new Block[width * height * depth];
        lightmap = new int[width * depth];
        random = new Random(seed);
        generateTerrain();
        generateLightmap(0, 0, width, depth);
    }

    private void generateTerrain() {
        int[] hm = SimplexHeightmap.generate(seed, width, height, depth);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    int i = (y * depth + z) * width + x;
                    if (y == 0) {
                        blocks[i] = Blocks.BEDROCK;
                        continue;
                    }
                    Block block = Blocks.AIR;
                    var h = hm[x + z * width];
                    if (h - 1 == y) {
                        block = Blocks.GRASS_BLOCK;
                    } else if (h - y > 5) {
                        block = Blocks.STONE;
                    } else if (h - y <= 5 && y < h) {
                        block = Blocks.DIRT;
                    }
                    blocks[i] = block;
                }
            }
        }
    }

    public void generateLightmap(int x0,
                                 int z0,
                                 int w,
                                 int d) {
        for (int x = x0; x < x0 + w; x++) {
            for (int z = z0; z < z0 + d; z++) {
                int oldMap = lightmap[x + z * width];
                int y = height - 1;
                while (y > 0 && !isLightBlocker(x, y, z)) --y;
                lightmap[x + z * width] = y;
                if (oldMap != y) {
                    int yl0 = min(oldMap, y);
                    int yl1 = max(oldMap, y);
                    for (var listener : worldListeners)
                        listener.lightColumnChanged(x, z, yl0, yl1);
                }
            }
        }
    }

    public void addListener(IWorldListener listener) {
        worldListeners.add(listener);
    }

    public boolean isLightBlocker(int x, int y, int z) {
        var block = getBlockState(x, y, z);
        return block.isOpaque();
    }

    public ArrayList<AABBox> getCubes(AABBox origin) {
        var boxes = new ArrayList<AABBox>();
        int x0 = (int) origin.min.x;
        int x1 = (int) (origin.max.x + 1.0);
        int y0 = (int) origin.min.y;
        int y1 = (int) (origin.max.y + 1.0);
        int z0 = (int) origin.min.z;
        int z1 = (int) (origin.max.z + 1.0);

        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (z0 < 0) {
            z0 = 0;
        }
        if (x1 > width) {
            x1 = width;
        }
        if (y1 > height) {
            y1 = height;
        }
        if (z1 > depth) {
            z1 = depth;
        }
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                for (int z = z0; z < z1; z++) {
                    var state = getBlockState(x, y, z);
                    if (!state.isAir()) {
                        var shape = state.getCollisionShape(null);
                        if (!shape.isEmpty()) {
                            for (var box : shape.getBoxes()) {
                                boxes.add(box.move(x, y, z, new AABBox()));
                            }
                        }
                    }
                }
            }
        }
        return boxes;
    }

    public boolean isInBorder(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth;
    }

    public boolean setBlockState(Block block, int x, int y, int z, int flags) {
        if (isInBorder(x, y, z)) {
            int i = (y * depth + z) * width + x;
            if (block == blocks[i])
                return false;
            blocks[i] = block;
            generateLightmap(x, z, 1, 1);
            for (var listener : worldListeners)
                listener.blockChanged(x, y, z);
            return true;
        }
        return false;
    }

    public Block getBlockState(int x, int y, int z) {
        if (isInBorder(x, y, z))
            return blocks[(y * depth + z) * width + x];
        return Blocks.AIR;
    }

    public boolean isLit(int x, int y, int z) {
        return !isInBorder(x, y, z) || y >= lightmap[x + z * width];
    }

    public boolean isSolidBlock(int x, int y, int z) {
        return getBlockState(x, y, z).isSolid();
    }

    public void randomTick(int minX,
                           int minY,
                           int minZ,
                           int maxX,
                           int maxY,
                           int maxZ) {
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    getBlockState(x, y, z).randomTick(null, this, x, y, z, random);
                }
            }
        }
    }

    public Random getRandom() {
        return random;
    }

    public long getSeed() {
        return seed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }
}
