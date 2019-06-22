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
            // in picture,(0,0) start at left-top
            // in texture,(0,0) start at left-bottom
            // so,we inverse the y(0 -> 1,1 -> 0)
            -1f, -1f, 0.0f, 0f, 1f,
            1f, -1f, 0.0f, 1f, 1f,
            1f, 1f, 0.0f, 1f, 0f,
            -1f, 1f, 0.0f, 0f, 0f
    };

    int[] indices = new int[]{
            0,1,2,
            0,2,3
    };

    FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertex);

    IntBuffer indexBuffer = ByteBuffer.allocateDirect(indices.length * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
            .put(indices);

    public Render(Context context) {
        this.context = context;
    }

    int program;
    int index = 0;

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
        w = width;
        h = height;
    }

    int vbo;
    int ebo;
    int vao;

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glViewport((w - widths[index]) / 2 , (h - heights[index]) / 2, widths[index], heights[index]);
        glUseProgram(program);
        glBindTexture(GL_TEXTURE_2D, textures[index]);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6,GL_UNSIGNED_INT,0);
    }

    public void onPause() {

    }

    public void onResume() {

    }

    long number;
    public void onClick() {
        number++;
        index = (int) (number % 6);
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
        int[] b = new int[2];
        glGenBuffers(2, b, 0);
        vbo = b[0];
        ebo = b[1];

        vertexBuffer.rewind();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.limit() * 4, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        indexBuffer.rewind();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer.limit() * 4,indexBuffer,GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
    }

    private void genVertexObject() {
        int[] b = new int[1];
        glGenVertexArrays(1, b, 0);
        vao = b[0];

        vertexBuffer.rewind();
        indexBuffer.rewind();
        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ebo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    int[] textures;
    int[] widths;
    int[] heights;

    int w;
    int h;

    private void uploadTexture() {
        textures = new int[6];
        widths = new int[6];
        heights = new int[6];

        glGenTextures(6, textures, 0);

        int[] images = {
                R.raw.dujuan,
                R.raw.dujuan2,
                R.raw.dujuan3,
                R.raw.dujuan4,
                R.raw.dujuan5,
                R.raw.dujuan7,
        };

        int i = 0;
        for (int texture:textures) {
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            // set texture filtering parameters
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), images[i], options);
            widths[i] = bitmap.getWidth();
            heights[i] = bitmap.getHeight();
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap, 0);

            glBindTexture(GL_TEXTURE_2D, 0);
            bitmap.recycle();
            i ++;
        }
    }
}
