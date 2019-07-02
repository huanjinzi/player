package com.ssnwt.cube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private View view;
    private Render render;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
