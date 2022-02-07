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

package org.overrun.tepv3.client.res;

import org.overrun.tepv3.client.tex.Texture2D;
import org.overrun.tepv3.client.tex.TextureUtil;
import org.overrun.tepv3.util.BuiltinResources;
import org.overrun.tepv3.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 3.0.1
 */
public class DefaultResourcePack implements IResourceFactory {
    private static final Map<Identifier, Resource> RESOURCE_MAP = new HashMap<>();

    public static void putResource(Identifier id, Resource resource) {
        RESOURCE_MAP.put(id, resource);
    }

    @Override
    public Resource getResource(Identifier id) {
        if (RESOURCE_MAP.containsKey(id))
            return RESOURCE_MAP.get(id);
        var path = IResourceFactory.toPath(id);
        if (ResourceType.MODELS.isSameAs(id)) {
            var res = new Resource(ResourceType.MODELS,
                id,
                false,
                BuiltinResources.getContent(path),
                null) {
                @Override
                public void close() {
                }
            };
            RESOURCE_MAP.put(id, res);
            return res;
        }
        if (ResourceType.SHADERS.isSameAs(id)) {
            var res = new Resource(ResourceType.SHADERS,
                id,
                false,
                BuiltinResources.getContent(path),
                null) {
                @Override
                public void close() {
                }
            };
            RESOURCE_MAP.put(id, res);
            return res;
        }
        if (ResourceType.TEXTURES.isSameAs(id)) {
            var res = new Texture2D(id, TextureUtil.loadTexture(id));
            putResource(id, res);
            return res;
        }
        return null;
    }
}
