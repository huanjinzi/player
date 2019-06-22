#version 300 es
//#extension GL_OES_EGL_image_external:require

out vec4 color;
in vec2 texCoord;

uniform sampler2D texPic;// texture unit 0,default active
void main()
{
    color = texture(texPic,texCoord);
}
