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

package org.overrun.tepv3.client.gl;

import org.overrun.tepv3.util.BuiltinResources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Shader {
    private final Type shaderType;
    private final String name;
    private int shaderID;

    protected Shader(Type shaderType, int shaderID, String name) {
        this.shaderType = shaderType;
        this.shaderID = shaderID;
        this.name = name;
    }

    public static Shader createFromResource(Type type,
                                            String name,
                                            InputStream stream,
                                            String namespace) throws IOException {
        int id = loadShader(type, name, stream, namespace);
        var shader = new Shader(type, id, name);
        type.getShaderCache().put(name, shader);
        return shader;
    }

    protected static int loadShader(Type type,
                                    String name,
                                    InputStream stream,
                                    String namespace) throws IOException {
        var str = BuiltinResources.getContent(stream, name);
        int id = glCreateShader(type.getGlType());
        glShaderSource(id, str);
        glCompileShader(id);
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IOException("Error compiling shader src from " + namespace + ": " + glGetShaderInfoLog(id).trim());
        }
        return id;
    }

    public void attachTo(IGLProgram program) {
        glAttachShader(program.getProgramId(), getShaderID());
    }

    public void free() {
        if (shaderID == -1)
            return;
        glDeleteShader(shaderID);
        shaderID = -1;
        shaderType.getShaderCache().remove(name);
    }

    public String getName() {
        return name;
    }

    protected int getShaderID() {
        return shaderID;
    }

    public enum Type {
        VERTEX("vertex", ".vert", GL_VERTEX_SHADER),
        FRAGMENT("fragment", ".frag", GL_FRAGMENT_SHADER);
        private final String name;
        private final String fileExtension;
        private final int glType;
        private final HashMap<String, Shader> shaderCache = new HashMap<>();

        Type(String name,
             String extension,
             int glType) {
            this.name = name;
            this.fileExtension = extension;
            this.glType = glType;
        }

        public String getName() {
            return name;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        int getGlType() {
            return glType;
        }

        public HashMap<String, Shader> getShaderCache() {
            return shaderCache;
        }
    }
}
