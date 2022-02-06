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

import org.overrun.tepv3.util.MapBuilder;

import static org.overrun.tepv3.client.render.VertexFormatElement.DataType.*;
import static org.overrun.tepv3.client.render.VertexFormatElement.Type.*;

/**
 * @author squid233
 * @since 3.0.1
 */
public class VertexFormats {
    public static final VertexFormatElement POSITION_ELEMENT = new VertexFormatElement(0, FLOAT, VertexFormatElement.Type.POSITION, 3);
    public static final VertexFormatElement COLOR_ELEMENT = new VertexFormatElement(0, UBYTE, COLOR, 4);
    public static final VertexFormatElement TEXTURE_0_ELEMENT = new VertexFormatElement(0, FLOAT, UV, 2);
    public static final VertexFormatElement OVERLAY_ELEMENT = new VertexFormatElement(1, SHORT, UV, 2);
    public static final VertexFormatElement LIGHT_ELEMENT = new VertexFormatElement(2, SHORT, UV, 2);
    public static final VertexFormatElement NORMAL_ELEMENT = new VertexFormatElement(0, BYTE, NORMAL, 3);
    public static final VertexFormatElement PADDING_ELEMENT = new VertexFormatElement(0, BYTE, PADDING, 1);
    public static final VertexFormatElement TEXTURE_ELEMENT = TEXTURE_0_ELEMENT;
    public static final VertexFormat BLIT_SCREEN = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("UV", TEXTURE_ELEMENT).of("Color", COLOR_ELEMENT).build());
    public static final VertexFormat POSITION_COLOR_TEXTURE_LIGHT_NORMAL = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("Color", COLOR_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).of("UV2", LIGHT_ELEMENT).of("Normal", NORMAL_ELEMENT).of("Padding", PADDING_ELEMENT).build());
    public static final VertexFormat POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("Color", COLOR_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).of("UV1", OVERLAY_ELEMENT).of("UV2", LIGHT_ELEMENT).of("Normal", NORMAL_ELEMENT).of("Padding", PADDING_ELEMENT).build());
    public static final VertexFormat POSITION_TEXTURE_COLOR_LIGHT = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).of("Color", COLOR_ELEMENT).of("UV2", LIGHT_ELEMENT).build());
    public static final VertexFormat POSITION = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).build());
    public static final VertexFormat POSITION_COLOR = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("Color", COLOR_ELEMENT).build());
    public static final VertexFormat LINES = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("Color", COLOR_ELEMENT).of("Normal", NORMAL_ELEMENT).of("Padding", PADDING_ELEMENT).build());
    public static final VertexFormat POSITION_COLOR_LIGHT = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("Color", COLOR_ELEMENT).of("UV2", LIGHT_ELEMENT).build());
    public static final VertexFormat POSITION_TEXTURE = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).build());
    public static final VertexFormat POSITION_COLOR_TEXTURE = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("Color", COLOR_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).build());
    public static final VertexFormat POSITION_TEXTURE_COLOR = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).of("Color", COLOR_ELEMENT).build());
    public static final VertexFormat POSITION_COLOR_TEXTURE_LIGHT = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("Color", COLOR_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).of("UV2", LIGHT_ELEMENT).build());
    public static final VertexFormat POSITION_TEXTURE_LIGHT_COLOR = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).of("UV2", LIGHT_ELEMENT).of("Color", COLOR_ELEMENT).build());
    public static final VertexFormat POSITION_TEXTURE_COLOR_NORMAL = new VertexFormat(MapBuilder.create("Position", POSITION_ELEMENT).of("UV0", TEXTURE_0_ELEMENT).of("Color", COLOR_ELEMENT).of("Normal", NORMAL_ELEMENT).of("Padding", PADDING_ELEMENT).build());
}
