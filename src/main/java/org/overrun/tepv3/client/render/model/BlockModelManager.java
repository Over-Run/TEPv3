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

package org.overrun.tepv3.client.render.model;

import org.overrun.tepv3.client.TEPv3Client;
import org.overrun.tepv3.util.Identifier;
import org.overrun.tepv3.util.JsonHelper;
import org.overrun.tepv3.util.registry.Registries;

import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 3.0.1
 */
@Deprecated(since = "3.0.1", forRemoval = true)
public class BlockModelManager {
    private final Map<Identifier, BlockModel> models = new HashMap<>();
    private final TEPv3Client client;

    public BlockModelManager(TEPv3Client client) {
        this.client = client;
    }

    /**
     * Load models from {@link Registries#BLOCK registry table}.
     */
    public void loadModels() {
        for (var e : Registries.BLOCK) {
            if (e.getValue().isAir())
                continue;
            var id = e.getKey();
            var resId = new Identifier(
                id.getNamespace(),
                "models/block/" + id.getPath() + ".json"
            );
            var res = client.defaultResourcePack.getResource(resId);
            models.put(id, new BlockModel(JsonHelper.deserialize(res.content())));
        }
    }

    /**
     * Get a block model by an identifier.
     *
     * @param id The id
     * @return The model
     */
    public BlockModel getModel(Identifier id) {
        return models.get(id);
    }
}
