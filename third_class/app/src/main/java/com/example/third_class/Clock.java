package com.example.third_class;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private float PANEL_RADIUS = 200.0f;// 表盘半径

    private float HOUR_POINTER_LENGTH;// 指针长度
    private float MINUTE_POINTER_LENGTH;
    private float SECOND_POINTER_LENGTH;
    private float UNIT_DEGREE = (float) (6 * Math.PI / 180);// 一个小格的度数

    private int mWidth, mCenterX, mCenterY, mRadius;
    private int nowHours, nowMinutes, nowSeconds;
    private boolean am, end = false;

    private int degreesColor;
    private final static String TAGS = "tips";

    private Paint mNeedlePaint, mTextPaint;
    private Handler mHandler = new Handler ();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            passOneSecond ();
            Log.d (TAGS, nowHours + ":" + nowMinutes + ":" + nowSeconds + "\t");
        }
    };

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();
        nowHours = 0;
        nowMinutes = 0;
        nowSeconds = 0;
        am = true;

        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw (final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;
        PANEL_RADIUS = mRadius;
        HOUR_POINTER_LENGTH = PANEL_RADIUS - 400;
        MINUTE_POINTER_LENGTH = PANEL_RADIUS - 250;
        SECOND_POINTER_LENGTH = PANEL_RADIUS - 190;

        drawDegrees(canvas);
        drawHoursValues(canvas);
        drawNeedles(canvas);
        drawHoursValues(canvas);
    }

    private void drawDegrees (Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.06f);
        int rEnd = mCenterX - (int) (mWidth * 0.1f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0) {
                paint.setAlpha(CUSTOM_ALPHA);
            } else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        float textPos = PANEL_RADIUS * 0.91f;

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mWidth * 0.04f);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();

        canvas.drawText("Ⅻ", mCenterX, mCenterY - textPos, mTextPaint);
        canvas.drawText("Ⅲ", mCenterX + textPos * 1.05f, mCenterY - 0.5f * fontMetrics.ascent, mTextPaint);
        canvas.drawText("Ⅵ", mCenterX, mCenterY + textPos - fontMetrics.ascent, mTextPaint);
        canvas.drawText("Ⅸ", mCenterX - textPos * 1.05f, mCenterY - 0.5f * fontMetrics.ascent, mTextPaint);
    }

    private void drawNeedles(final Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int part;
        // 画秒针
        drawPointer(canvas, 2, nowSeconds);
        // 画分针
        part = nowSeconds / 60;
        drawPointer(canvas, 1, nowMinutes + part);
        // 画时针
        part = nowMinutes / 12;
        drawPointer(canvas, 0, 5 * nowHours + part);
    }


    private void drawPointer(Canvas canvas, int pointerType, int value) {

        float degree;
        float[] pointerHeadXY = new float[2];

        mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        switch (pointerType) {
            case 0:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.WHITE);
                pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
                break;
            case 1:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.BLUE);
                pointerHeadXY = getPointerHeadXY(MINUTE_POINTER_LENGTH, degree);
                break;
            case 2:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.GREEN);
                pointerHeadXY = getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
                break;
        }

        canvas.drawLine(mCenterX, mCenterY, pointerHeadXY[0], pointerHeadXY[1], mNeedlePaint);
    }

    private float[] getPointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (mCenterX + pointerLength * Math.sin(degree));
        xy[1] = (float) (mCenterY - pointerLength * Math.cos(degree));
        return xy;
    }

    private void passOneSecond () {
        nowSeconds++;
        if (nowSeconds == 60) {
            nowSeconds = 0;
            nowMinutes++;
            if (nowMinutes == 60) {
                nowMinutes = 0;
                nowHours++;
                if (nowHours == 12) {
                    nowHours = 0;
                    am = !am;
                }
            }
        }
        if (! end) {
            mHandler.postDelayed (runnable, 1000);
        }
    }

    public void start () {
        end = false;
        passOneSecond ();
    }

    public int[] getTime () {
        int[] time = new int[3];

        time[0] = nowHours;
        if (!am) {
            time[0] += 12;
        }
        time[1] = nowMinutes;
        time[2] = nowSeconds;

        return time;
    }

    public void setTime (int[] time) {
        if (time[0] >= 12) {
            am = false;
            nowHours = time[0] % 12;
        }else {
            am = true;
            nowHours = time[0];
        }
        nowMinutes = time[1];
        nowSeconds = time[2];
    }
}