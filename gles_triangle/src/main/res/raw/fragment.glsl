#version 300 es
//#extension GL_OES_EGL_image_external : require

out vec4 color;
in vec2 texCoord;

uniform sampler2D texPic;// texture unit 0,default active
void main()
{
    vec4 texColor = texture(texPic,texCoord);
    float gray = 0.299 * texColor.r + 0.587 * texColor.g + 0.114 * texColor.b;
    color = vec4(gray,gray,gray,0);
}
