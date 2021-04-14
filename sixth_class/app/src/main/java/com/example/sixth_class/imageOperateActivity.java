package com.example.sixth_class;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class imageOperateActivity extends AppCompatActivity {
    private LayoutInflater layoutInflater = null;
    private ImageView imageView;
    private Drawable drawable;
    private ScaleGestureDetector scaleGestureDetector;

    private Matrix mSuppMatrix = new Matrix ();
    private Matrix mBaseMatrix = new Matrix ();
    private Matrix mDrawableMatrix = new Matrix ();
    private RectF mDisplayRect = new RectF ();

    private int drawableWidth;
    private int drawableHeight;
    private int viewWidth;
    private int viewHeight;

    private static final String TAG = "inform";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_img_operate);

        imageView = (ImageView) findViewById (R.id.img_operate);
        drawable = ContextCompat.getDrawable (this, R.drawable.scenery);
        imageView.setImageDrawable (drawable);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                drawableWidth = drawable.getIntrinsicWidth ();
                drawableHeight = drawable.getIntrinsicHeight ();
                viewWidth = imageView.getWidth () - imageView.getPaddingLeft () - imageView.getPaddingRight ();
                viewHeight = imageView.getHeight () - imageView.getPaddingTop () - imageView.getPaddingBottom ();

                RectF mTempScr = new RectF (0, 0, drawableWidth, drawableHeight);
                RectF mTempDst = new RectF (0, 0, viewWidth, viewHeight);

                mBaseMatrix.setRectToRect (mTempScr, mTempDst, Matrix.ScaleToFit.CENTER);
                mDrawableMatrix.set (mBaseMatrix);
                imageView.setImageMatrix (mDrawableMatrix);
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return scaleGestureDetector.onTouchEvent (event);
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                Log.d (TAG, String.valueOf (scaleFactor));
                float focusX = detector.getFocusX();
                float focusY = detector.getFocusY();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }

                mSuppMatrix.postScale (scaleFactor, scaleFactor, focusX, focusY);
                imageView.setImageMatrix (getDrawMatrix());

                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        });


    }

    private Matrix getDrawMatrix() {
        mDrawableMatrix.set (mBaseMatrix);
        mDrawableMatrix.postConcat (mSuppMatrix);

        return mDrawableMatrix;
    }
}
