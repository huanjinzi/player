#version 300 es

in vec3 vertexColor;
out vec4 color;

void main() {
    color = vec4(vertexColor,0);
}
