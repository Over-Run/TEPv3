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

import java.util.function.Supplier;

/**
 * @author squid233
 * @since 3.0.1
 */
public class DefaultedRegistry<T>
    extends Registry<T> {
    private final Supplier<T> defaultEntry;

    public DefaultedRegistry(ResourceKey<T> resourceKey,
                             Supplier<T> defaultEntry) {
        super(resourceKey);
        this.defaultEntry = defaultEntry;
    }

    @Override
    public T get(Identifier id) {
        var e = super.get(id);
        return e == null ? defaultEntry.get() : e;
    }

    @Override
    public T get(int rawId) {
        var e = super.get(rawId);
        return e == null ? defaultEntry.get() : e;
    }

    @Override
    public Identifier getId(T entry) {
        var id = super.getId(entry);
        return id == null ? super.getId(defaultEntry.get()) : id;
    }

    @Override
    public int getRawId(T entry) {
        var inv = rawIdMap.inverse();
        if (!inv.containsKey(entry))
            return inv.get(defaultEntry.get());
        return inv.get(entry);
    }
}
