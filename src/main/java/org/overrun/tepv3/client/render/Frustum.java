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

package org.overrun.tepv3.client.render;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.overrun.tepv3.client.phys.AABBox;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Frustum {
    private static final Frustum frustum = new Frustum();
    private final Matrix4f clipMatrix = new Matrix4f();
    private final FrustumIntersection intersection = new FrustumIntersection();

    public static Frustum getFrustum() {
        frustum.calculateFrustum();
        return frustum;
    }

    private void calculateFrustum() {
        intersection.set(clipMatrix.set(RenderSystem.getProjection()).mul(RenderSystem.getModelView()));
    }

    public boolean testPoint(float x, float y, float z) {
        return intersection.testPoint(x, y, z);
    }

    public boolean testSphere(float x, float y, float z, float radius) {
        return intersection.testSphere(x, y, z, radius);
    }

    public boolean testAab(float x1, float y1, float z1, float x2, float y2, float z2) {
        return intersection.testAab(x1, y1, z1, x2, y2, z2);
    }

    public boolean testAab(AABBox box) {
        return intersection.testAab((float) box.min.x,
            (float) box.min.y,
            (float) box.min.z,
            (float) box.max.x,
            (float) box.max.y,
            (float) box.max.z);
    }
}
