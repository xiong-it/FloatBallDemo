package io.github.xiong_it.floatballdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by xiongxunxiang on 2017/2/14.
 * 自定义底部波浪小球
 */

public class BottomBallView extends View {
    private int mWidth;
    private int mHeight;
    private Paint mBallPaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private Path mPath = new Path();
    private int mProgress = 50;
    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;

    private int mCurrentCount = 50;
    private final int mMaxCount = 50;

    private boolean mDoubleTabState;

    private GestureDetector mGestureDetector;
    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            startDoubleTabAnim(e);
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            startSingleTabAnim(e);
            return super.onSingleTapConfirmed(e);
        }

    };

    private static final int MSG_DOUBLE_TAB_UPDATE = 0;
    private static final int MSG_SINGLE_TAB_UPDATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_DOUBLE_TAB_UPDATE) {
                mCurrentProgress++;
                setDoubleTabState(true);
                if (mCurrentProgress <= mProgress) {
                    invalidate();
                    sendEmptyMessageDelayed(MSG_DOUBLE_TAB_UPDATE, 100);
                } else {
                    removeMessages(MSG_DOUBLE_TAB_UPDATE);
                    mCurrentProgress = 0;
                }
            }
            if (msg.what == MSG_SINGLE_TAB_UPDATE) {
                mCurrentCount--;
                setDoubleTabState(false);
                if (mCurrentCount >= 0) {
                    invalidate();
                    sendEmptyMessageDelayed(MSG_SINGLE_TAB_UPDATE, 100);
                } else {
                    removeMessages(MSG_SINGLE_TAB_UPDATE);
                    mCurrentCount = 50;
                }
            }
        }
    };

    /**
     * 点击动画：波浪
     *
     * @param e
     */
    private void startSingleTabAnim(MotionEvent e) {
        Toast.makeText(getContext(), "单击", Toast.LENGTH_SHORT).show();
        mHandler.sendEmptyMessage(MSG_SINGLE_TAB_UPDATE);
    }

    /**
     * 双击动画：波浪上涨
     *
     * @param e
     */
    private void startDoubleTabAnim(MotionEvent e) {
        Toast.makeText(getContext(), "双击", Toast.LENGTH_SHORT).show();
        mHandler.sendEmptyMessage(MSG_DOUBLE_TAB_UPDATE);
    }

    public BottomBallView(Context context) {
        super(context);
        init();
    }

    public BottomBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomBallView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mWidth = (int) getResources().getDimension(R.dimen.ball_width);
        mHeight = (int) getResources().getDimension(R.dimen.ball_height);

        mBallPaint = new Paint();
        mBallPaint.setAntiAlias(true);
        mBallPaint.setColor(Color.argb(0xff, 0x3a, 0x8c, 0x6c));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.argb(0xff, 0x4e, 0xc9, 0x63));
        // 这个函数的作用还没弄懂，找个时间看下。
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
        mTextPaint.setColor(Color.WHITE);

        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);

        setClickable(true);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

    }

    void setDoubleTabState(boolean state) {
        mDoubleTabState = state;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas.drawCircle((mWidth / 2), (mHeight / 2), (mWidth / 2), mBallPaint);
//        canvas.drawCircle((mWidth / 2), (mHeight / 2), (mWidth / 2), mBallPaint);

        mPath.reset();
        float y = 0f;
        if (mDoubleTabState) {
            y = mHeight * (1 - (float) mCurrentProgress / mMaxProgress);
        } else {
            y = mHeight * (1 - (float) mProgress / mMaxProgress);
        }
        // 移动到第一点开始绘制
        mPath.moveTo(mWidth, y);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(0, y);

        int yCoordinateHigh = 20;
        // 绘制贝塞尔曲线：正弦曲线
        if (mDoubleTabState) {// 如果是双击状态
            float dh = (1 - ((float) mCurrentProgress / mProgress)) * yCoordinateHigh;
            drawQuadraticCurve(mCurrentProgress, dh);
        } else {// 单击或者默认状态
            float sh = ((float) mCurrentCount / (float)mMaxCount) * yCoordinateHigh;
            drawQuadraticCurve(mCurrentCount, sh);
        }
        mPath.close();
        mCanvas.drawPath(mPath, mProgressPaint);
        String s = "";
        if (mDoubleTabState) {
            s = (int)(((float) mCurrentProgress / mMaxProgress) * 100) + "%";
        } else {
            s = (int)(((float) mProgress / mMaxProgress) * 100) + "%";
        }
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float measureText = mTextPaint.measureText(s);
        float baseLineY = (mHeight / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2);
        mCanvas.drawText(s, (mWidth / 2 - measureText / 2), baseLineY, mTextPaint);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 绘制二次曲线
     * @param control 用来控制绘制上下弦的参数
     * @param h       用来控制y轴最高/低点的参数
     */
    private void drawQuadraticCurve(int control, float h) {
        int xCoordinateLen = 40;
        int n = mWidth / xCoordinateLen + 1;

        if (control %2 == 0) {
            for (int i = 0; i < n; i++) {
                // 绘制上弦线，参数解释如下：
                // x轴坐标：上次x轴坐标点的加上该参数，上弦x轴的控制点。第一绘制时x点为0。一般等于上弦长度/2。
                // y轴起点：上次y轴坐标点的加上该参数，上弦y轴的控制点。第一绘制时y点为0。一般为最高/低点坐标值*2。注意！Android中y轴坐标与常规数学中的坐标相反。
                // x轴终点：上次x轴坐标加上该值，确定最后x轴的终点。一般为上弦总长度。
                // y轴终点：上次y轴坐标加上该值，确定最后y轴的终点。一般为0。
                mPath.rQuadTo(xCoordinateLen / 2, -h, xCoordinateLen, 0);
                // 绘制下弦线
                mPath.rQuadTo(xCoordinateLen / 2, h, xCoordinateLen, 0);
            }
        } else {
            for (int i = 0; i < n; i++) {
                mPath.rQuadTo(xCoordinateLen / 2, h, xCoordinateLen, 0);
                mPath.rQuadTo(xCoordinateLen / 2, -h, xCoordinateLen, 0);
            }
        }
    }

}
