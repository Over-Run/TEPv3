#version 150 core

in vec3 Position;
in vec4 Color;

out vec4 out_Color;

uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    out_Color = Color;
}
