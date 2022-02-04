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

import org.overrun.tepv3.util.registry.Registries;
import org.overrun.tepv3.util.registry.Registry;
import org.overrun.tepv3.world.block.AbstractBlock.Settings;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Blocks {
    public static final AirBlock AIR = register(0, "air", new AirBlock(new Settings().air().nonOpaque().nonSolid()));
    public static final Block GRASS_BLOCK = register(1, "grass_block", new GrassBlock(new Settings()));
    public static final Block DIRT = register(2, "dirt", new Block(new Settings()));
    public static final Block STONE = register(3, "stone", new Block(new Settings()));
    public static final Block COBBLESTONE = register(4, "cobblestone", new Block(new Settings()));
    public static final Block BEDROCK = register(5, "bedrock", new Block(new Settings()));

    private static <T extends Block>
    T register(int rawId,
               String name,
               T block) {
        return Registry.register(Registries.BLOCK, rawId, name, block);
    }
}
