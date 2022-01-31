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
public class Timer {
    public static final long NS_PER_SECOND = 1_000_000_000L;
    public static final long MAX_NS_PER_UPDATE = 1_000_000_000L;
    /**
     * Max tick count per update
     */
    protected static final int MAX_TICKS_PER_UPDATE = 100;
    /**
     * Timer ticks per seconds.
     */
    public final double tps;
    /**
     * The last time update.
     */
    private long lastTime = System.nanoTime();
    /**
     * The tick count that should tick.
     */
    public int ticks;
    /**
     * The time since the last time.
     */
    public double delta;
    /**
     * The timer speed scale
     */
    public double timeScale = 1.0;
    /**
     * Frames per seconds
     */
    public double fps = 0.0;
    /**
     * The time since the last time.
     */
    public double passedTime = 0.0;

    public Timer(float tps) {
        this.tps = tps;
    }

    public void advanceTime() {
        var now = System.nanoTime();
        var passedNs = now - lastTime;
        lastTime = now;
        if (passedNs < 0L) {
            passedNs = 0L;
        }
        if (passedNs > MAX_NS_PER_UPDATE) {
            passedNs = MAX_NS_PER_UPDATE;
        }
        fps = (double) MAX_NS_PER_UPDATE / passedNs;
        passedTime += passedNs * timeScale * tps / (double) NS_PER_SECOND;
        ticks = (int) passedTime;
        if (ticks > MAX_TICKS_PER_UPDATE) {
            ticks = MAX_TICKS_PER_UPDATE;
        }
        passedTime -= ticks;
        delta = passedTime;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public double getTimeScale() {
        return timeScale;
    }

    public double getDelta() {
        return delta;
    }
}
