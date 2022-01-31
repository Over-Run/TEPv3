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

package org.overrun.tepv3.game.client;

import org.overrun.tepv3.TEPv3;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;

/**
 * @author squid233
 * @since 3.0.1
 */
public class RunArgs {
    public static final String D_WIDTH = "tepv3.game.width";
    public static final String D_HEIGHT = "tepv3.game.height";
    public static final String D_TITLE = "tepv3.game.title";
    public static final int INIT_WIDTH = getInt(D_WIDTH, 854);
    public static final int INIT_HEIGHT = getInt(D_HEIGHT, 480);
    public static final String INIT_TITLE = getProperty(D_TITLE, "The Experimental Project v" + TEPv3.VERSION_STR);

    private static int getInt(String k, int def) {
        var v = getProperty(k);
        return v != null ? parseInt(v) : def;
    }
}
