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

import org.overrun.tepv3.client.res.Resource;
import org.overrun.tepv3.client.res.ResourceType;
import org.overrun.tepv3.util.Identifier;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author squid233
 * @since 3.0.1
 */
public class SpriteAtlas extends Resource {
    private final HashMap<Identifier, Info> infoMap;
    private final int width;
    private final int height;
    private final int glId;

    public SpriteAtlas(
        Identifier id,
        HashMap<Identifier, Info> infoMap,
        int width, int height, int glId) {
        super(ResourceType.TEXTURES,
            id,
            true,
            null,
            null);
        this.infoMap = infoMap;
        this.width = width;
        this.height = height;
        this.glId = glId;
    }

    public Info getInfo(Identifier id) {
        return infoMap.get(id);
    }

    public float getU0(Identifier id) {
        return (float) getInfo(id).u / (float) width;
    }

    public float getU1(Identifier id) {
        var info = getInfo(id);
        return (float) (info.u + info.width) / (float) width;
    }

    public float getV0(Identifier id) {
        return (float) getInfo(id).v / (float) height;
    }

    public float getV1(Identifier id) {
        var info = getInfo(id);
        return (float) (info.v + info.height) / (float) height;
    }

    public HashMap<Identifier, Info> infoMap() {
        return infoMap;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int glId() {
        return glId;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SpriteAtlas) obj;
        return Objects.equals(this.infoMap, that.infoMap) &&
            this.width == that.width &&
            this.height == that.height &&
            this.glId == that.glId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(infoMap, width, height, glId);
    }

    @Override
    public String toString() {
        return "SpriteAtlas[" +
            "infoMap=" + infoMap + ", " +
            "width=" + width + ", " +
            "height=" + height + ", " +
            "glId=" + glId + ']';
    }


    public record Info(Identifier id, int u, int v, int width, int height) {
    }
}
