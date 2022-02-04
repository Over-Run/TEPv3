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

import org.jetbrains.annotations.NotNull;
import org.overrun.tepv3.util.Identifier;
import org.overrun.tepv3.util.BiHashMap;

import java.util.Iterator;

/**
 * The registry table.
 *
 * @author squid233
 * @since 3.0.1
 */
public class Registry<T> implements Iterable<T> {
    protected final BiHashMap<Identifier, T> entryMap = new BiHashMap<>();
    protected final BiHashMap<Integer, T> rawIdMap = new BiHashMap<>();
    private final ResourceKey<T> resourceKey;
    private int nextId;

    public Registry(ResourceKey<T> resourceKey) {
        this.resourceKey = resourceKey;
    }

    public static <T, R extends T>
    R register(Registry<T> registry,
               int rawId,
               Identifier id,
               R entry) {
        return registry.set(rawId, id, entry);
    }

    public static <T, R extends T>
    R register(Registry<T> registry,
               int rawId,
               String id,
               R entry) {
        return registry.set(rawId, new Identifier(id), entry);
    }

    public static <T, R extends T>
    R register(Registry<T> registry,
               Identifier id,
               R entry) {
        return registry.add(id, entry);
    }

    public static <T, R extends T>
    R register(Registry<T> registry,
               String id,
               R entry) {
        return registry.add(new Identifier(id), entry);
    }

    public <R extends T> R set(int rawId, Identifier id, R entry) {
        entryMap.put(id, entry);
        rawIdMap.put(rawId, entry);
        nextId = rawId;
        return entry;
    }

    public <R extends T> R add(Identifier id, R entry) {
        if (entryMap.containsKey(id))
            throw new IllegalArgumentException("Already registered object. From "
                + resourceKey
                + ". Entry id: "
                + id);
        entryMap.put(id, entry);
        rawIdMap.put(nextId++, entry);
        return entry;
    }

    public T get(Identifier id) {
        return entryMap.get(id);
    }

    public T get(int rawId) {
        return rawIdMap.get(rawId);
    }

    public Identifier getId(T entry) {
        return entryMap.inverse().get(entry);
    }

    public int getRawId(T entry) {
        return rawIdMap.inverse().get(entry);
    }

    public ResourceKey<T> getResourceKey() {
        return resourceKey;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return entryMap.values().iterator();
    }
}
