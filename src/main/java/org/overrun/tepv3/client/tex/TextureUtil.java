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
import org.overrun.tepv3.client.res.IResourceFactory;
import org.overrun.tepv3.io.FileSystem;
import org.overrun.tepv3.util.Identifier;

import java.io.IOException;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * @author squid233
 * @since 3.0.1
 */
public class TextureUtil {
    private static final HashMap<Identifier, Integer> TEXTURES = new HashMap<>();
    private static final int[] MISSING_TEXTURE = {
        0xfff800f8, 0xff000000,
        0xff000000, 0xfff800f8
    };

    public static NativeImage loadTexture(Identifier id) {
        int glId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, glId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        try (var stack = MemoryStack.stackPush()) {
            var xp = stack.mallocInt(1);
            var yp = stack.mallocInt(1);
            var cp = stack.mallocInt(1);
            var path = IResourceFactory.toPath(id);
            try (var is = FileSystem.BUILTIN.findResource(path, null)) {
                var bytes = requireNonNull(is, "{" + path + "} is null!")
                    .readAllBytes();
                var buf = memAlloc(bytes.length).put(bytes).flip();
                try {
                    var data = stbi_load_from_memory(buf, xp, yp, cp, STBI_default);
                    var failed = data == null;
                    if (failed) {
                        System.err.println("Error loading image {"
                            + id
                            + "}. Reason: "
                            + stbi_failure_reason());//todo use logger
                        data = memAlloc(64);
                        data.asIntBuffer().put(MISSING_TEXTURE);
                    }
                    int w = xp.get(0);
                    int h = yp.get(0);
                    glTexImage2D(GL_TEXTURE_2D,
                        0,
                        GL_RGBA,
                        w,
                        h,
                        0,
                        cp.get(0) == STBI_rgb ? GL_RGB : GL_RGBA,
                        GL_UNSIGNED_BYTE,
                        data);
                    glGenerateMipmap(GL_TEXTURE_2D);
                    putTexture(id, glId);
                    return new NativeImage(w, h, data, !failed);
                } finally {
                    memFree(buf);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int findTexture(Identifier id) {
        return TEXTURES.get(id);
    }

    private static void putTexture(Identifier id,
                                  int glId) {
        TEXTURES.put(id, glId);
    }
}
