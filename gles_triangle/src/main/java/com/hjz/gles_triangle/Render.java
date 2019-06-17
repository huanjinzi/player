package com.hjz.gles_triangle;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Render extends GLES30 implements GLSurfaceView.Renderer {
    private static final String TAG = "huanjinzi";
    private Context context;
    float[] vertex = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f,
    };

    FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertex.length*4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertex);

    public Render(Context context) {
        this.context = context;
    }

    int program;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, ShaderUtils.getShaderString(context, R.raw.vertex));
        glCompileShader(vertexShader);

        IntBuffer params = IntBuffer.allocate(100);
        glGetShaderiv(vertexShader,GL_COMPILE_STATUS,params);
        String log = glGetShaderInfoLog(vertexShader);
        Log.d(TAG, log);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, ShaderUtils.getShaderString(context, R.raw.fragment));
        glCompileShader(fragmentShader);

        glGetShaderiv(fragmentShader,GL_COMPILE_STATUS,params);
        log = glGetShaderInfoLog(fragmentShader);
        Log.d(TAG, log);

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
        glDisable(GL_BLEND);
        glDisable(GL_CULL_FACE);
        glDisable(GL_SCISSOR_TEST);
        glDisable(GL_POLYGON_OFFSET_FILL);

        glUseProgram(program);
        vertexBuffer.position(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, vertexBuffer);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    public void onPause(){

    }

    public void onResume(){

    }
}
