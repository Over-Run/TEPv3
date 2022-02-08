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

package org.overrun.tepv3.world.entity;

import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.overrun.tepv3.client.TEPv3Client;
import org.overrun.tepv3.world.World;
import org.overrun.tepv3.client.phys.AABBox;

import static java.lang.Math.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Entity {
    protected final TEPv3Client client = TEPv3Client.getInstance();//todo split into client and server
    protected World world;
    public final Vector3d prevPos = new Vector3d();
    public final Vector3d position = new Vector3d();
    public final Vector3d velocity = new Vector3d();
    public final Vector2d rotation = new Vector2d();
    public AABBox box;
    public boolean onGround = false;
    public boolean isRemoved = false;
    protected double eyeHeight = 0.0;
    protected double bbWidth = 0.6;
    protected double bbHeight = 1.8;

    public Entity(World world) {
        this.world = world;
        resetPos();
    }

    protected void resetPos() {
        var x = random() * world.width;
        var y = world.height + 10;
        var z = random() * world.depth;
        setPos(x, y, z);
    }

    public void remove() {
        isRemoved = true;
    }

    protected void setSize(double w, double h) {
        bbWidth = w;
        bbHeight = h;
    }

    protected void setPos(double x, double y, double z) {
        position.set(x, y, z);
        var w = bbWidth / 2.0;
        box = new AABBox(x - w,
            y,
            z - w,
            x + w,
            y + bbHeight,
            z + w);
    }

    public void turn(double yaw, double pitch) {
        rotation.add(pitch, yaw);
        if (rotation.x < -90.0)
            rotation.x = -90.0;
        else if (rotation.x > 90.0)
            rotation.x = 90.0;
        if (rotation.y > 360.0)
            rotation.y = 0.0;
        else if (rotation.y < 0.0)
            rotation.y = 360.0;
    }

    public void tick() {
        prevPos.set(position);
    }

    public void move(double x, double y, double z) {
        var xOrg = x;
        var yOrg = y;
        var zOrg = z;
        var boxes = world.getCubes(box.expand(x, y, z, new AABBox()));
        for (var b : boxes)
            y = b.clipYCollide(box, y);
        box.move(0, y, 0);
        for (var b : boxes)
            x = b.clipXCollide(box, x);
        box.move(x, 0, 0);
        for (var b : boxes)
            z = b.clipZCollide(box, z);
        box.move(0, 0, z);
        onGround = yOrg != y && yOrg < 0.0;
        if (xOrg != x)
            velocity.x = 0.0;
        if (yOrg != y)
            velocity.y = 0.0;
        if (zOrg != z)
            velocity.z = 0.0;
        position.set((box.min.x + box.max.x) / 2.0,
            box.min.y,
            (box.min.z + box.max.z) / 2.0);
    }

    public void move(Vector3dc vec) {
        move(vec.x(), vec.y(), vec.z());
    }

    public void moveRelative(double x, double z, double speed) {
        var dist = fma(x, x, z * z);
        if (dist >= 0.01) {
            dist = speed / sqrt(dist);
            x *= dist;
            z *= dist;
            var rad = toRadians(rotation.y);
            var sin = sin(rad);
            var cos = cos(rad);
            velocity.add(x * cos - z * sin,
                0,
                z * cos + x * sin);
        }
    }

    public boolean isLit() {
        return world.isLit((int) position.x, (int) position.y, (int) position.z);
    }
}
