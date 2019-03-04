package com.zyp.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Author：Cuzz.Yin
 * Date：2019/3/1 9:41
 * Description：
 */
public class PathSelectView extends View {
    Path linePath;
    Paint paint;
    float point1X, point1Y, point2X, point2Y;

    public PathSelectView(Context context) {
        super(context);
        init();
    }

    public PathSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        point1X=300;
        point1Y=600;
        point2X=800;
        point2Y=1100;

        linePath = new Path();

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setDither(true);
//        paint.setPathEffect(new PathDashPathEffect(linePath,12,0, PathDashPathEffect.Style.ROTATE));//线的显示效果：破折号格式
        paint.setPathEffect(new DashPathEffect(new float[]{10f, 5f}, 0));//线的显示效果：破折号格式
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        linePath.reset();
        // draw the line
        linePath.moveTo(point1X, point1Y);
        linePath.lineTo(point2X, point2Y);

        linePath.addRect(point1X, point1Y, point2X, point2Y, Path.Direction.CCW);

        canvas.drawPath(linePath, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(Utils.isLineIntersectRectangle(
                    new Point((int)point1X, (int)point1Y),
                    new Point((int)point2X, (int)point2Y),
                    new Point((int)event.getX()-40, (int)event.getY()-40),
                    new Point((int)event.getX()+40, (int)event.getY()+40))){
                Log.d("onTouchEvent", "onTouchEvent.select the line");
                Toast.makeText(getContext(), "onTouchEvent.select the line", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);
    }
}
