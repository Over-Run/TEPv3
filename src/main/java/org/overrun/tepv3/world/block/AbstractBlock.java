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

package org.overrun.tepv3.world.block;

import org.overrun.tepv3.client.world.render.BlockRenderType;
import org.overrun.tepv3.world.World;
import org.overrun.tepv3.client.phys.VoxelShape;
import org.overrun.tepv3.client.phys.VoxelShapes;

import java.util.Random;

/**
 * @author squid233
 * @since 3.0.1
 */
public abstract class AbstractBlock {
    private final boolean isAir, isOpaque, isSolid;

    public AbstractBlock(Settings settings) {
        isAir = settings.isAir;
        isOpaque = settings.isOpaque;
        isSolid = settings.isSolid;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void randomTick(BlockState state,
                           World world,
                           int x,
                           int y,
                           int z,
                           Random random) {
    }

    public VoxelShape getCollisionShape(BlockState state) {
        return getOutlineShape(state);
    }

    public VoxelShape getOutlineShape(BlockState state) {
        return VoxelShapes.fullCube();
    }

    public VoxelShape getRayCastingShape(BlockState state) {
        return getOutlineShape(state);
    }

    public boolean isAir() {
        return isAir;
    }

    public boolean isOpaque() {
        return isOpaque;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public static class Settings {
        private boolean isAir = false, isOpaque = true, isSolid = true;

        public Settings air() {
            isAir = true;
            return this;
        }

        public Settings nonOpaque() {
            isOpaque = false;
            return this;
        }

        public Settings nonSolid() {
            isSolid = false;
            return this;
        }
    }
}
