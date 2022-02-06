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

import org.joml.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class GLUniform extends Uniform implements AutoCloseable {
    public static final int TYPE_INT1 = 0;
    public static final int TYPE_INT2 = 1;
    public static final int TYPE_INT3 = 2;
    public static final int TYPE_INT4 = 3;
    public static final int TYPE_FLOAT1 = 4;
    public static final int TYPE_FLOAT2 = 5;
    public static final int TYPE_FLOAT3 = 6;
    public static final int TYPE_FLOAT4 = 7;
    public static final int TYPE_MAT2 = 8;
    public static final int TYPE_MAT3 = 9;
    public static final int TYPE_MAT4 = 10;
    private int location;
    private final int count;
    private final int dataType;
    private final IntBuffer intData;
    private final FloatBuffer floatData;
    private final String name;
    private boolean isDirty;
    private final IGLProgram program;

    public GLUniform(String name, int dataType, int count, IGLProgram program) {
        this.name = name;
        this.count = count;
        this.dataType = dataType;
        this.program = program;
        if (dataType <= TYPE_INT4) {
            intData = memAllocInt(count);
            floatData = null;
        } else {
            intData = null;
            floatData = memAllocFloat(count);
        }
        location = -1;
        markDirty();
    }

    public static int getLocation(int program, CharSequence name) {
        return glGetUniformLocation(program, name);
    }

    public static int getAttribLocation(int program, CharSequence name) {
        return glGetAttribLocation(program, name);
    }

    public static void bindAttribLocation(int program, int index, CharSequence name) {
        glBindAttribLocation(program, index, name);
    }

    public static int getTypeIndex(String typeName) {
        if ("int".equals(typeName))
            return TYPE_INT1;
        if ("float".equals(typeName))
            return TYPE_FLOAT1;
        if (typeName.startsWith("mat")) {
            if (typeName.endsWith("2"))
                return TYPE_MAT2;
            if (typeName.endsWith("3"))
                return TYPE_MAT3;
            if (typeName.endsWith("4"))
                return TYPE_MAT4;
        }
        return -1;
    }

    private void markDirty() {
        isDirty = true;
        if (program != null)
            program.markUniformsDirty();
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    @Override
    public final void set(float value) {
        floatData.position(0).put(0, value);
        markDirty();
    }

    @Override
    public final void set(float value1, float value2) {
        floatData.position(0).put(0, value1).put(1, value2);
        markDirty();
    }

    @Override
    public final void set(int index, float value) {
        floatData.position(0).put(index, value);
        markDirty();
    }

    @Override
    public final void set(float value1, float value2, float value3) {
        floatData.position(0).put(0, value1).put(1, value2).put(2, value3);
        markDirty();
    }

    @Override
    public final void set(Vector3fc vec) {
        set(vec.x(), vec.y(), vec.z());
    }

    @Override
    public final void set(float value1, float value2, float value3, float value4) {
        floatData.position(0).put(0, value1).put(1, value2).put(2, value3).put(3, value4);
        markDirty();
    }

    @Override
    public final void set(Vector4fc vec) {
        set(vec.x(), vec.y(), vec.z(), vec.w());
    }

    @Override
    public final void set(float value1, float value2,
                          float value3, float value4,
                          float value5, float value6) {
        floatData.position(0).put(0, value1).put(1, value2)
            .put(2, value3).put(3, value4)
            .put(4, value5).put(5, value6);
        markDirty();
    }

    @Override
    public final void set(float value1, float value2,
                          float value3, float value4,
                          float value5, float value6,
                          float value7, float value8) {
        floatData.position(0).put(0, value1).put(1, value2)
            .put(2, value3).put(3, value4)
            .put(4, value5).put(5, value6)
            .put(6, value7).put(7, value8);
        markDirty();
    }

    @Override
    public final void set(float value1, float value2, float value3,
                          float value4, float value5, float value6,
                          float value7, float value8, float value9) {
        floatData.position(0).put(0, value1).put(1, value2).put(2, value3)
            .put(3, value4).put(4, value5).put(5, value6)
            .put(6, value7).put(7, value8).put(8, value9);
        markDirty();
    }

    @Override
    public final void set(float value1, float value2, float value3,
                          float value4, float value5, float value6,
                          float value7, float value8, float value9,
                          float value10, float value11, float value12) {
        floatData.position(0).put(0, value1).put(1, value2).put(2, value3)
            .put(3, value4).put(4, value5).put(5, value6)
            .put(6, value7).put(7, value8).put(8, value9)
            .put(9, value10).put(10, value11).put(11, value12);
        markDirty();
    }

    @Override
    public final void set(float value1, float value2, float value3, float value4,
                          float value5, float value6, float value7, float value8,
                          float value9, float value10, float value11, float value12,
                          float value13, float value14, float value15, float value16) {
        floatData.position(0).put(0, value1).put(1, value2).put(2, value3).put(3, value4)
            .put(4, value5).put(5, value6).put(6, value7).put(7, value8)
            .put(8, value9).put(9, value10).put(10, value11).put(11, value12)
            .put(12, value13).put(13, value14).put(14, value15).put(15, value16);
        markDirty();
    }

    @Override
    public final void set(float[] values) {
        if (values.length < count) {
            System.err.println("Argument values length is too small! Excepted " + count + ", got " + values.length);//todo use logger
            return;
        }
        floatData.position(0).put(values).position(0);
        markDirty();
    }

    @Override
    public final void set(Matrix4fc mat) {
        mat.get(floatData.position(0));
        markDirty();
    }

    @Override
    public final void set(Matrix3fc mat) {
        mat.get(floatData.position(0));
        markDirty();
    }

    @Override
    public final void setForDataType(float value1, float value2, float value3, float value4) {
        floatData.position(0);
        if (dataType >= TYPE_FLOAT1)
            floatData.put(0, value1);
        if (dataType >= TYPE_FLOAT2)
            floatData.put(1, value2);
        if (dataType >= TYPE_FLOAT3)
            floatData.put(2, value3);
        if (dataType >= TYPE_FLOAT4)
            floatData.put(3, value4);
        markDirty();
    }

    @Override
    public final void set(int value) {
        intData.position(0).put(0, value);
        markDirty();
    }

    @Override
    public final void set(int value1, int value2) {
        intData.position(0).put(0, value1).put(1, value2);
        markDirty();
    }

    @Override
    public final void set(int value1, int value2, int value3) {
        intData.position(0).put(0, value1).put(1, value2).put(2, value3);
        markDirty();
    }

    @Override
    public final void set(int value1, int value2, int value3, int value4) {
        intData.position(0).put(0, value1).put(1, value2).put(2, value3).put(3, value4);
        markDirty();
    }

    @Override
    public final void setForDataType(int value1, int value2, int value3, int value4) {
        intData.position(0);
        if (dataType >= TYPE_INT1)
            intData.put(0, value1);
        if (dataType >= TYPE_INT2)
            intData.put(1, value2);
        if (dataType >= TYPE_INT3)
            intData.put(2, value3);
        if (dataType >= TYPE_INT4)
            intData.put(3, value4);
        markDirty();
    }

    public void upload() {
        if (!isDirty) {
        }
        isDirty = false;
        if (dataType <= TYPE_INT4)
            uploadInts();
        else if (dataType <= TYPE_FLOAT4)
            uploadFloats();
        else if (dataType <= TYPE_MAT4)
            uploadMatrix();
        else
            System.err.println("dataType is invalid. Ignoring in GLUniform.upload.");//todo use logger
    }

    private void uploadInts() {
        intData.rewind();
        switch (dataType) {
            case TYPE_INT1 -> glUniform1iv(location, intData);
            case TYPE_INT2 -> glUniform2iv(location, intData);
            case TYPE_INT3 -> glUniform3iv(location, intData);
            case TYPE_INT4 -> glUniform4iv(location, intData);
            default -> System.err.println("dataType is not in range 0 to 3. Ignoring.");//todo use logger
        }
    }

    private void uploadFloats() {
        floatData.rewind();
        switch (dataType) {
            case TYPE_FLOAT1 -> glUniform1fv(location, floatData);
            case TYPE_FLOAT2 -> glUniform2fv(location, floatData);
            case TYPE_FLOAT3 -> glUniform3fv(location, floatData);
            case TYPE_FLOAT4 -> glUniform4fv(location, floatData);
            default -> System.err.println("dataType is not in range 4 to 7. Ignoring.");//todo use logger
        }
    }

    private void uploadMatrix() {
        floatData.clear();
        switch (dataType) {
            case TYPE_MAT2 -> glUniformMatrix2fv(location, false, floatData);
            case TYPE_MAT3 -> glUniformMatrix3fv(location, false, floatData);
            case TYPE_MAT4 -> glUniformMatrix4fv(location, false, floatData);
        }
    }

    public int getLocation() {
        return location;
    }

    public int getCount() {
        return count;
    }

    public int getDataType() {
        return dataType;
    }

    public IntBuffer getIntData() {
        return intData;
    }

    public FloatBuffer getFloatData() {
        return floatData;
    }

    @Override
    public void close() {
        if (intData != null)
            memFree(intData);
        if (floatData != null)
            memFree(floatData);
    }
}
