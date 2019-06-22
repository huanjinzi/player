package com.hjz.gles_triangle;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private Render render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(glSurfaceView);

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
}
