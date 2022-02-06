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

/**
 * @author squid233
 * @since 3.0.1
 */
public class StringUtils {
    public static String abbreviateMiddle(String str, String middle, int length) {
        if (!isAnyEmpty(str, middle) && length < str.length() && length >= middle.length() + 2) {
            int targetSting = length - middle.length();
            int startOffset = targetSting / 2 + targetSting % 2;
            int endOffset = str.length() - targetSting / 2;
            return str.substring(0, startOffset) + middle + str.substring(endOffset);
        }
        return str;
    }

    public static boolean isAnyEmpty(CharSequence... css) {
        if (css == null || css.length == 0)
            return true;
        for (var cs : css) {
            if (cs.isEmpty())
                return true;
        }
        return false;
    }
}
