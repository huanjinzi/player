package com.ssnwt.cube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render extends GLES30 implements GLSurfaceView.Renderer {
    private Context context;
    float[] vertex = {
            -1f,1f,1f,  1f,0f,0f,   0f,0f,      //0
            1f,1f,1f,   0f,1f,0f,   1f,0f,      //1
            -1f,-1f,1f, 0f,0f,1f,   0f,1f,      //2
            1f,-1f,1f,  1f,1f,0f,   1f,1f,      //3

            -1f,1f,-1f, 1f,0f,0f,   0f,0f,      //4
            1f,1f,-1f,  0f,1f,0f,   1f,0f,      //5
            -1f,-1f,-1f,0f,0f,1f,   0f,1f,      //6
            1f,-1f,-1f, 1f,1f,0f,   1f,1f       //7
    };

    float[] vertexs = {
            -1f,1f,1f,  1f,0f,0f,   0f,0f,      //0
            1f,1f,1f,   0f,1f,0f,   1f,0f,      //1
            1f,-1f,1f,  1f,1f,0f,   1f,1f,      //3

            -1f,1f,1f,  1f,0f,0f,   0f,0f,      //0
            -1f,-1f,1f, 0f,0f,1f,   0f,1f,      //2
            1f,-1f,1f,  1f,1f,0f,   1f,1f,      //3

            1f,-1f,1f,  1f,1f,0f,   0f,1f,      //3
            1f,1f,1f,   0f,1f,0f,   0f,0f,      //1
            1f,1f,-1f,  0f,1f,0f,   1f,0f,      //5

            1f,-1f,1f,  1f,1f,0f,   0f,1f,      //3
            1f,-1f,-1f, 1f,1f,0f,   1f,1f,      //7
            1f,1f,-1f,  0f,1f,0f,   1f,0f,      //5

            1f,1f,-1f,  0f,1f,0f,   0f,0f,      //5
            -1f,1f,-1f, 1f,0f,0f,   1f,0f,      //4
            -1f,-1f,-1f,0f,0f,1f,   1f,1f,      //6

            1f,1f,-1f,  0f,1f,0f,   0f,0f,      //5
            1f,-1f,-1f, 1f,1f,0f,   0f,1f,      //7
            -1f,-1f,-1f,0f,0f,1f,   1f,1f,      //6

            -1f,1f,1f,  1f,0f,0f,   1f,0f,      //0
            -1f,-1f,1f, 0f,0f,1f,   1f,1f,      //2
            -1f,-1f,-1f,0f,0f,1f,   0f,1f,      //6

            -1f,1f,1f,  1f,0f,0f,   1f,0f,      //0
            -1f,1f,-1f, 1f,0f,0f,   0f,0f,      //4
            -1f,-1f,-1f,0f,0f,1f,   0f,1f,      //6

            1f,-1f,1f,  1f,1f,0f,   1f,0f,      //3
            1f,-1f,-1f, 1f,1f,0f,   1f,0f,      //7
            -1f,-1f,-1f,0f,0f,1f,   0f,0f,      //6

            1f,-1f,1f,  1f,1f,0f,   1f,0f,      //3
            -1f,-1f,1f, 0f,0f,1f,   0f,0f,      //2
            -1f,-1f,-1f,0f,0f,1f,   0f,0f,      //6

            -1f,1f,1f,  1f,0f,0f,   0f,1f,      //0
            -1f,1f,-1f, 1f,0f,0f,   0f,0f,      //4
            1f,1f,-1f,  0f,1f,0f,   1f,0f,      //5

            -1f,1f,1f,  1f,0f,0f,   0f,1f,      //0
            1f,1f,1f, 1f,0f,0f,     1f,1f,      //1
            1f,1f,-1f,  0f,1f,0f,   1f,0f,      //5
    };

    int[] index = {
            // front
            0, 1, 3,
            0, 2, 3,
            // right
            3, 1, 5,
            3, 7, 5,
            // back
            5, 4, 6,
            5, 7, 6,
            // left
            0, 2, 6,
            0, 4, 6,
            // bottom
            3, 2, 6,
            3, 7, 6,
            // top
            0, 1, 5,
            0, 4, 5
    };

    public Render(Context context) {
        this.context = context;
    }

    FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertex);

    FloatBuffer vertexsBuffer = ByteBuffer.allocateDirect(vertexs.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexs);

    IntBuffer indexBuffer = ByteBuffer.allocateDirect(index.length * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
            .put(index);

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        compileProgram();
        uploadVertex();
        uploadTexture();
        genVertexArrayObject();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio * 2, ratio * 2, -2f, 2f, 2, 7);
        Matrix.setLookAtM(mViewMatrix,0,
                0f,3f,4,
                0f,0f,0f,
                0f,1f,0f
        );
    }

    int program;
    int vbo;
    int ebo;
    int vao;
    @Override
    public void onDrawFrame(GL10 gl) {
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glClearDepthf(4f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(program);
        Matrix.rotateM(mViewMatrix, 0, 0.5f, 0, 1, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix,0);
        int location = glGetUniformLocation(program, "transform");

        glUniformMatrix4fv(location, 1, false, mMVPMatrix, 0);
        glBindTexture(GL_TEXTURE_2D, texture);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    int texture;
    private void uploadTexture() {
        int[] b = new int[1];
        glGenTextures(1, b, 0);
        texture = b[0];

        glBindTexture(GL_TEXTURE_2D,texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // set texture filtering parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.dujuan7, options);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
        bitmap.recycle();
    }

    private void uploadVertex() {
        int[] b = new int[2];
        glGenBuffers(2, b, 0);
        vbo = b[0];
        ebo = b[1];

        vertexsBuffer.rewind();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexsBuffer.limit() * 4, vertexsBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        indexBuffer.rewind();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.limit() * 4, indexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
    }

    private void genVertexArrayObject() {
        int[] b = new int[1];
        glGenVertexArrays(1, b, 0);
        vao = b[0];

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        //config vertex pointer
        vertexsBuffer.rewind();
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4);
        glEnableVertexAttribArray(2);
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
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
