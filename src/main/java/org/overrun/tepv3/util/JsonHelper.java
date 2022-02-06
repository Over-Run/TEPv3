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

package org.overrun.tepv3.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author squid233
 * @since 3.0.1
 */
public class JsonHelper {
    private static final Gson GSON = new GsonBuilder().create();

    public static boolean hasString(JsonObject object, String name) {
        return hasPrimitive(object, name) && object.getAsJsonPrimitive(name).isString();
    }

    public static boolean isString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }

    public static boolean hasNumber(JsonObject object, String name) {
        return hasPrimitive(object, name) && object.getAsJsonPrimitive(name).isNumber();
    }

    public static boolean isNumber(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
    }

    public static boolean hasBoolean(JsonObject object, String name) {
        return hasPrimitive(object, name) && object.getAsJsonPrimitive(name).isBoolean();
    }

    public static boolean isBoolean(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean();
    }

    public static boolean hasArray(JsonObject object, String name) {
        return hasElement(object, name) && object.getAsJsonPrimitive(name).isJsonArray();
    }

    public static boolean hasJsonObject(JsonObject object, String name) {
        return hasElement(object, name) && object.getAsJsonPrimitive(name).isJsonObject();
    }

    public static boolean hasPrimitive(JsonObject object, String name) {
        return hasElement(object, name) && object.get(name).isJsonPrimitive();
    }

    public static boolean hasElement(JsonObject object, String name) {
        return object != null && object.has(name);
    }

    public static String asString(JsonElement element, String name) {
        if (element.isJsonPrimitive())
            return element.getAsString();
        throw new JsonSyntaxException("Expected " + name + " to be a string, was " + getType(element));
    }

    public static String getString(JsonObject object, String name) {
        if (object.has(name))
            return asString(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a string");
    }

    @Nullable
    public static String getString(JsonObject object, String name, @Nullable String def) {
        return object.has(name) ? asString(object.get(name), name) : def;
    }

    public static boolean asBoolean(JsonElement element, String name) {
        if (element.isJsonPrimitive())
            return element.getAsBoolean();
        throw new JsonSyntaxException("Expected " + name + " to be a Boolean, was " + getType(element));
    }

    public static boolean getBoolean(JsonObject object, String name) {
        if (object.has(name))
            return asBoolean(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a Boolean");
    }

    public static boolean getBoolean(JsonObject object, String name, boolean def) {
        return object.has(name) ? asBoolean(object.get(name), name) : def;
    }

    public static double asDouble(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsDouble();
        throw new JsonSyntaxException("Expected " + name + " to be a Double, was " + getType(element));
    }

    public static double getDouble(JsonObject object, String name) {
        if (object.has(name))
            return asDouble(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a Double");
    }

    public static double getDouble(JsonObject object, String name, double def) {
        return object.has(name) ? asDouble(object.get(name), name) : def;
    }

    public static float asFloat(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsFloat();
        throw new JsonSyntaxException("Expected " + name + " to be a Float, was " + getType(element));
    }

    public static float getFloat(JsonObject object, String name) {
        if (object.has(name))
            return asFloat(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a Float");
    }

    public static float getFloat(JsonObject object, String name, float def) {
        return object.has(name) ? asFloat(object.get(name), name) : def;
    }

    public static long asLong(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsLong();
        throw new JsonSyntaxException("Expected " + name + " to be a Long, was " + getType(element));
    }

    public static long getLong(JsonObject object, String name) {
        if (object.has(name))
            return asLong(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a Long");
    }

    public static long getLong(JsonObject object, String name, long def) {
        return object.has(name) ? asLong(object.get(name), name) : def;
    }

    public static int asInt(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsInt();
        throw new JsonSyntaxException("Expected " + name + " to be an Int, was " + getType(element));
    }

    public static int getInt(JsonObject object, String name) {
        if (object.has(name))
            return asInt(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find an Int");
    }

    public static int getInt(JsonObject object, String name, int def) {
        return object.has(name) ? asInt(object.get(name), name) : def;
    }

    public static byte asByte(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsByte();
        throw new JsonSyntaxException("Expected " + name + " to be a Byte, was " + getType(element));
    }

    public static byte getByte(JsonObject object, String name) {
        if (object.has(name))
            return asByte(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a Byte");
    }

    public static byte getByte(JsonObject object, String name, byte def) {
        return object.has(name) ? asByte(object.get(name), name) : def;
    }

    public static BigDecimal asBigDecimal(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsBigDecimal();
        throw new JsonSyntaxException("Expected " + name + " to be a BigDecimal, was " + getType(element));
    }

    public static BigDecimal getBigDecimal(JsonObject object, String name) {
        if (object.has(name))
            return asBigDecimal(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a BigDecimal");
    }

    public static BigDecimal getBigDecimal(JsonObject object, String name, BigDecimal def) {
        return object.has(name) ? asBigDecimal(object.get(name), name) : def;
    }

    public static BigInteger asBigInteger(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsBigInteger();
        throw new JsonSyntaxException("Expected " + name + " to be a BigInteger, was " + getType(element));
    }

    public static BigInteger getBigInteger(JsonObject object, String name) {
        if (object.has(name))
            return asBigInteger(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a BigInteger");
    }

    public static BigInteger getBigInteger(JsonObject object, String name, BigInteger def) {
        return object.has(name) ? asBigInteger(object.get(name), name) : def;
    }

    public static short asShort(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            return element.getAsShort();
        throw new JsonSyntaxException("Expected " + name + " to be a Short, was " + getType(element));
    }

    public static short getShort(JsonObject object, String name) {
        if (object.has(name))
            return asShort(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a Short");
    }

    public static short getShort(JsonObject object, String name, short def) {
        return object.has(name) ? asShort(object.get(name), name) : def;
    }

    public static JsonObject asObject(JsonElement element, String name) {
        if (element.isJsonObject())
            return element.getAsJsonObject();
        throw new JsonSyntaxException("Expected " + name + " to be a JsonObject, was " + getType(element));
    }

    public static JsonObject getObject(JsonObject object, String name) {
        if (object.has(name))
            return asObject(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a JsonObject");
    }

    @Nullable
    public static JsonObject getObject(JsonObject object, String name, @Nullable JsonObject def) {
        return object.has(name) ? asObject(object.get(name), name) : def;
    }

    public static JsonArray asArray(JsonElement element, String name) {
        if (element.isJsonArray())
            return element.getAsJsonArray();
        throw new JsonSyntaxException("Expected " + name + " to be a JsonArray, was " + getType(element));
    }

    public static JsonArray getArray(JsonObject object, String name) {
        if (object.has(name))
            return asArray(object.get(name), name);
        throw new JsonSyntaxException("Missing " + name + ", expected to find a JsonArray");
    }

    @Nullable
    public static JsonArray getArray(JsonObject object, String name, @Nullable JsonArray def) {
        return object.has(name) ? asArray(object.get(name), name) : def;
    }

    public static <T> T deserialize(@Nullable JsonElement element, String name, JsonDeserializationContext context, Class<? extends T> type) {
        if (element != null) {
            return context.deserialize(element, type);
        }
        throw new JsonSyntaxException("Missing " + name);
    }

    public static <T> T deserialize(JsonObject object, String name, JsonDeserializationContext context, Class<? extends T> type) {
        if (object.has(name)) {
            return deserialize(object.get(name), name, context, type);
        }
        throw new JsonSyntaxException("Missing " + name);
    }

    @Nullable
    public static <T> T deserialize(JsonObject object, String name, @Nullable T defaultValue, JsonDeserializationContext context, Class<? extends T> type) {
        if (object.has(name)) {
            return deserialize(object.get(name), name, context, type);
        }
        return defaultValue;
    }

    @Nullable
    public static <T> T deserialize(Gson gson, Reader reader, Class<T> type, boolean lenient) {
        try {
            var jsonReader = new JsonReader(reader);
            jsonReader.setLenient(lenient);
            return gson.getAdapter(type).read(jsonReader);
        }
        catch (IOException iOException) {
            throw new JsonParseException(iOException);
        }
    }

    @Nullable
    public static <T> T deserialize(Gson gson, Reader reader, TypeToken<T> typeToken, boolean lenient) {
        try {
            var jsonReader = new JsonReader(reader);
            jsonReader.setLenient(lenient);
            return gson.getAdapter(typeToken).read(jsonReader);
        }
        catch (IOException iOException) {
            throw new JsonParseException(iOException);
        }
    }

    @Nullable
    public static <T> T deserialize(Gson gson, String content, TypeToken<T> typeToken, boolean lenient) {
        return deserialize(gson, new StringReader(content), typeToken, lenient);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, String content, Class<T> clazz, boolean lenient) {
        return deserialize(gson, new StringReader(content), clazz, lenient);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, Reader reader, TypeToken<T> typeToken) {
        return deserialize(gson, reader, typeToken, false);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, String content, TypeToken<T> typeToken) {
        return deserialize(gson, content, typeToken, false);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, Reader reader, Class<T> clazz) {
        return deserialize(gson, reader, clazz, false);
    }

    @Nullable
    public static <T> T deserialize(Gson gson, String content, Class<T> clazz) {
        return deserialize(gson, content, clazz, false);
    }

    public static JsonObject deserialize(String content, boolean lenient) {
        return deserialize(new StringReader(content), lenient);
    }

    public static JsonObject deserialize(Reader reader, boolean lenient) {
        return deserialize(GSON, reader, JsonObject.class, lenient);
    }

    public static JsonObject deserialize(String content) {
        return deserialize(content, false);
    }

    public static JsonObject deserialize(Reader reader) {
        return deserialize(reader, false);
    }

    public static JsonArray deserializeArray(Reader reader) {
        return deserialize(GSON, reader, JsonArray.class, false);
    }

    public static String getType(@Nullable JsonElement element) {
        var string = StringUtils.abbreviateMiddle(String.valueOf(element), "...", 10);
        if (element == null) {
            return "null (missing)";
        }
        if (element.isJsonNull()) {
            return "null (json)";
        }
        if (element.isJsonArray()) {
            return "an array (" + string + ")";
        }
        if (element.isJsonObject()) {
            return "an object (" + string + ")";
        }
        if (element.isJsonPrimitive()) {
            var jsonPrimitive = element.getAsJsonPrimitive();
            if (jsonPrimitive.isNumber()) {
                return "a number (" + string + ")";
            }
            if (jsonPrimitive.isBoolean()) {
                return "a boolean (" + string + ")";
            }
        }
        return string;
    }
}
