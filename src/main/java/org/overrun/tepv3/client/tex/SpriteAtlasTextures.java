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

package org.overrun.tepv3.client.tex;

import org.lwjgl.system.MemoryStack;
import org.overrun.tepv3.client.res.DefaultResourcePack;
import org.overrun.tepv3.client.res.IResourceFactory;
import org.overrun.tepv3.io.FileSystem;
import org.overrun.tepv3.util.Identifier;

import java.io.IOException;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * @author squid233
 * @since 3.0.1
 */
public class SpriteAtlasTextures {
    public static final Identifier BLOCK_ATLAS = new Identifier("textures/atlas/block.png");
    private static final HashMap<Identifier, SpriteAtlas> ATLASES = new HashMap<>();
    private static final int[] MISSING_TEXTURE = {
        0xfff800f8, 0xff000000,
        0xff000000, 0xfff800f8
    };

    public static SpriteAtlas getAtlas(Identifier id) {
        return ATLASES.get(id);
    }

    public static void generateAtlases() {
        var blockAtlas = generateBlockAtlas();
        DefaultResourcePack.putResource(BLOCK_ATLAS, blockAtlas);
        ATLASES.put(BLOCK_ATLAS, blockAtlas);
    }

    private static SpriteAtlas generateBlockAtlas() {
        Identifier[] blocks = {
            new Identifier("block/bedrock"),
            new Identifier("block/cobblestone"),
            new Identifier("block/dirt"),
            new Identifier("block/grass_block_side"),
            new Identifier("block/grass_block_side_overlay"),
            new Identifier("block/grass_block_top"),
            new Identifier("block/stone")};//todo collect models and replace
        var images = new HashMap<Identifier, NativeImage>(blocks.length);
        try (var stack = MemoryStack.stackPush()) {
            var xp = stack.mallocInt(1);
            var yp = stack.mallocInt(1);
            var cp = stack.mallocInt(1);
            for (var id : blocks) {
                var path = IResourceFactory.toPath(id.getNamespace(),
                    "textures/" + id.getPath() + ".png");
                try (var is = FileSystem.CLASS.findResource(
                    path,
                    TextureStitcher.class)) {
                    var bytes = requireNonNull(is, "{" + path + "} is null!")
                        .readAllBytes();
                    var buf = memAlloc(bytes.length).put(bytes).flip();
                    try {
                        var data = stbi_load_from_memory(buf, xp, yp, cp, STBI_rgb_alpha);
                        var failed = data == null;
                        if (failed) {
                            System.err.println("Error loading image {"
                                + id
                                + "}. Reason: "
                                + stbi_failure_reason());//todo use logger
                            data = memAlloc(64);
                            data.asIntBuffer().put(MISSING_TEXTURE);
                        }
                        images.put(id, new NativeImage(xp.get(0), yp.get(0), data, !failed));
                    } finally {
                        memFree(buf);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return TextureStitcher.generateAtlas(BLOCK_ATLAS, images);
    }
}
