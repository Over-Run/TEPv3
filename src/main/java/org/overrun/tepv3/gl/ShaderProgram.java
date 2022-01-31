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

package org.overrun.tepv3.gl;

import org.joml.Matrix4fc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.overrun.tepv3.gl.VertexFormat.COLOR4F;
import static org.overrun.tepv3.gl.VertexFormat.VERTEX3F;
import static org.overrun.tepv3.util.BuiltinResources.getContent;

/**
 * @author squid233
 * @since 3.0.1
 */
public class ShaderProgram {
    private static final HashMap<VertexLayout, ShaderProgram> LAYOUT2PROGRAM =
        new HashMap<>();
    public static final ShaderProgram POS_COLOR =
        new Builder()
            .filename("position_color")
            .addVar("Position", VERTEX3F)
            .addVar("Color", COLOR4F)
            .build();
    /**
     * The programs to be initialized.
     */
    private static final ShaderProgram[] PROGRAMS = {
        POS_COLOR
    };
    private static final float[] MAT4_BUF = new float[16];
    private final VertexLayout layout;
    private final Map<String, ProgramVar> variables =
        new HashMap<>();
    private final Map<String, Integer> uniforms =
        new HashMap<>();
    private final String filename32;
    private String filename20;
    private final boolean gl20;
    private int handle;

    public static class Builder {
        private final Map<String, VertexFormat> formatMap =
            new HashMap<>();
        private String filename;
        private boolean fallback;

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder supportGL20() {
            fallback = true;
            return this;
        }

        public Builder addVar(String name, VertexFormat format) {
            formatMap.put(name, format);
            return this;
        }

        public ShaderProgram build() {
            var p = new ShaderProgram(formatMap.values(),
                filename + "32",
                fallback);
            if (fallback) {
                p.filename20 = filename + "20";
            }
            formatMap.forEach((name, format) ->
                p.variables.put(name, new ProgramVar(name, format)));
            return p;
        }
    }

    private ShaderProgram(Collection<VertexFormat> formats,
                          String filename32,
                          boolean gl20) {
        layout = new VertexLayout(formats);
        this.filename32 = filename32;
        this.gl20 = gl20;
        LAYOUT2PROGRAM.put(layout, this);
    }

    public void setMatrix4(String name, Matrix4fc mat) {
        glUniformMatrix4fv(uniforms.get(name), false, mat.get(MAT4_BUF));
    }

    public void use() {
        glUseProgram(handle);
    }

    public void noUsing() {
        glUseProgram(0);
    }

    public VertexLayout getLayout() {
        return layout;
    }

    public Map<String, ProgramVar> getVariables() {
        return variables;
    }

    public Map<String, Integer> getUniforms() {
        return uniforms;
    }

    private void createUniforms(String... names) {
        for (var name : names)
            uniforms.put(name, glGetUniformLocation(handle, name));
    }

    private void link(int vshId, int fshId) {
        glLinkProgram(handle);
        if (glGetProgrami(handle, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error linking GL program: " +
                glGetProgramInfoLog(handle));
        }
        if (glIsShader(vshId)) {
            glDetachShader(handle, vshId);
            glDeleteShader(vshId);
        }
        if (glIsShader(fshId)) {
            glDetachShader(handle, fshId);
            glDeleteShader(fshId);
        }
        glValidateProgram(handle);
        if (glGetProgrami(handle, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(handle));
        }
    }

    private int createShader(String src, int type) {
        int id = glCreateShader(type);
        if (id == 0) {
            throw new RuntimeException(
                "An error occurred creating the shader " +
                    type +
                    " object."
            );
        }
        glShaderSource(id, src);
        glCompileShader(id);
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling shader src: " +
                glGetShaderInfoLog(id));
        }
        glAttachShader(handle, id);
        return id;
    }

    /**
     * Get the {@link ShaderProgram} by the {@link VertexLayout}.
     *
     * @param layout The layout.
     * @return The program.
     */
    public static ShaderProgram getByLayout(VertexLayout layout) {
        return LAYOUT2PROGRAM.get(layout);
    }

    /**
     * Initialize all {@link ShaderProgram}s.
     */
    public static void initialize() {
        for (var program : PROGRAMS) {
            var id = program.handle = glCreateProgram();
            if (id == 0) {
                throw new RuntimeException("An error occurred creating the program object.");
            }
            int vsh, fsh;
            String prefix;
            if (program.gl20) {
                prefix = "_TEPv3/shaders/" + program.filename20;
                // TODO support gl20
            } else {
                prefix = "_TEPv3/shaders/" + program.filename32;
            }
            vsh = program.createShader(
                getContent(prefix + ".vert"),
                GL_VERTEX_SHADER);
            fsh = program.createShader(
                getContent(prefix + ".frag"),
                GL_FRAGMENT_SHADER);
            program.link(vsh, fsh);
            program.variables.forEach((name, programVar) ->
                programVar.location = glGetAttribLocation(program.handle, name));
            program.createUniforms("ProjMat", "ModelViewMat");
        }
    }

    public static void freeResources() {
        glUseProgram(0);
        for (var program : PROGRAMS) {
            glDeleteProgram(program.handle);
        }
    }
}
