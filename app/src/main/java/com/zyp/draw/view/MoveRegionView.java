package com.zyp.draw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by zhangyiipeng on 2018/7/6.
 */

public class MoveRegionView extends RelativeLayout implements View.OnTouchListener {

    public MoveRegionView(Context context) {
        this(context, null);
    }

    public MoveRegionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveRegionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    public interface OnTouchMoveListener {
        void onTouchMove(float mx, float my,boolean isMovind);
    }

    private OnTouchMoveListener mTouchMoveListener;

    public void setTouchMoveListener(OnTouchMoveListener touchMoveListener) {
        mTouchMoveListener = touchMoveListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int mCurrentMotionEvent = -1;

    private float mStartTouchX, mStartTouchY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentMotionEvent = MotionEvent.ACTION_DOWN;


                break;

            case MotionEvent.ACTION_MOVE:
                if ((mCurrentMotionEvent == MotionEvent.ACTION_DOWN
                        || mCurrentMotionEvent == MotionEvent.ACTION_MOVE)) {
                    mCurrentMotionEvent = MotionEvent.ACTION_MOVE;

                    if (mTouchMoveListener != null && mStartTouchX != 0 && mStartTouchY != 0) {
                        mTouchMoveListener.onTouchMove(touchX - mStartTouchX, touchY - mStartTouchY,true);
                    }

                    mStartTouchX = touchX;
                    mStartTouchY = touchY;

                }
                break;

            case MotionEvent.ACTION_UP:
                mCurrentMotionEvent = MotionEvent.ACTION_UP;
                if (mTouchMoveListener != null && mStartTouchX != 0 && mStartTouchY != 0) {
                    mTouchMoveListener.onTouchMove(0, 0,false);
                }

                mStartTouchX = 0;
                mStartTouchY = 0;
                break;
        }
        return true;
    }

}
