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

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.overrun.tepv3.client.gl.GLUniform;
import org.overrun.tepv3.client.gl.IGLProgram;
import org.overrun.tepv3.client.gl.Shader;
import org.overrun.tepv3.client.gl.Uniform;
import org.overrun.tepv3.io.FileSystem;
import org.overrun.tepv3.util.BuiltinResources;
import org.overrun.tepv3.util.Identifier;

import java.io.IOException;
import java.util.*;

import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.overrun.tepv3.client.gl.GLProgramMgr.*;
import static org.overrun.tepv3.util.JsonHelper.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class Program implements IGLProgram, AutoCloseable {
    private static final String CORE_DIRECTORY = "shaders/core/";
    private static final Uniform DEFAULT_UNIFORM = new Uniform();
    private static Program activeProgram;
    private static int activeProgramId = -1;
    private final Map<String, Object> samplers = new HashMap<>();
    private final List<String> samplerNames = new ArrayList<>();
    private final IntArrayList loadedSamplerIds = new IntArrayList();
    private final Map<String, GLUniform> loadedUniforms = new HashMap<>();
    private final List<GLUniform> uniforms = new ArrayList<>();
    private final IntArrayList loadedUniformIds = new IntArrayList();
    private final int programId;
    private final String name;
    private boolean isDirty;
    private final IntArrayList loadedAttributeIds;
    private final List<String> attributeNames;
    private final Shader vertexShader;
    private final Shader fragmentShader;
    private final VertexFormat format;
    public final GLUniform projMat;
    public final GLUniform modelViewMat;
    public final GLUniform colorModulator;

    public Program(String name, VertexFormat format) {
        this.name = name;
        this.format = format;
        var id = new Identifier(CORE_DIRECTORY + name + ".json");
        try {
            var json = BuiltinResources.getContent("assets/" + id.getNamespace() + "/" + id.getPath());
            var obj = deserialize(json);

            // Load samplers
            var samplers = getArray(obj, "samplers", null);
            if (samplers != null) {
                for (var element : samplers) {
                    readSampler(element);
                }
            }

            // Load attributes
            var attributes = getArray(obj, "attributes", null);
            if (attributes != null) {
                loadedAttributeIds = new IntArrayList(attributes.size());
                attributeNames = new ArrayList<>(attributes.size());
                for (var element : attributes) {
                    attributeNames.add(asString(element, "attribute"));
                }
            } else {
                loadedAttributeIds = null;
                attributeNames = null;
            }

            // Load uniforms
            var uniforms = getArray(obj, "uniforms", null);
            if (uniforms != null) {
                for (var element : uniforms) {
                    addUniform(element);
                }
            }

            vertexShader = loadShader(Shader.Type.VERTEX, getString(obj, "vertex"));
            fragmentShader = loadShader(Shader.Type.FRAGMENT, getString(obj, "fragment"));
            programId = createProgram();
            if (attributeNames != null) {
                int i = 0;
                for (var attrib : format.getShaderAttributes()) {
                    GLUniform.bindAttribLocation(programId, i, attrib);
                    ++i;
                }
            }
            linkProgram(this);
            loadRefs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        markUniformsDirty();
        projMat = getUniform("ProjMat");
        modelViewMat = getUniform("ModelViewMat");
        colorModulator = getUniform("ColorModulator");
    }

    private static Shader loadShader(Shader.Type type, String name) throws IOException {
        var shader = type.getShaderCache().get(name);
        if (shader == null) {
            var rawPath = CORE_DIRECTORY + name + type.getFileExtension();
            var id = new Identifier(rawPath);
            var path = "assets/" + id.getNamespace() + "/" + id.getPath();
            try (var is = FileSystem.CLASS.findResource(path, Program.class)) {
                return Shader.createFromResource(type, name, is, id.getNamespace());
            }
        } else
            return shader;
    }

    public void bind() {
        isDirty = false;
        activeProgram = this;
        if (programId != activeProgramId) {
            useProgram(programId);
            activeProgramId = programId;
        }
        int lastUnit = RenderSystem.getActiveTexture();
        for (int i = 0; i < loadedSamplerIds.size(); i++) {
            var name = samplerNames.get(i);
            if (samplers.get(name) == null) continue;
            int loc = GLUniform.getLocation(programId, name);
            glUniform1i(loc, i);
            RenderSystem.activeTexture(i);
            RenderSystem.enableTexture();
            var sampler = samplers.get(name);
            int tex = -1;
            if (sampler instanceof Integer) {
                tex = (Integer) sampler;
            }
            if (tex == -1) continue;
            RenderSystem.bindTexture(tex);
        }
        RenderSystem.activeTexture(lastUnit);
        for (var uniform : uniforms) {
            uniform.upload();
        }
    }

    public void unbind() {
        useProgram(0);
        activeProgramId = -1;
        activeProgram = null;
        int lastUnit = RenderSystem.getActiveTexture();
        for (int i = 0; i < loadedSamplerIds.size(); i++) {
            if (samplers.get(samplerNames.get(i)) == null) continue;
            RenderSystem.activeTexture(i);
            RenderSystem.bindTexture(0);
        }
        RenderSystem.activeTexture(lastUnit);
    }

    public GLUniform getUniform(String name) {
        return loadedUniforms.get(name);
    }

    public Uniform getUniformOrDefault(String name) {
        var u = getUniform(name);
        return u == null ? DEFAULT_UNIFORM : u;
    }

    private void loadRefs() {
        var samplerList = new IntArrayList();
        for (int i = 0; i < samplerNames.size(); i++) {
            var name = samplerNames.get(i);
            int loc = GLUniform.getLocation(programId, name);
            if (loc == -1) {
                System.err.println("Shader " + name + " could not find sampler named " + name + " in the specified shader program.");//todo use logger
                samplers.remove(name);
                samplerList.add(i);
                continue;
            }
            loadedSamplerIds.add(loc);
        }
        for (int i = samplerList.size() - 1; i >= 0; i--) {
            samplerNames.remove(samplerList.getInt(i));
        }
        for (var uniform : uniforms) {
            var name = uniform.getName();
            int loc = GLUniform.getLocation(programId, name);
            if (loc == -1) {
                System.err.println("Shader " + name + " could not find sampler named " + name + " in the specified shader program.");//todo use logger
                continue;
            }
            loadedUniformIds.add(loc);
            uniform.setLocation(loc);
            loadedUniforms.put(name, uniform);
        }
    }

    private void readSampler(JsonElement json) {
        var obj = asObject(json, "sampler");
        var name = getString(obj, "name");
        if (!hasString(obj, "file")) {
            samplers.put(name, null);
            samplerNames.add(name);
            return;
        }
        samplerNames.add(name);
    }

    public void addSampler(String name, Object sampler) {
        samplers.put(name, sampler);
        markUniformsDirty();
    }

    private void addUniform(JsonElement json) {
        var obj = asObject(json, "uniform");
        var name = getString(obj, "name");
        int type = GLUniform.getTypeIndex(getString(obj, "type"));
        int count = getInt(obj, "count");
        float[] floats = new float[Math.max(count, 16)];
        var values = getArray(obj, "values");
        if (values.size() != count && values.size() > 1)
            throw new RuntimeException("Invalid amount of values specified (expected " + count + ", found " + values.size() + ")");
        int i = 0;
        for (var element : values) {
            floats[i] = asFloat(element, "value");
            ++i;
        }
        if (count > 1 && values.size() == 1) {
            while (i < count) {
                floats[i] = floats[0];
                ++i;
            }
        }
        int typeAdd =
            // check if count in range 2 to 4
            count > 1 && count <= 4
                // check if type is not matrix
                && type < GLUniform.TYPE_MAT2
                ? count - 1
                // Don't add
                : 0;
        var uniform = new GLUniform(name, type + typeAdd, count, this);
        if (type <= GLUniform.TYPE_INT4)
            uniform.setForDataType((int) floats[0], (int) floats[1], (int) floats[2], (int) floats[3]);
        else if (type <= GLUniform.TYPE_FLOAT4)
            uniform.setForDataType(floats[0], floats[1], floats[2], floats[3]);
        else
            uniform.set(Arrays.copyOfRange(floats, 0, count));
        uniforms.add(uniform);
    }

    public VertexFormat getFormat() {
        return format;
    }

    public String getName() {
        return name;
    }

    @Override
    public void markUniformsDirty() {
        isDirty = true;
    }

    @Override
    public Shader getVertexShader() {
        return vertexShader;
    }

    @Override
    public Shader getFragmentShader() {
        return fragmentShader;
    }

    @Override
    public int getProgramId() {
        return programId;
    }

    @Override
    public void attachReferencedShaders() {
        fragmentShader.attachTo(this);
        vertexShader.attachTo(this);
    }

    @Override
    public void close() {
        for (var uniform : uniforms)
            uniform.close();
        deleteProgram(this);
    }
}
