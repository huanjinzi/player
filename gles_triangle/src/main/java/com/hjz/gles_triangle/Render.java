package com.hjz.gles_triangle;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Render extends GLES30 implements GLSurfaceView.Renderer {
    private Context context;
    float[] vertex = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f,
    };

    FloatBuffer vertexBuffer = FloatBuffer.wrap(vertex);

    public Render(Context context) {
        this.context = context;
    }

    int vbo;
    int program;
    int vao;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f,1.0f,0.0f,0.0f);

        int[] b = new int[1];
        glGenBuffers(1,b,0);
        vbo = b[0];

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        vertexBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer.limit(),vertexBuffer,GL_STATIC_DRAW);

        glGenVertexArrays(1, b, 0);
        vao = b[0];

        glBindVertexArray(vao);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3, 0);



        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, ShaderUtils.getShaderString(context, R.raw.vertex));
        glCompileShader(vertexShader);

        IntBuffer params = IntBuffer.allocate(100);
        glGetShaderiv(vertexShader,GL_COMPILE_STATUS,params);
        String log = glGetShaderInfoLog(vertexShader);
        Log.d("huanjinzi", log);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, ShaderUtils.getShaderString(context, R.raw.fragment));
        glCompileShader(fragmentShader);

        glGetShaderiv(fragmentShader,GL_COMPILE_STATUS,params);
        log = glGetShaderInfoLog(fragmentShader);
        Log.d("huanjinzi", log);

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUseProgram(program);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindVertexArray(0);
    }

    public void onPause(){

    }

    public void onResume(){

    }
}
