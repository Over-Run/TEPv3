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
import org.overrun.tepv3.phys.VoxelShape;

import java.util.Random;

/**
 * @author squid233
 * @since 3.0.1
 */
public class BlockState {
    private final Block block;

    public BlockState(Block block) {
        this.block = block;
    }

    public BlockRenderType getRenderType() {
        return block.getRenderType(this);
    }

    public void randomTick(World world,
                           int x,
                           int y,
                           int z,
                           Random random) {
        block.randomTick(this, world, x, y, z, random);
    }

    public VoxelShape getCollisionShape() {
        return block.getCollisionShape(this);
    }

    public VoxelShape getOutlineShape() {
        return block.getOutlineShape(this);
    }

    public VoxelShape getRayCastingShape() {
        return block.getRayCastingShape(this);
    }

    public Block getBlock() {
        return block;
    }
}