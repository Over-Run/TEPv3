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

package org.overrun.tepv3.client;

import org.joml.Vector3d;
import org.lwjgl.opengl.GLUtil;
import org.overrun.tepv3.client.tex.SpriteAtlasTextures;
import org.overrun.tepv3.client.world.render.WorldRenderer;
import org.overrun.tepv3.gl.RenderSystem;
import org.overrun.tepv3.gl.ShaderProgram;
import org.overrun.tepv3.scene.GLFWScene;
import org.overrun.tepv3.util.registry.Registries;
import org.overrun.tepv3.world.World;
import org.overrun.tepv3.world.entity.PlayerEntity;

import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.tepv3.client.RunArgs.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class TEPv3Game extends GLFWScene {
    public static final double SENSITIVITY = 0.15;
    private static TEPv3Game instance;
    private boolean isFirstMouse = true;
    private boolean isPausing = true;
    public World world;
    public WorldRenderer worldRenderer;
    public PlayerEntity player;// todo player into world
    public Camera attachCamera;

    public TEPv3Game() {
        super(INIT_WIDTH, INIT_HEIGHT, INIT_TITLE);
    }

    @Override
    public void onKey(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_ESCAPE) {
                isPausing = !isPausing;
                timer.timeScale = isPausing ? 0 : 1;
                isFirstMouse = true;
                getWindow().setGrabbed(!isPausing);
                getWindow().setCursorPos(mouseX = getWidth() / 2, mouseY = getHeight() / 2);
            }
        }
    }

    @Override
    public void onCursorPos(int x, int y, int deltaX, int deltaY) {
        if (isFirstMouse) {
            x = mouseX = getWidth() / 2;
            y = mouseY = getHeight() / 2;
            deltaX = deltaMX = 0;
            deltaY = deltaMY = 0;
            isFirstMouse = false;
        }
        if (!isPausing) {
            player.turn(deltaX, deltaY);
        }
    }

    @Override
    public void init() {
        final var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null)
            getWindow().setPos((vidMode.width() - viewport.getWidth()) / 2,
                (vidMode.height() - viewport.getHeight()) / 2);
        getWindow().setRawMouseMotion(true);
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL_LEQUAL);
        if (false) //#ifdef _DEBUG
            GLUtil.setupDebugMessageCallback(System.err);

        Registries.load();

        timer.timeScale = 0;

        world = new World(System.nanoTime(), 256, 64, 256);
        worldRenderer = new WorldRenderer(world);
        player = new PlayerEntity(world);
        attachCamera = player.camera;

        SpriteAtlasTextures.generateAtlases();
    }

    @Override
    public void tick() {
        player.tick();
    }

    private void moveCameraToPlayer(double delta) {
        var mv = RenderSystem.getModelView().translation(0.0f,
                0.0f,
                -0.3f)
            .rotateX((float) toRadians(player.rotation.x))
            .rotateY((float) toRadians(player.rotation.y));
        var lPos = attachCamera.prevPos.lerp(attachCamera.position,
            delta,
            new Vector3d());
        mv.translate((float) -lPos.x,
            (float) -lPos.y,
            (float) -lPos.z);
    }

    private void setupCamera(double delta) {
        RenderSystem.getProjection().setPerspective(
            (float) toRadians(90.0),
            getAspectRatiof(),
            0.05f,
            1000.0f
        );
        moveCameraToPlayer(delta);
    }

    @Override
    public void render(double delta) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        setupCamera(delta);
        RenderSystem.enableCullFace();
        RenderSystem.useShader(ShaderProgram.POSITION_COLOR_TEX);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        var frustum = Frustum.getFrustum();
        worldRenderer.updateDirtyChunks(player);
        worldRenderer.render(0);
        //todo setup fog and light
        worldRenderer.render(1);
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onExiting() {
        worldRenderer.free();
    }

    public static TEPv3Game getInstance() {
        if (instance == null)
            instance = new TEPv3Game();
        return instance;
    }
}
