#version 300 es

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 inVertexColor;
layout(location = 2) in vec2 inTexCoord;
uniform mat4 transform;
out vec3 vertexColor;
out vec2 texCoord;
void main() {

    gl_Position = transform * vec4(position.x, position.y, position.z, 1);
    vertexColor = inVertexColor;
    texCoord = inTexCoord;
}
