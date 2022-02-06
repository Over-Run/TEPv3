#version 150 core

in vec3 Position;
in vec4 Color;
in vec2 UV;

out vec4 vertexColor;
out vec2 texCoord;

uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vertexColor = Color;
    texCoord = UV;
}
