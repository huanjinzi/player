package com.hjz.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render extends GLES20 implements GLSurfaceView.Renderer,MediaPlayer.OnVideoSizeChangedListener,
        SurfaceTexture.OnFrameAvailableListener {
    private MediaPlayer mediaPlayer;
    private Context context;
    private SurfaceTexture st;
    private String path = "/storage/emulated/0/电影/vivo X21 TVC 周冬雨篇.mp4";

    private final float[] vertexData = {
            1f,-1f,0f,
            -1f,-1f,0f,
            1f,1f,0f,
            -1f,1f,0f
    };
    private final float[] textureVertexData = {
            1f,0f,
            0f,0f,
            1f,1f,
            0f,1f
    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureVertexBuffer;

    public Render(Context context){
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureVertexBuffer.position(0);

    }

    int aPositionLocation;
    int uMatrixLocation;
    int uSTMatrixLocation;
    int uTextureSamplerLocation;
    int aTextureCoordLocation;
    int texId;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        IntBuffer ib = IntBuffer.allocate(1);
        glGenTextures(1, ib);
        texId = ib.get(0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texId);
        glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
        glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        String vertexSource = ShaderUtils.getShaderString(context, R.raw.quota);
        String fragmentSource = ShaderUtils.getShaderString(context, R.raw.fragment);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader,vertexSource);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,fragmentSource);
        glCompileShader(fragmentShader);

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);

        aPositionLocation = glGetAttribLocation(program, "aPosition");
        uMatrixLocation = glGetUniformLocation(program, "uMatrix");
        uSTMatrixLocation = glGetUniformLocation(program, "uSTMatrix");
        uTextureSamplerLocation = glGetUniformLocation(program, "sTexture");
        aTextureCoordLocation = glGetAttribLocation(program, "aTexCoord");

        st = new SurfaceTexture(texId);
        st.setOnFrameAvailableListener(this);
        Surface surface = new Surface(st);
        mediaPlayer.setSurface(surface);
        surface.release();

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    int screenWidth;
    int screenHeight;
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    private float[] mSTMatrix = new float[16];
    int program;
    private final float[] projectionMatrix=new float[16];
    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        st.updateTexImage();
        st.getTransformMatrix(mSTMatrix);
        glUseProgram(program);

        glUniformMatrix4fv(uSTMatrixLocation,1,false,projectionMatrix,0);
        glUniformMatrix4fv(uMatrixLocation,1,false,mSTMatrix,0);

        vertexBuffer.position(0);
        glEnableVertexAttribArray(aPositionLocation);
        glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 12, vertexBuffer);

        textureVertexBuffer.position(0);
        glEnableVertexAttribArray(aTextureCoordLocation);
        glVertexAttribPointer(aTextureCoordLocation,2,GL_FLOAT,false,8,textureVertexBuffer);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,texId);
        glViewport(0, 0, screenWidth, screenHeight);

        glUniform1i(uTextureSamplerLocation,0);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        updateProjection(width, height);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }

    private void updateProjection(int videoWidth, int videoHeight){
        float screenRatio = (float) screenWidth / screenHeight;
        float videoRatio = (float) videoWidth / videoHeight;
        if (videoRatio > screenRatio) {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -videoRatio / screenRatio, videoRatio / screenRatio, -1f, 1f);
        } else {
            Matrix.orthoM(projectionMatrix, 0, -screenRatio / videoRatio, screenRatio / videoRatio, -1f, 1f, -1f, 1f);
        }
    }
}
