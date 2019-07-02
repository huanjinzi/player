#version 300 es

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 inVertexColor;
uniform mat4 transform;
out vec3 vertexColor;
void main() {

    gl_Position = transform * vec4(position.x, position.y, position.z, 1);
    vertexColor = inVertexColor;
}
