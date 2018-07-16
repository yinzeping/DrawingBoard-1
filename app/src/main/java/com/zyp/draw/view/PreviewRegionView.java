package com.zyp.draw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhangyiipeng on 2018/7/6.
 */

public class PreviewRegionView extends AppCompatImageView implements View.OnTouchListener {

    public static final String TAG = "PreviewRegionView";

    private Paint mPaint;
    private Bitmap mPreviewBitmap;
    private Canvas mPreviewCanvas;
    private Paint ClipBoundsPaint;

    private OnZoomRegionListener mOnZoomRegionListener;

    public void setOnZoomRegionListener(OnZoomRegionListener onZoomRegionListener) {
        mOnZoomRegionListener = onZoomRegionListener;
    }

    public interface OnZoomRegionListener {
        void onZoomRegionMoved(Rect newRect);
    }

    public PreviewRegionView(Context context) {
        this(context, null);
    }

    public PreviewRegionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewRegionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(60);

        ClipBoundsPaint = new Paint();
        ClipBoundsPaint.setColor(Color.WHITE);
        ClipBoundsPaint.setAlpha(180);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPreviewCanvas == null || mPreviewBitmap == null) {
            mDestRect = new Rect(0, 0, getWidth(), getHeight());
            mPreviewBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mPreviewCanvas = new Canvas(mPreviewBitmap);
        }

        if (mClipBoundsRect != null) {
            if (mBgBitmap != null) {
                canvas.drawBitmap(scaleImageBitmap(mBgBitmap), left, top, null);
            }
            canvas.drawBitmap(mDrawBitmap, mSourceRect, mDestRect, null);

            mPreviewBitmap.eraseColor(Color.TRANSPARENT);
            mPreviewCanvas.drawRect(mDestRect, mPaint);
            mPreviewCanvas.drawRect(mClipBoundsRect, ClipBoundsPaint);//中心点指示器区域

            canvas.drawBitmap(mPreviewBitmap, 0, 0, null);
        }

        super.onDraw(canvas);
    }

    public void moveDestView(Rect clipBoundsRect, float scaleFactor) {
        this.mClipBoundsRect = new Rect((int) (clipBoundsRect.left / scaleFactor), (int) (clipBoundsRect.top / scaleFactor),
                (int) (clipBoundsRect.right / scaleFactor), (int) (clipBoundsRect.bottom / scaleFactor));

        invalidate();
    }

    private int mCurrentMotionEvent = -1;
    private boolean mMoveZoomArea = false;

    private float mStartTouchX, mStartTouchY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentMotionEvent = MotionEvent.ACTION_DOWN;
                mMoveZoomArea = false;

                if (touchX >= mClipBoundsRect.left && touchX <= mClipBoundsRect.right
                        && touchY >= mClipBoundsRect.top && touchY <= mClipBoundsRect.bottom) {
                    mMoveZoomArea = true;
                    mStartTouchX = touchX;
                    mStartTouchY = touchY;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if ((mCurrentMotionEvent == MotionEvent.ACTION_DOWN
                        || mCurrentMotionEvent == MotionEvent.ACTION_MOVE) && mMoveZoomArea) {
                    mCurrentMotionEvent = MotionEvent.ACTION_MOVE;
                    Rect preview = new Rect(
                            mClipBoundsRect.left + (int) (touchX - mStartTouchX),
                            mClipBoundsRect.top + (int) (touchY - mStartTouchY),
                            mClipBoundsRect.right + (int) ((touchX - mStartTouchX)),
                            mClipBoundsRect.bottom + (int) ((touchY - mStartTouchY)));

                    if (preview.left >= 0 && preview.right <= getWidth()
                            && preview.top >= 0 && preview.bottom <= getHeight()) {
                        mClipBoundsRect = preview;
                        invalidate();
                    }
                    if (mOnZoomRegionListener != null) {
                        mOnZoomRegionListener.onZoomRegionMoved(mClipBoundsRect);
                    }

                    mStartTouchX = touchX;
                    mStartTouchY = touchY;

                }
                break;

            case MotionEvent.ACTION_UP:
                mCurrentMotionEvent = MotionEvent.ACTION_UP;
                mMoveZoomArea = false;
                break;
        }
        return true;
    }

    private Bitmap mDrawBitmap;
    private Bitmap mBgBitmap;

    private Rect mClipBoundsRect;
    private Rect mSourceRect;
    private Rect mDestRect;

    private int left;
    private int top;

    public void drawPreviewRegion(Bitmap drawBitmap, Bitmap bgBitmap, int left, int top, Rect clipBoundsRect, float scaleFactor) {
        this.mDrawBitmap = drawBitmap;
        this.mBgBitmap = bgBitmap;
        this.left = (int) (left / scaleFactor);
        this.top = (int) (top / scaleFactor);
        this.mClipBoundsRect = new Rect((int) (clipBoundsRect.left / scaleFactor), (int) (clipBoundsRect.top / scaleFactor),
                (int) (clipBoundsRect.right / scaleFactor), (int) (clipBoundsRect.bottom / scaleFactor));

        mSourceRect = new Rect(0, 0, mDrawBitmap.getWidth(), mDrawBitmap.getHeight());

        invalidate();
    }

    public Bitmap scaleImageBitmap(Bitmap bitmap) {
        float scaleRatio = 1;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float screenRatio = 1.0f;
        float imgRatio = (float) height / (float) width;
        float drawRatio = (float) getHeight() / (float) getWidth();
        if (imgRatio >= screenRatio && drawRatio < imgRatio) {
            //高度大于屏幕，以高为准
            scaleRatio = (float) getHeight() / (float) height;
        } else {
            scaleRatio = (float) getWidth() / (float) width;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleRatio, scaleRatio);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

    }
}
