#version 300 es

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 inTexCoord;
uniform mat4 transform;

out vec2 texCoord;

void main()
{
//    transform = mat4(
//        0.70710677, 0.70710677, 0.0, 0.0,
//        -0.70710677, 0.70710677, 0.0, 0.0,
//        0.0, 0.0, 1.0, 0.0,
//        0.0, 0.0, 0.0, 1.0);

    gl_Position = transform * vec4(position.x, position.y, position.z, 1.0);
    texCoord = inTexCoord;
}
