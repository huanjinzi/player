package com.hjz.gles_triangle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render extends GLES30 implements GLSurfaceView.Renderer {
    private static final String TAG = "huanjinzi";
    private Context context;
    float[] vertex = {
            -1f, -1f, 0.0f, 0f, 0f,
            1f, -1f, 0.0f, 1f, 0f,
            0f, 1f, 0.0f, 0.5f, 1f
    };

    FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertex);

    public Render(Context context) {
        this.context = context;
    }

    int program;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        compileShader();
        uploadVertex();
        uploadTexture();
        genVertexObject();
        glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport((width - this.width) /2 , (height - this.height) / 2, this.width, this.height);
    }

    int vbo;
    int vao;

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUseProgram(program);
        glBindTexture(GL_TEXTURE_2D, texture);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    public void onPause() {

    }

    public void onResume() {

    }

    private void compileShader() {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, ShaderUtils.getShaderString(context, R.raw.vertex));
        glCompileShader(vertexShader);

        IntBuffer params = IntBuffer.allocate(100);
        glGetShaderiv(vertexShader, GL_COMPILE_STATUS, params);
        String log = glGetShaderInfoLog(vertexShader);
        Log.d(TAG, log);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, ShaderUtils.getShaderString(context, R.raw.fragment));
        glCompileShader(fragmentShader);

        glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, params);
        log = glGetShaderInfoLog(fragmentShader);
        Log.d(TAG, log);

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
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

    private void genVertexObject() {
        int[] b = new int[1];
        glGenVertexArrays(1, b, 0);
        vao = b[0];

        vertexBuffer.rewind();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    int texture;
    int width;
    int height;

    private void uploadTexture() {
        int[] t = new int[1];
        glGenTextures(1, t, 0);
        texture = t[0];

        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // set texture filtering parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.dujuan4, options);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap, 0);

        glBindTexture(GL_TEXTURE_2D, 0);
        bitmap.recycle();

    }
}
