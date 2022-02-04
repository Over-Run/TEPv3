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

import org.overrun.tepv3.model.Mesh;
import org.overrun.tepv3.util.Direction;
import org.overrun.tepv3.util.Identifier;
import org.overrun.tepv3.util.registry.Registries;
import org.overrun.tepv3.world.World;

import static org.overrun.tepv3.util.Direction.*;
import static org.overrun.tepv3.util.Direction.SOUTH;

/**
 * @author squid233
 * @since 3.0.1
 */
public class GrassBlock extends Block {
    private static final Identifier TEXTURE_TOP = new Identifier("grass_block_top");
    private static final Identifier TEXTURE_SIDE = new Identifier("grass_block_side");

    public GrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void render(Mesh.Builder builder, World world, int layer, int x, int y, int z) {
        float c1 = 1.0f;
        float c2 = 0.8f;
        float c3 = 0.6f;
        if (shouldRenderFace(world, x - 1, y, z, layer)) {
            renderFace(builder.color(c3, c3, c3), x, y, z, WEST);
        }
        if (shouldRenderFace(world, x + 1, y, z, layer)) {
            renderFace(builder.color(c1, c1, c1), x, y, z, EAST);
        }
        if (shouldRenderFace(world, x, y - 1, z, layer)) {
            renderFace(builder.color(c3, c3, c3), x, y, z, DOWN);
        }
        if (shouldRenderFace(world, x, y + 1, z, layer)) {
            renderFace(builder.color(c1 * 0.56640625f,
                c1 * 0.73828125f,
                c1 * 0.34765625f), x, y, z, UP);
        }
        if (shouldRenderFace(world, x, y, z - 1, layer)) {
            renderFace(builder.color(c2, c2, c2), x, y, z, NORTH);
        }
        if (shouldRenderFace(world, x, y, z + 1, layer)) {
            renderFace(builder.color(c2, c2, c2), x, y, z, SOUTH);
        }
    }

    @Override
    protected Identifier getTexture(Direction face) {
        if (face == Direction.UP)
            return TEXTURE_TOP;
        if (face == Direction.DOWN)
            return Registries.BLOCK.getId(Blocks.DIRT);
        return TEXTURE_SIDE;
    }
}
