package com.zyp.draw.view;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by zhangyiipeng on 2018/7/6.
 */

public class DrawPathData {
    private Paint mPaint;
    private int mColor;
    private float mStrokeWidth;
    private Path mPath;

    public DrawPathData(Path path, Paint paint) {
        mPaint = paint;
        mPath = path;

        mStrokeWidth = paint.getStrokeWidth();
        mColor = paint.getColor();
    }

    public Paint getPaint() {
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        return mPaint;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public Path getPath() {
        return mPath;
    }
}
