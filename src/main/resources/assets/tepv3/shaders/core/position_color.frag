#version 150 core

in vec4 vertexColor;

out vec4 fragColor;

uniform vec4 ColorModulator;

void main() {
    vec4 color = vertexColor;
    if (color.a == 0.0) {
        discard;
    }
    fragColor = color * ColorModulator;
}
