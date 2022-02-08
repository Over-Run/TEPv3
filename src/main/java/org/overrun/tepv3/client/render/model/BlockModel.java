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

package org.overrun.tepv3.client.render.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import org.overrun.tepv3.util.Direction;
import org.overrun.tepv3.util.Identifier;

import static org.overrun.tepv3.util.JsonHelper.*;

/**
 * @author squid233
 * @since 3.0.1
 */
@Deprecated(since = "3.0.1", forRemoval = true)
public class BlockModel {
    private final JsonObject json;

    public BlockModel(JsonObject json) {
        this.json = json;
    }

    /**
     * The model element.
     *
     * @author squid233
     * @since 3.0.1
     */
    public static class Element {
        private double[] from, to;
        private Faces faces;

        public Element(double[] from,
                       double[] to,
                       Faces faces) {
            this.from = from;
            this.to = to;
            this.faces = faces;
        }

        public Element() {
        }

        /**
         * The element faces.
         *
         * @author squid233
         * @since 3.0.1
         */
        public static class Faces {
            private Face west;
            private Face east;
            private Face down;
            private Face up;
            private Face north;
            private Face south;

            public Faces(Face west,
                         Face east,
                         Face down,
                         Face up,
                         Face north,
                         Face south) {
                this.west = west;
                this.east = east;
                this.down = down;
                this.up = up;
                this.north = north;
                this.south = south;
            }

            public Faces() {
            }

            public Face getWest() {
                return west;
            }

            public void setWest(Face west) {
                this.west = west;
            }

            public Face getEast() {
                return east;
            }

            public void setEast(Face east) {
                this.east = east;
            }

            public Face getDown() {
                return down;
            }

            public void setDown(Face down) {
                this.down = down;
            }

            public Face getUp() {
                return up;
            }

            public void setUp(Face up) {
                this.up = up;
            }

            public Face getNorth() {
                return north;
            }

            public void setNorth(Face north) {
                this.north = north;
            }

            public Face getSouth() {
                return south;
            }

            public void setSouth(Face south) {
                this.south = south;
            }

            /**
             * The element face (single).
             *
             * @author squid233
             * @since 3.0.1
             */
            public static class Face {
                private String name;
                private String cullFace;

                public Face(String name,
                            String cullFace) {
                    this.name = name;
                    this.cullFace = cullFace;
                }

                public Face() {
                }

                public Identifier getName(BlockModel model,
                                          BlockModelManager mgr) {
                    return new Identifier(model.getDefine(name, mgr));
                }

                public Direction getCullFace() {
                    return Direction.getByName(cullFace);
                }
            }
        }

        public double[] getFrom() {
            return from;
        }

        public void setFrom(double[] from) {
            this.from = from;
        }

        public double[] getTo() {
            return to;
        }

        public void setTo(double[] to) {
            this.to = to;
        }

        public Faces getFaces() {
            return faces;
        }

        public void setFaces(Faces faces) {
            this.faces = faces;
        }
    }

    /**
     * Get parent model from a model manager.
     *
     * @param mgr The block model manager.
     * @return The parent model, in json named "parent".
     */
    @Nullable
    public BlockModel getParent(BlockModelManager mgr) {
        if (hasString(json, "parent"))
            return mgr.getModel(new Identifier(getString(json, "parent")));
        return null;
    }

    @Nullable
    public JsonArray getElements(@Nullable BlockModel parent,
                                 BlockModelManager mgr) {
        if (parent == null)
            return null;
        if (hasArray(parent.json, "elements"))
            return getArray(parent.json, "elements");
        return getElements(parent.getParent(mgr), mgr);
    }

    @Nullable
    public JsonArray getElements(BlockModelManager mgr) {
        return getElements(this, mgr);
    }

    /**
     * Get vector {@code from}.
     *
     * @param element The element in array named "elements".
     * @return The position array, may be is normalized by 16.
     */
    public double[] getFrom(JsonElement element) {
        var obj = element.getAsJsonObject();
        var arr = getArray(obj, "from");
        return new double[]{
            arr.get(0).getAsDouble() / 16.0,
            arr.get(1).getAsDouble() / 16.0,
            arr.get(2).getAsDouble() / 16.0
        };
    }

    /**
     * Get vector {@code TO}.
     *
     * @param element The element in array named "elements".
     * @return The position array, may be is normalized by 16.
     */
    public double[] getTo(JsonElement element) {
        var obj = element.getAsJsonObject();
        var arr = getArray(obj, "to");
        return new double[]{
            arr.get(0).getAsDouble() / 16.0,
            arr.get(1).getAsDouble() / 16.0,
            arr.get(2).getAsDouble() / 16.0
        };
    }

    @Nullable
    public JsonObject getFaces(JsonElement element) {
        return getObject(element.getAsJsonObject(), "faces", null);
    }

    @Nullable
    public JsonObject getDefines(@Nullable BlockModel parent,
                                 BlockModelManager mgr) {
        if (parent == null)
            return null;
        if (hasJsonObject(parent.json, "defines"))
            return getObject(json, "defines");
        return getDefines(parent.getParent(mgr), mgr);
    }

    @Nullable
    public JsonObject getDefines(BlockModelManager mgr) {
        return getDefines(this, mgr);
    }

    public String getDefine(BlockModel parent,
                            String name,
                            BlockModelManager mgr) {
        // Can't find anyway
        if (parent == null)
            return null;
        var obj = parent.getDefines(mgr);
        if (obj == null) {
            // Try to find in parent
            return getDefine(parent.getParent(mgr), name, mgr);
        }
        var value = getString(obj, name, null);
        if (value == null) {
            // Try to find in parent
            return getDefine(parent.getParent(mgr), name, mgr);
        }
        // if value still a macro
        if (value.startsWith("#")) {
            return getDefine(parent, value.substring(1), mgr);
        }
        // the final value
        return obj.get(name).getAsString();
    }

    public String getDefine(String name,
                            BlockModelManager mgr) {
        return getDefine(this, name, mgr);
    }
}
