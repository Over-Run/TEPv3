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

import org.overrun.tepv3.client.Camera;
import org.overrun.tepv3.client.TEPv3Client;
import org.overrun.tepv3.world.World;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class PlayerEntity extends Entity {
    public final Camera camera = new Camera();//todo client

    public PlayerEntity(World world) {
        super(world);
        eyeHeight = 1.62;
    }

    @Override
    public void turn(double yaw, double pitch) {
        super.turn(yaw * TEPv3Client.SENSITIVITY, pitch * TEPv3Client.SENSITIVITY);
    }

    @Override
    public void tick() {
        super.tick();
        camera.prevPos.set(camera.position);
        var xa = 0.0;
        var za = 0.0;
        var wnd = client.getWindow();
        if (wnd.isKeyDown(GLFW_KEY_R))
            resetPos();
        if (wnd.isKeyDown(GLFW_KEY_W))
            --za;
        if (wnd.isKeyDown(GLFW_KEY_S))
            ++za;
        if (wnd.isKeyDown(GLFW_KEY_A))
            --xa;
        if (wnd.isKeyDown(GLFW_KEY_D))
            ++xa;
        if (wnd.isKeyDown(GLFW_KEY_SPACE) && (onGround || true))//todo
            velocity.y = 0.5;
        moveRelative(xa, za, onGround ? 0.1 : 0.02);
        velocity.y -= 0.08;
        move(velocity);
        velocity.mul(0.91, 0.98, 0.91);
        if (onGround)
            velocity.mul(0.7, 1, 0.7);
    }

    @Override
    public void move(double x, double y, double z) {
        super.move(x, y, z);
        camera.position.set(position.x, position.y + eyeHeight, position.z);
    }
}
