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

import org.overrun.tepv3.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class TextureStitcher {
    public static SpriteAtlas generateAtlas(Identifier identifier,
                                            Map<Identifier, NativeImage> images) {
        var values = images.values().toArray(new NativeImage[0]);
        var blocks = new Block[values.length];
        for (int i = 0; i < values.length; i++) {
            var img = values[i];
            blocks[i] = new Block(img.width(), img.height());
        }
        var packer = new GrowingPacker();
        packer.fit(blocks);
        int w = packer.root.w;
        int h = packer.root.h;
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D,
            0,
            GL_RGBA,
            w,
            h,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            new int[w * h]);
        var map = new HashMap<Identifier, SpriteAtlas.Info>();
        var keys = images.keySet().toArray(new Identifier[0]);
        for (int i = 0; i < blocks.length; i++) {
            var block = blocks[i];
            if (block.fit != null) {
                map.put(keys[i], new SpriteAtlas.Info(keys[i],
                    block.fit.x,
                    block.fit.y,
                    block.w,
                    block.h));
                glTexSubImage2D(GL_TEXTURE_2D,
                    0,
                    block.fit.x,
                    block.fit.y,
                    block.w,
                    block.h,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    values[i].buffer());
            }
            values[i].close();
        }
        glGenerateMipmap(GL_TEXTURE_2D);
        return new SpriteAtlas(identifier, map, w, h, id);
    }
}
