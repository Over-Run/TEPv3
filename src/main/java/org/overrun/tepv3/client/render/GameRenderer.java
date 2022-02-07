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

import org.jetbrains.annotations.Nullable;
import org.overrun.tepv3.client.TEPv3Client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 3.0.1
 */
public class GameRenderer implements AutoCloseable {
    private final TEPv3Client client;
    private final Map<String, Program> programs = new HashMap<>();
    public Program blitScreenProgram;
    //@Nullable
    //private static Program positionProgram;
    @Nullable
    private static Program positionColorProgram;
    @Nullable
    private static Program positionColorTexProgram;
    @Nullable
    private static Program positionTexProgram;

    public GameRenderer(TEPv3Client client) {
        this.client = client;
    }

    public void preloadPrograms() {
        if (blitScreenProgram != null)
            throw new RuntimeException("Blit shader already preloaded");
        blitScreenProgram = new Program("blit_screen", VertexFormats.BLIT_SCREEN);
        //positionProgram = loadProgram("position", VertexFormats.POSITION);
        positionColorProgram = loadProgram("position_color", VertexFormats.POSITION_COLOR);
        positionColorTexProgram = loadProgram("position_color_tex", VertexFormats.POSITION_COLOR_TEXTURE);
        positionTexProgram = loadProgram("position_tex", VertexFormats.POSITION_TEXTURE);
    }

    private Program loadProgram(String name, VertexFormat format) {
        try {
            var program = new Program(name, format);
            programs.put(name, program);
            return program;
        } catch (Exception e) {
            throw new IllegalStateException("could not preload shader " + name, e);
        }
    }

    private void clearPrograms() {
        programs.values().forEach(Program::close);
        programs.clear();
    }

    @Nullable
    public Program getProgram(@Nullable String name) {
        if (name == null)
            return null;
        return programs.get(name);
    }

    @Override
    public void close() {
        clearPrograms();
        if (blitScreenProgram != null)
            blitScreenProgram.close();
    }

    //@Nullable
    //public static Program getPositionProgram() {
    //    return positionProgram;
    //}

    @Nullable
    public static Program getPositionColorProgram() {
        return positionColorProgram;
    }

    @Nullable
    public static Program getPositionColorTexProgram() {
        return positionColorTexProgram;
    }

    @Nullable
    public static Program getPositionTexProgram() {
        return positionTexProgram;
    }
}
