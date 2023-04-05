package com.ssnwt.cube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private View view;
    private Render render;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = new View(this);
        render = new Render(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(dm.widthPixels, dm.heightPixels);
        view.setLayoutParams(params);
        view.setEGLContextClientVersion(3);
        view.setRenderer(render);
        setContentView(view);
    }


    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }
}
