package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FainActivity extends AppCompatActivity {

    GLSurfaceView glv;
    Texture tr1 = null;
    Texture tr2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fain);
        glv = new GLSurfaceView(this);
        glv.setEGLContextClientVersion(2);
        setContentView(glv);

        glv.setRenderer(new GLSurfaceView.Renderer() {
            private final float[] mMVPMatrix = new float[16];
            private final float[] mProjectionMatrix = new float[16];
            private final float[] mViewMatrix = new float[16];
            private final float[] mRotationMatrix = new float[16];
            private float[] mTranMatrix = new float[16];
            private float[] scratch = new float[16];
            private float[] ed = new float[16];
            private float[] op = new float[16];
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                gl.glClearColor(0.1f, 0.5f, 0.6f, 0.0f);
                gl.glEnable(GL10.GL_BLEND);
                gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);



                Bitmap t = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.raw.loading02);
                Bitmap t2 = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.raw.loading02);
                tr1 = new Texture(t2, 0);
                tr2 = new Texture(t2, 1);
                tr1.show = true;
                tr2.show = true;
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

                gl.glViewport(0, 0, width, height);
                float ratio = (float) width / height;
                Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                long time = SystemClock.uptimeMillis() % 4000L;
                float angle = 0.090f * ((int) time);
                double rad = angle/180*Math.PI;
                Matrix.setLookAtM(mViewMatrix,0, 0, 0, -5
                                            , 0f, 0f, 0f
                                            , 0f, 2.0f, 0.0f);
                Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
                //Matrix.
                float x = (float) Math.cos(rad) * 0.3f;
                float y = (float) Math.sin(rad) * 0.3f;
                Matrix.setIdentityM(mTranMatrix, 0);
                Matrix.rotateM(mTranMatrix, 0, angle, 1, -1f, 0);
                //Matrix.translateM(mTranMatrix, 0, 1, 0, 0);

                //Matrix.multiplyMM(scratch, 0, mTranMatrix, 0, mRotationMatrix, 0);
                Matrix.multiplyMM(ed, 0, mMVPMatrix, 0, mTranMatrix, 0);
               // Matrix.multiplyMM(op, 0, mMVPMatrix, 0, mRotationMatrix, 0);
                gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
                if(tr1!=null) {
                    tr1.draw(ed);
                    tr2.draw(mMVPMatrix);
                }else {
                }
            }
        });

        glv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tr1.show = !tr1.show;
                tr2.show = !tr2.show;
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        glv.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glv.onResume();
    }
}
