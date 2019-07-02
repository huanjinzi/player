package com.ssnwt.cube;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render extends GLES30 implements GLSurfaceView.Renderer {
    private Context context;
    float[] vertex = {
            -1f,-1f,0.0f, 1.0f,0f,0f,
            1f,-1f,0.0f, 0.0f,1.0f,0f,
            0f,1f,0.0f, 0.0f,0.0f,1.0f,
    };

    public Render(Context context) {
        this.context = context;
    }

    FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertex);

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        compileProgram();
        uploadVertex();
        genVertexArrayObject();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio * 2, ratio * 2, -2f, 2f, 2, 11);
        Matrix.setLookAtM(mViewMatrix,0,
                0f,0f,3,
                0f,0f,0f,
                0f,1f,0f
        );
    }

    int program;
    int vbo;
    int vao;
    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUseProgram(program);

        Matrix.rotateM(mViewMatrix,0,1,0,1,0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix,0);
        int location = glGetUniformLocation(program, "transform");
        glUniformMatrix4fv(location, 1, false, mMVPMatrix, 0);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    private void uploadVertex() {
        int[] b = new int[1];
        glGenBuffers(1, b, 0);
        vbo = b[0];

        vertexBuffer.rewind();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.limit() * 4, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void genVertexArrayObject() {
        int[] b = new int[1];
        glGenVertexArrays(1, b, 0);
        vao = b[0];

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        //config vertex pointer
        vertexBuffer.rewind();
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 24, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 24, 12);
        glEnableVertexAttribArray(1);
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void compileProgram() {
        String vertexString = ShaderUtils.getShaderString(context, R.raw.vertex);
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexString);
        glCompileShader(vertexShader);
        Log.d("huanjinzi", glGetShaderInfoLog(vertexShader));

        String fragmentString = ShaderUtils.getShaderString(context, R.raw.fragment);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,fragmentString);
        glCompileShader(fragmentShader);
        Log.d("huanjinzi", glGetShaderInfoLog(fragmentShader));

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }
}
