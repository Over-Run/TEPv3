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

package org.overrun.tepv3.client.world.render;

import org.overrun.tepv3.client.Frustum;
import org.overrun.tepv3.client.tex.SpriteAtlasTextures;
import org.overrun.tepv3.client.world.ClientChunk;
import org.overrun.tepv3.gl.RenderSystem;
import org.overrun.tepv3.world.DirtyChunkSorter;
import org.overrun.tepv3.world.IWorldListener;
import org.overrun.tepv3.world.World;
import org.overrun.tepv3.world.entity.PlayerEntity;

import java.util.ArrayList;

import static org.overrun.tepv3.world.Chunk.CHUNK_SIZE;

/**
 * @author squid233
 * @since 3.0.1
 */
public class WorldRenderer implements IWorldListener {
    public static final int MAX_REBUILDS_PER_FRAME = 8;
    private final World world;
    private final ClientChunk[] chunks;
    private final int xChunks;
    private final int yChunks;
    private final int zChunks;

    public WorldRenderer(World world) {
        this.world = world;
        world.addListener(this);
        xChunks = world.width / CHUNK_SIZE;
        yChunks = world.height / CHUNK_SIZE;
        zChunks = world.depth / CHUNK_SIZE;
        chunks = new ClientChunk[xChunks * yChunks * zChunks];
        for (int x = 0; x < xChunks; x++) {
            for (int y = 0; y < yChunks; y++) {
                for (int z = 0; z < zChunks; z++) {
                    int x0 = x * CHUNK_SIZE;
                    int y0 = y * CHUNK_SIZE;
                    int z0 = z * CHUNK_SIZE;
                    int x1 = (x + 1) * CHUNK_SIZE;
                    int y1 = (y + 1) * CHUNK_SIZE;
                    int z1 = (z + 1) * CHUNK_SIZE;

                    if (x1 > world.width) {
                        x1 = world.width;
                    }
                    if (y1 > world.height) {
                        y1 = world.height;
                    }
                    if (z1 > world.depth) {
                        z1 = world.depth;
                    }

                    chunks[(x + y * xChunks) * zChunks + z] =
                        new ClientChunk(world, x0, y0, z0, x1, y1, z1);
                }
            }
        }
    }

    public ArrayList<ClientChunk> getAllDirtyChunks() {
        ArrayList<ClientChunk> dirty = null;
        for (var chunk : chunks) {
            if (chunk.isDirty()) {
                if (dirty == null)
                    dirty = new ArrayList<>();
                dirty.add(chunk);
            }
        }
        return dirty;
    }

    public void render(int layer) {
        RenderSystem.setShaderTexture(SpriteAtlasTextures.BLOCK_ATLAS);
        var frustum = Frustum.getFrustum();
        for (var chunk : chunks) {
            if (frustum.testAab(chunk.box)) {
                chunk.render(layer);
            }
        }
        RenderSystem.setShaderTexture(0);
    }

    public void updateDirtyChunks(PlayerEntity player) {
        var dirty = getAllDirtyChunks();
        if (dirty != null) {
            dirty.sort(new DirtyChunkSorter(player, Frustum.getFrustum()));
            for (int i = 0; i < MAX_REBUILDS_PER_FRAME && i < dirty.size(); i++) {
                dirty.get(i).rebuild();
            }
        }
    }

    public void markDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
        x0 /= 16;
        x1 /= 16;
        y0 /= 16;
        y1 /= 16;
        z0 /= 16;
        z1 /= 16;

        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (z0 < 0) {
            z0 = 0;
        }
        if (x1 >= xChunks) {
            x1 = xChunks - 1;
        }
        if (y1 >= yChunks) {
            y1 = yChunks - 1;
        }
        if (z1 >= zChunks) {
            z1 = zChunks - 1;
        }

        for (int x = x0; x <= x1; ++x) {
            for (int y = y0; y <= y1; ++y) {
                for (int z = z0; z <= z1; ++z) {
                    chunks[(x + y * xChunks) * zChunks + z].markDirty();
                }
            }
        }
    }

    @Override
    public void blockChanged(int x, int y, int z) {
        markDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    @Override
    public void lightColumnChanged(int x, int z, int y0, int y1) {
        markDirty(x - 1, y0 - 1, z - 1, x + 1, y1 + 1, z + 1);
    }

    @Override
    public void allChanged() {
        markDirty(0, 0, 0, world.width, world.height, world.depth);
    }

    public void free() {
        for (var chunk : chunks) {
            chunk.free();
        }
    }
}
