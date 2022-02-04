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

package org.overrun.tepv3.util.registry;

import org.overrun.tepv3.util.Identifier;
import org.overrun.tepv3.world.block.Block;

/**
 * @author squid233
 * @since 3.0.1
 */
public class ResourceKey<T> {
    public static final ResourceKey<?> ROOT = new ResourceKey<>(null, new Identifier("root"));
    public static final ResourceKey<Block> BLOCK = create("block");
    private final ResourceKey<?> parent;
    private final Identifier id;

    private ResourceKey(ResourceKey<?> parent,
                        Identifier id) {
        this.parent = parent;
        if (parent != null)
            this.id = new Identifier(id.getNamespace(),
                parent.id.getPath() + "." + id.getPath());
        else
            this.id = id;
    }

    public static <T> ResourceKey<T> create(String key) {
        return new ResourceKey<>(ROOT, new Identifier(key));
    }

    public ResourceKey<?> getParent() {
        return parent;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public String toString() {
        return "resourceKey{" + id + "}";
    }
}
