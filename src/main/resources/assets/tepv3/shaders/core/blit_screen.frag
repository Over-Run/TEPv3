#version 150 core

in vec4 vertexColor;
in vec2 texCoord;

out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulator;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord) * vertexColor;
    fragColor = color * ColorModulator;
}
