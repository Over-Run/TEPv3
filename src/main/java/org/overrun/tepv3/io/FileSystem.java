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

package org.overrun.tepv3.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.function.BiFunction;

/**
 * <h2>The FileSystem class</h2>
 * The FileSystem class is a collection of methods to find the resources.
 *
 * @author squid233
 * @since 3.0.1
 */
public class FileSystem<T> {
    /**
     * The builtin resource filesystem.
     */
    public static final FileSystem<?> BUILTIN =
        new FileSystem<>((name, o) ->
            FileSystem.class.getClassLoader()
                .getResourceAsStream(name));
    /**
     * The ClassLoader filesystem.
     * <p>
     * Usage: {@code CLASSLOADER.findResource("Test.txt", Main.class.getClassLoader())}
     * </p>
     */
    public static final FileSystem<ClassLoader> CLASSLOADER =
        new FileSystem<>((name, o) ->
            o.getResourceAsStream(name));
    /**
     * The Class filesystem.
     * <p>
     * Usage: CLASS.findResource("Test.txt", Main.class)
     * </p>
     */
    public static final FileSystem<Class<?>> CLASS =
        new FileSystem<>((name, o) ->
            o.getClassLoader().getResourceAsStream(name));
    /**
     * The local filesystem.
     * <p>
     * Use it to find the local files.
     * </p>
     */
    public static final FileSystem<?> LOCAL =
        new FileSystem<>((name, o) ->
        {
            try {
                return new FileInputStream(name);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    private final BiFunction<String, T, InputStream> function;

    private FileSystem(BiFunction<String, T, InputStream> function) {
        this.function = function;
    }

    /**
     * Find a resource by name and (a) custom parameter(s).
     *
     * @param name  The resource name.
     * @param param The parameter.
     * @return The InputStream. You must explicitly close it.
     */
    public InputStream findResource(String name, T param) {
        return function.apply(name, param);
    }
}
