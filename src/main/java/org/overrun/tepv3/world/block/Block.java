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

import org.overrun.tepv3.client.model.Mesh;
import org.overrun.tepv3.util.Direction;
import org.overrun.tepv3.util.Identifier;
import org.overrun.tepv3.util.registry.Registries;
import org.overrun.tepv3.world.World;

import static org.overrun.tepv3.client.tex.SpriteAtlasTextures.BLOCK_ATLAS;
import static org.overrun.tepv3.client.tex.SpriteAtlasTextures.getAtlas;
import static org.overrun.tepv3.util.Direction.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Block extends AbstractBlock {
    public Block(Settings settings) {
        super(settings);
    }

    /**
     * render
     *
     * @param builder builder
     * @param world   world
     * @param layer   layer
     * @param x       x
     * @param y       y
     * @param z       z
     * @deprecated will remove in the future
     */
    @Deprecated
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
            renderFace(builder.color(c1, c1, c1), x, y, z, UP);
        }
        if (shouldRenderFace(world, x, y, z - 1, layer)) {
            renderFace(builder.color(c2, c2, c2), x, y, z, NORTH);
        }
        if (shouldRenderFace(world, x, y, z + 1, layer)) {
            renderFace(builder.color(c2, c2, c2), x, y, z, SOUTH);
        }
    }

    protected boolean shouldRenderFace(World world, int x, int y, int z, int layer) {
        return !world.isSolidBlock(x, y, z) && (world.isLit(x, y, z) ^ (layer == 1));
    }

    /**
     * getTexture
     *
     * @param face face
     * @return Identifier
     * @deprecated Will remove in the future and replace with models
     */
    @Deprecated
    protected Identifier getTexture(Direction face) {
        return Registries.BLOCK.getId(this);
    }

    @Deprecated
    public void renderFace(Mesh.Builder builder, int x, int y, int z, Direction face) {
        var tex = getTexture(face);
        var u0 = getAtlas(BLOCK_ATLAS).getU0(tex);
        var u1 = getAtlas(BLOCK_ATLAS).getU1(tex);
        var v0 = getAtlas(BLOCK_ATLAS).getV0(tex);
        var v1 = getAtlas(BLOCK_ATLAS).getV1(tex);
        float x0 = (float) x;
        float x1 = x + 1.0f;
        float y0 = (float) y;
        float y1 = y + 1.0f;
        float z0 = (float) z;
        float z1 = z + 1.0f;
        switch (face) {
            case WEST -> {
                builder.tex(u0, v0).vertex(x0, y1, z0).next();
                builder.tex(u0, v1).vertex(x0, y0, z0).next();
                builder.tex(u1, v1).vertex(x0, y0, z1).next();
                builder.tex(u1, v0).vertex(x0, y1, z1).next();
            }
            case EAST -> {
                builder.tex(u0, v0).vertex(x1, y1, z1).next();
                builder.tex(u0, v1).vertex(x1, y0, z1).next();
                builder.tex(u1, v1).vertex(x1, y0, z0).next();
                builder.tex(u1, v0).vertex(x1, y1, z0).next();
            }
            case DOWN -> {
                builder.tex(u0, v0).vertex(x0, y0, z1).next();
                builder.tex(u0, v1).vertex(x0, y0, z0).next();
                builder.tex(u1, v1).vertex(x1, y0, z0).next();
                builder.tex(u1, v0).vertex(x1, y0, z1).next();
            }
            case UP -> {
                builder.tex(u0, v0).vertex(x0, y1, z0).next();
                builder.tex(u0, v1).vertex(x0, y1, z1).next();
                builder.tex(u1, v1).vertex(x1, y1, z1).next();
                builder.tex(u1, v0).vertex(x1, y1, z0).next();
            }
            case NORTH -> {
                builder.tex(u0, v0).vertex(x1, y1, z0).next();
                builder.tex(u0, v1).vertex(x1, y0, z0).next();
                builder.tex(u1, v1).vertex(x0, y0, z0).next();
                builder.tex(u1, v0).vertex(x0, y1, z0).next();
            }
            case SOUTH -> {
                builder.tex(u0, v0).vertex(x0, y1, z1).next();
                builder.tex(u0, v1).vertex(x0, y0, z1).next();
                builder.tex(u1, v1).vertex(x1, y0, z1).next();
                builder.tex(u1, v0).vertex(x1, y1, z1).next();
            }
        }
    }

    public final BlockState getDefaultState() {
        return new BlockState(this);
    }
}
