package com.hjz.gles_triangle;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private Render render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (glSurfaceView == null) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            glSurfaceView = new GLSurfaceView(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dm.widthPixels, dm.heightPixels);
            glSurfaceView.setLayoutParams(params);
            glSurfaceView.setEGLContextClientVersion(3);
            render = new Render(this);
            glSurfaceView.setRenderer(render);
            glSurfaceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    render.onClick();
                }
            });
        }

//        if (savedInstanceState != null) {
//            render.number = savedInstanceState.getInt("index");
//        }
        setContentView(glSurfaceView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        outState.putInt("index", (int) render.number);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        render.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        render.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
