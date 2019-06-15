package com.hjz.player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dm.widthPixels,dm.heightPixels);
        glSurfaceView.setLayoutParams(params);

        glSurfaceView.setRenderer(new Render(this));
        setContentView(glSurfaceView);

        ContentResolver resolver = getContentResolver();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            Log.d("huanjinzi", filePath);
        }
        if (cursor != null) cursor.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
