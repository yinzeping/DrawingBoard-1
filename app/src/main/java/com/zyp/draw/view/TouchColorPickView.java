package com.zyp.draw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhangyiipeng on 2018/7/6.
 */

public class TouchColorPickView extends View implements View.OnTouchListener {

    public static final String TAG = "TouchColorPickView";
    private int measuredWidth;
    private int measuredHeight;
    private Paint paint;

    public TouchColorPickView(Context context) {
        this(context, null);
    }

    public TouchColorPickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchColorPickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
        //设置画笔抗锯齿和抗抖动
        paint = new Paint();
        paint.setStrokeWidth(10);  //画笔的宽度
        paint.setColor(strokeColor); //画笔的颜色
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();
    }

    private int strokeColor = Color.BLACK;
    private float mScaleFactor = 1;

    public void setPickBitmapColor(int color, Bitmap bitmap, float scaleFactor) {
        this.strokeColor = color;
        this.mBitmap = bitmap;
        this.mScaleFactor = scaleFactor;
        paint.setColor(strokeColor);
        invalidate();
    }

    private Bitmap mBitmap;
    private int r = 10;

    @Override
    protected void onDraw(Canvas canvas) {

        int centerX = measuredWidth / 2;
        int centerY = measuredHeight / 2;
        int radius = measuredHeight / 2;
        if (mBitmap != null) {
            int sc0 = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.drawCircle(centerX, centerY, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.save();
            canvas.scale(mScaleFactor * 2, mScaleFactor * 2, centerX, centerY);
            canvas.drawBitmap(mBitmap, 0, 0, paint);
            canvas.restore();
            //还原
            paint.setXfermode(null);
            //绘制红色十字中心点
            paint.setColor(Color.RED);
            paint.setStrokeWidth(1f);
            canvas.drawLine(centerX - r, centerY, centerX + r, centerY, paint);
            canvas.drawLine(centerX, centerY - r, centerX, centerY + r, paint);

            canvas.restoreToCount(sc0);
        }
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        paint.setColor(strokeColor);
        canvas.drawCircle(centerX, centerY, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawCircle(centerX, centerY, radius * 3 / 4, paint);
        //还原
        paint.setXfermode(null);

        canvas.restoreToCount(sc);
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
