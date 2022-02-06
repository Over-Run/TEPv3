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

package org.overrun.tepv3.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * @author squid233
 * @since 3.0.1
 */
public class BuiltinResources {
    private static final ClassLoader LOADER =
        BuiltinResources.class.getClassLoader();

    public static String getContent(InputStream stream, String name) {
        try (var isr = new InputStreamReader(requireNonNull(stream), UTF_8);
             var br = new BufferedReader(isr)) {
            var ln = br.readLine();
            var sb = new StringBuilder(ln);
            while ((ln = br.readLine()) != null) {
                sb.append("\n").append(ln);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Can't find resource {" + name + "}", e);
        }
    }

    public static String getContent(String filename) {
        try (var is = LOADER.getResourceAsStream(filename)) {
            return getContent(is, filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
