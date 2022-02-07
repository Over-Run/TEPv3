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
import org.overrun.tepv3.client.gl.GLBlendFunc;
import org.overrun.tepv3.client.gl.GLBlendState;
import org.overrun.tepv3.client.gl.GLDepthFunc;
import org.overrun.tepv3.client.model.BlockModelManager;
import org.overrun.tepv3.client.render.Frustum;
import org.overrun.tepv3.client.render.GameRenderer;
import org.overrun.tepv3.client.render.RenderSystem;
import org.overrun.tepv3.client.res.DefaultResourcePack;
import org.overrun.tepv3.client.tex.SpriteAtlasTextures;
import org.overrun.tepv3.client.world.render.WorldRenderer;
import org.overrun.tepv3.client.model.Mesh;
import org.overrun.tepv3.scene.GLFWScene;
import org.overrun.tepv3.util.registry.Registries;
import org.overrun.tepv3.world.World;
import org.overrun.tepv3.world.entity.PlayerEntity;

import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.Configuration.*;
import static org.overrun.tepv3.client.RunArgs.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class TEPv3Client extends GLFWScene {
    private static final boolean _DEBUG = false; // Change it on production
    private static final boolean ENABLE_MULTI_SAMPLE = false;
    public static final double SENSITIVITY = 0.15;
    private static TEPv3Client instance;
    private boolean isFirstMouse = true;
    private boolean isPausing = true;
    public GameRenderer gameRenderer;
    public World world;
    public WorldRenderer worldRenderer;
    public PlayerEntity player;// todo player into world
    public Camera attachCamera;
    public DefaultResourcePack defaultResourcePack;
    public BlockModelManager blockModelManager;
    @Deprecated(since = "3.0.1", forRemoval = true)
    public Mesh crossHair;

    public TEPv3Client() {
        super(INIT_WIDTH, INIT_HEIGHT, INIT_TITLE);
    }

    @Override
    public void onStarting() {
        if (_DEBUG) { //#ifdef _DEBUG
            DEBUG.set(true);
            DEBUG_LOADER.set(true);
            DEBUG_MEMORY_ALLOCATOR.set(true);
            DEBUG_STACK.set(true);
            DEBUG_FUNCTIONS.set(true);
        }
    }

    @Override
    public void setWindowHints() {
        if (_DEBUG) { //#ifdef _DEBUG
            glfwWindowHint(GLFW_CONTEXT_DEBUG, GLFW_TRUE);
        }
        if (ENABLE_MULTI_SAMPLE) {
            glfwWindowHint(GLFW_SAMPLES, 2);
        }
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
        if (_DEBUG) { //#ifdef _DEBUG
            GLUtil.setupDebugMessageCallback(System.err);
        }

        final var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null)
            getWindow().setPos((vidMode.width() - viewport.getWidth()) / 2,
                (vidMode.height() - viewport.getHeight()) / 2);
        getWindow().setRawMouseMotion(true);
        RenderSystem.setClearColor(0.4f, 0.6f, 0.9f, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GLDepthFunc.LEQUAL);
        if (ENABLE_MULTI_SAMPLE) {
            RenderSystem.enableMultiSample();
        }

        Registries.load();

        timer.timeScale = 0;

        gameRenderer = new GameRenderer(this);
        gameRenderer.preloadPrograms();
        world = new World(System.nanoTime(), 256, 64, 256);
        worldRenderer = new WorldRenderer(world);
        player = new PlayerEntity(world);
        attachCamera = player.camera;
        var builder = new Mesh.Builder().enableQuad();
        builder.color(1, 1, 1, 0.5f).vertex(2, -8, 0).next();
        builder.color(1, 1, 1, 0.5f).vertex(0, -8, 0).next();
        builder.color(1, 1, 1, 0.5f).vertex(0, 10, 0).next();
        builder.color(1, 1, 1, 0.5f).vertex(2, 10, 0).next();
        builder.color(1, 1, 1, 0.5f).vertex(10, 0, 0).next();
        builder.color(1, 1, 1, 0.5f).vertex(-8, 0, 0).next();
        builder.color(1, 1, 1, 0.5f).vertex(-8, 2, 0).next();
        builder.color(1, 1, 1, 0.5f).vertex(10, 2, 0).next();
        crossHair = builder.build();

        defaultResourcePack = new DefaultResourcePack();
        blockModelManager = new BlockModelManager(this);
        blockModelManager.loadModels();

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
        RenderSystem.clearColorBuf();
        RenderSystem.clearDepthBuf();
        RenderSystem.clear();
        setupCamera(delta);
        RenderSystem.enableCullFace();
        RenderSystem.setProgramColor(1, 1, 1, 1);
        var frustum = Frustum.getFrustum();
        worldRenderer.updateDirtyChunks(player);
        worldRenderer.render(0);
        //todo setup fog and light
        worldRenderer.render(1);

        drawGui();
    }

    private void drawGui() {
        RenderSystem.clearDepthBuf();
        RenderSystem.clear();
        RenderSystem.getProjection().setOrtho(0, viewport.getWidth(), viewport.getHeight(), 0, -1, 1);
        RenderSystem.getModelView().translation(viewport.getWidth() / 2.0f,
            viewport.getHeight() / 2.0f,
            0.0f);
        RenderSystem.setProgram(GameRenderer.getPositionColorProgram());
        RenderSystem.blendFuncSeparate(GLBlendFunc.ONE_MINUS_DST_COLOR,
            GLBlendFunc.ONE_MINUS_SRC_COLOR,
            GLBlendFunc.ONE,
            GLBlendFunc.ZERO);
        crossHair.render();
        GLBlendState.activeBlendState = null;
    }

    @Override
    public void resize(int width, int height) {
        RenderSystem.setViewport(0, 0, width, height);
    }

    @Override
    public void onExiting() {
        gameRenderer.close();
        worldRenderer.free();
    }

    public static TEPv3Client getInstance() {
        if (instance == null)
            instance = new TEPv3Client();
        return instance;
    }
}
