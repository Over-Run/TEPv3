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

package org.overrun.tepv3.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <h2>The Event</h2>
 * The events are triggered on custom operating.
 * <h3>Creating Events</h3>
 * To create event, you should use the {@link EventFactory#create create}
 * method.
 * <p>
 * Here is the example:<br>
 * <pre><code>@FunctionalInterface
public interface PlayerMoveEvent {
    ListBackedEvent<PlayerMoveEvent> EVENT = create(events -> (x, y) -> {
        for (var event : events)
            event.onMoving(x, y);
    });

    void onMoving(double x, double y);
}</code></pre>
 * </p>
 *
 * @author squid233
 * @since 3.0.1
 */
public class ListBackedEvent<T> {
    private final Function<List<T>, T> factory;
    private final List<T> listeners = new ArrayList<>();

    public ListBackedEvent(Function<List<T>, T> factory) {
        this.factory = factory;
    }

    public void register(T event) {
        listeners.add(event);
    }

    public T onEvent() {
        return factory.apply(listeners);
    }
}
