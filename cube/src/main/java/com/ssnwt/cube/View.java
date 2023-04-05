package com.ssnwt.cube;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class View extends GLSurfaceView {
    public View(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
