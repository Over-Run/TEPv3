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

import org.overrun.tepv3.util.Identifier;

import java.util.function.Predicate;

/**
 * The resource types.
 * <p>
 * Currently available types are in the code.
 * </p>
 *
 * @author squid233
 * @since 3.0.1
 */
public enum ResourceType {
    MODELS("models/", s -> s.endsWith(".json")),
    SHADERS("shaders/", s -> s.endsWith(".json")),
    TEXTURES("textures/", s -> s.endsWith(".png"));

    private final String directory;
    private final Predicate<String> checker;

    ResourceType(String directory,
                 Predicate<String> checker) {
        this.directory = directory;
        this.checker = checker;
    }

    /**
     * Get the directory.
     *
     * @return The directory ends with "/".
     */
    public String getDirectory() {
        return directory;
    }

    public boolean isSameAs(String s) {
        return s.contains(getDirectory()) || checker.test(s);
    }

    public boolean isSameAs(Identifier id) {
        return isSameAs(id.getPath());
    }
}
