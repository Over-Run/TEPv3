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

import org.overrun.tepv3.client.Frustum;
import org.overrun.tepv3.client.world.ClientChunk;
import org.overrun.tepv3.world.entity.PlayerEntity;

import java.util.Comparator;

/**
 * @author squid233
 * @since 3.0.1
 */
public class DirtyChunkSorter implements Comparator<ClientChunk> {
    private final PlayerEntity player;
    private final Frustum frustum;
    private final long now = System.currentTimeMillis();

    public DirtyChunkSorter(PlayerEntity player, Frustum frustum) {
        this.player = player;
        this.frustum = frustum;
    }

    @Override
    public int compare(ClientChunk c0, ClientChunk c1) {
        var i0 = frustum.testAab(c0.box);
        var i1 = frustum.testAab(c1.box);
        if (i0 && !i1) return -1;
        if (i1 && !i0) return 1;
        int t0 = (int) ((now - c0.dirtiedTime) / 2000L);
        int t1 = (int) ((now - c1.dirtiedTime) / 2000L);
        if (t0 < t1) return -1;
        if (t0 > t1) return 1;
        return c0.distanceToSqr(player) < c1.distanceToSqr(player) ? -1 : 1;
    }
}
