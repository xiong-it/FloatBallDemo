package io.github.xiong_it.floatballdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.y;

/**
 * Created by xiongxunxiang on 2017/2/14.
 */

public class FloatBallView extends View {
    private Paint mBallPaint;
    private Paint mTextPaint;

    private int mWidth;
    private int mHeight;

    private boolean mIsDragState;
    private Bitmap mBgBitmap;

    public FloatBallView(Context context) {
        super(context);
        init();
    }

    public FloatBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatBallView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mWidth = (int) getResources().getDimension(R.dimen.float_ball_width);
        mHeight = (int) getResources().getDimension(R.dimen.float_ball_height);

        mBallPaint = new Paint();
        mBallPaint.setColor(Color.GRAY);
        mBallPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.floatball);
        mBgBitmap = src.createScaledBitmap(src, mWidth, mHeight, true);
        src.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsDragState) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mBallPaint);

            String s = "50%";
            float measureText = mTextPaint.measureText(s);
            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            float x = mWidth / 2 - measureText / 2;
            float y = mHeight / 2 - (metrics.descent + metrics.ascent) / 2;
            canvas.drawText(s, x, y, mTextPaint);
        } else {
            canvas.drawBitmap(mBgBitmap, 0, 0, null);
        }
    }

    /**
     * 是否处于拖拽
     * @param isdrag
     */
    public void setDragState(boolean isdrag) {
        mIsDragState = isdrag;
        invalidate();
    }
}
