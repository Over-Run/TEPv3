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

package org.overrun.tepv3.model;

import org.joml.Vector3f;

/**
 * The builder to build the models.
 *
 * @author squid233
 * @since 3.0.1
 */
public class ModelsBuilder {
    /**
     * Build a triangle model with colors.
     *
     * @param p0 The 1st point
     * @param p1 The 2nd point
     * @param p2 The 3rd point
     * @param c0 The 1st color
     * @param c1 The 2nd color
     * @param c2 The 3rd color
     * @return The mesh may be {@link Mesh} or {@code Mesh20}
     */
    public static Mesh buildTrianglePC(final Vector3f p0,
                                       final Vector3f p1,
                                       final Vector3f p2,
                                       final Vector3f c0,
                                       final Vector3f c1,
                                       final Vector3f c2) {
        var builder = new Mesh.Builder();
        builder.color(c0.x, c0.y, c0.z).vertex(p0.x, p0.y, p0.z).next();
        builder.color(c1.x, c1.y, c1.z).vertex(p1.x, p1.y, p1.z).next();
        builder.color(c2.x, c2.y, c2.z).vertex(p2.x, p2.y, p2.z).next();
        return builder.build();
    }

    public static Mesh buildCubePC(final Vector3f p0,
                                   final Vector3f p1,
                                   final Vector3f c0,
                                   final Vector3f c1) {
        var builder = new Mesh.Builder().enableQuad();
        // -x
        builder.color(c0.x, c1.y, c0.z).vertex(p0.x, p1.y, p0.z).next();
        builder.color(c0.x, c0.y, c0.z).vertex(p0.x, p0.y, p0.z).next();
        builder.color(c0.x, c0.y, c1.z).vertex(p0.x, p0.y, p1.z).next();
        builder.color(c0.x, c1.y, c1.z).vertex(p0.x, p1.y, p1.z).next();
        // +x
        builder.color(c1.x, c1.y, c1.z).vertex(p1.x, p1.y, p1.z).next();
        builder.color(c1.x, c0.y, c1.z).vertex(p1.x, p0.y, p1.z).next();
        builder.color(c1.x, c0.y, c0.z).vertex(p1.x, p0.y, p0.z).next();
        builder.color(c1.x, c1.y, c0.z).vertex(p1.x, p1.y, p0.z).next();
        // -y
        builder.color(c0.x, c0.y, c1.z).vertex(p0.x, p0.y, p1.z).next();
        builder.color(c0.x, c0.y, c0.z).vertex(p0.x, p0.y, p0.z).next();
        builder.color(c1.x, c0.y, c0.z).vertex(p1.x, p0.y, p0.z).next();
        builder.color(c1.x, c0.y, c1.z).vertex(p1.x, p0.y, p1.z).next();
        // +y
        builder.color(c0.x, c1.y, c0.z).vertex(p0.x, p1.y, p0.z).next();
        builder.color(c0.x, c1.y, c1.z).vertex(p0.x, p1.y, p1.z).next();
        builder.color(c1.x, c1.y, c1.z).vertex(p1.x, p1.y, p1.z).next();
        builder.color(c1.x, c1.y, c0.z).vertex(p1.x, p1.y, p0.z).next();
        // -z
        builder.color(c1.x, c1.y, c0.z).vertex(p1.x, p1.y, p0.z).next();
        builder.color(c1.x, c0.y, c0.z).vertex(p1.x, p0.y, p0.z).next();
        builder.color(c0.x, c0.y, c0.z).vertex(p0.x, p0.y, p0.z).next();
        builder.color(c0.x, c1.y, c0.z).vertex(p0.x, p1.y, p0.z).next();
        // +z
        builder.color(c0.x, c1.y, c1.z).vertex(p0.x, p1.y, p1.z).next();
        builder.color(c0.x, c0.y, c1.z).vertex(p0.x, p0.y, p1.z).next();
        builder.color(c1.x, c0.y, c1.z).vertex(p1.x, p0.y, p1.z).next();
        builder.color(c1.x, c1.y, c1.z).vertex(p1.x, p1.y, p1.z).next();
        return builder.build();
    }
}
