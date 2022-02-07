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

package org.overrun.tepv3.client.world;

import org.overrun.tepv3.client.world.render.BlockRenderType;
import org.overrun.tepv3.world.Chunk;
import org.overrun.tepv3.world.World;
import org.overrun.tepv3.world.entity.Entity;
import org.overrun.tepv3.client.model.Mesh;
import org.overrun.tepv3.phys.AABBox;

import static java.lang.Math.fma;

/**
 * @author squid233
 * @since 3.0.1
 */
public class ClientChunk extends Chunk {
    public AABBox box;
    public final World world;
    public final int x0;
    public final int y0;
    public final int z0;
    public final int x1;
    public final int y1;
    public final int z1;
    public final double x;
    public final double y;
    public final double z;
    private boolean dirty = true;
    private final Mesh[] meshes = new Mesh[2];
    public long dirtiedTime = 0L;
    public static int updates;
    private static long totalTime;
    private static int totalUpdates;

    public ClientChunk(World world,
                       int x0,
                       int y0,
                       int z0,
                       int x1,
                       int y1,
                       int z1) {
        this.world = world;
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        x = (x0 + x1) / 2.0;
        y = (y0 + y1) / 2.0;
        z = (z0 + z1) / 2.0;
        box = new AABBox(x0, y0, z0, x1, y1, z1);
    }

    private void rebuild(int layer) {
        dirty = false;
        if (meshes[layer] != null) meshes[layer].free();
        ++updates;
        long before = System.nanoTime();
        int blocks = 0;
        var builder = new Mesh.Builder().enableQuad();
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                for (int z = z0; z < z1; z++) {
                    var state = world.getBlockState(x, y, z);
                    if (state.getRenderType(null) == BlockRenderType.MODEL) {
                        state.render(builder, world, layer, x, y, z);
                        ++blocks;
                    }
                }
            }
        }
        meshes[layer] = builder.build();
        long after = System.nanoTime();
        if (blocks > 0) {
            totalTime += after - before;
            ++totalUpdates;
        }
    }

    public void rebuild() {
        rebuild(0);
        rebuild(1);
    }

    public void render(int layer) {
        var mesh = meshes[layer];
        if (mesh != null) mesh.render();
    }

    public void markDirty() {
        if (!dirty) {
            dirtiedTime = System.currentTimeMillis();
        }
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public double distanceToSqr(Entity entity) {
        var xd = entity.position.x - x;
        var yd = entity.position.y - y;
        var zd = entity.position.z - z;
        return fma(xd, xd, fma(yd, yd, zd * zd));
    }

    public void free() {
        for (var mesh : meshes) {
            if (mesh != null) {
                mesh.free();
            }
        }
    }
}
