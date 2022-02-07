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

package org.overrun.tepv3.world;

import org.joml.SimplexNoise;

/**
 * @author squid233
 * @since 3.0.1
 */
public class SimplexHeightmap {
    private static float sumOctave(int numIterators,
                                   float x,
                                   float y,
                                   float z,
                                   float persistence,
                                   float scale,
                                   float low,
                                   float high) {
        float maxAmp = 0, amp = 1;
        float freq = scale;
        float noise = 0;
        for (int i = 0; i < numIterators; i++) {
            noise += SimplexNoise.noise(x * freq, y, z * freq) * amp;
            maxAmp += amp;
            amp *= persistence;
            freq *= 2;
        }
        noise /= maxAmp;
        noise = noise * (high - low) / 2.0f + (high + low) / 2.0f;
        return noise;
    }

    public static int[] generate(long seed,
                                 int w,
                                 int h,
                                 int d) {
        final var arr = new int[w * d];
        final var invY = 1.0f / seed;
        final var scale = 2.0f / (w + d);
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                arr[x + z * w] = (int) sumOctave(16, x, invY, z, .5f, scale, 0, h);
            }
        }
        return arr;
    }
}
