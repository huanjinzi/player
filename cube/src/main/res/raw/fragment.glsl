#version 300 es

in vec3 vertexColor;
in vec2 texCoord;
out vec4 color;

uniform sampler2D texPic;// texture unit 0,default active
void main() {
    vec4 texColor = texture(texPic,texCoord);
    //float gray = 0.299 * texColor.r + 0.587 * texColor.g + 0.114 * texColor.b;
    //color = vec4(vertexColor,0.0);
    color = texColor;
}
