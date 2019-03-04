package com.zyp.draw;

import android.graphics.Paint;
import android.graphics.Point;

/**
 * Author：Cuzz.Yin
 * Date：2019/3/1 16:58
 * Description：
 */
public class Utils {
    /**
     * Paint类介绍
     *
     * Paint即画笔，在绘图过程中起到了极其重要的作用，画笔主要保存了颜色，
     * 样式等绘制信息，指定了如何绘制文本和图形，画笔对象有很多设置方法，
     * 大体上可以分为两类，一类与图形绘制相关，一类与文本绘制相关。
     *
     * 1.图形绘制
     * setARGB(int a,int r,int g,int b);
     * 设置绘制的颜色，a代表透明度，r，g，b代表颜色值。
     *
     * setAlpha(int a);
     * 设置绘制图形的透明度。
     *
     * setColor(int color);
     * 设置绘制的颜色，使用颜色值来表示，该颜色值包括透明度和RGB颜色。
     *
     * setAntiAlias(boolean aa);
     * 设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
     *
     * setDither(boolean dither);
     * 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
     *
     * setFilterBitmap(boolean filter);
     * 如果该项设置为true，则图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示
     * 速度，本设置项依赖于dither和xfermode的设置
     *
     * setMaskFilter(MaskFilter maskfilter);
     * 设置MaskFilter，可以用不同的MaskFilter实现滤镜的效果，如滤化，立体等
     *
     * setColorFilter(ColorFilter colorfilter);
     * 设置颜色过滤器，可以在绘制颜色时实现不用颜色的变换效果
     *
     * setPathEffect(PathEffect effect);
     * 设置绘制路径的效果，如点画线等
     *
     * setShader(Shader shader);
     * 设置图像效果，使用Shader可以绘制出各种渐变效果
     *
     * setShadowLayer(float radius ,float dx,float dy,int color);
     * 在图形下面设置阴影层，产生阴影效果，radius为阴影的角度，dx和dy为阴影在x轴和y轴上的距离，color为阴影的颜色
     *
     * setStyle(Paint.Style style);
     * 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE    Style.FILL: 实心   STROKE:空心   FILL_OR_STROKE:同时实心与空心

     *
     * setStrokeCap(Paint.Cap cap);
     * 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
     * Cap.ROUND,或方形样式Cap.SQUARE
     *
     * setSrokeJoin(Paint.Join join);
     * 设置绘制时各图形的结合方式，如平滑效果等
     *
     * setStrokeWidth(float width);
     * 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度
     *
     * setXfermode(Xfermode xfermode);
     * 设置图形重叠时的处理方式，如合并，取交集或并集，经常用来制作橡皮的擦除效果
     *
     * 2.文本绘制
     * setFakeBoldText(boolean fakeBoldText);
     * 模拟实现粗体文字，设置在小字体上效果会非常差
     *
     * setSubpixelText(boolean subpixelText);
     * 设置该项为true，将有助于文本在LCD屏幕上的显示效果
     *
     * setTextAlign(Paint.Align align);
     * 设置绘制文字的对齐方向
     *
     * setTextScaleX(float scaleX);
     * 设置绘制文字x轴的缩放比例，可以实现文字的拉伸的效果
     *
     * setTextSize(float textSize);
     * 设置绘制文字的字号大小
     *
     * setTextSkewX(float skewX);
     * 设置斜体文字，skewX为倾斜弧度
     *
     * setTypeface(Typeface typeface);
     * 设置Typeface对象，即字体风格，包括粗体，斜体以及衬线体，非衬线体等
     *
     * setUnderlineText(boolean underlineText);
     * 设置带有下划线的文字效果
     *
     * setStrikeThruText(boolean strikeThruText);
     * 设置带有删除线的效果
     *
     */
    private Paint paint;

    /**
     * 判断线段是否在矩形内
     *
     * 先看线段所在直线是否与矩形相交，
     * 如果不相交则返回false，
     * 如果相交，
     * 则看线段的两个点是否在矩形的同一边（即两点的x(y)坐标都比矩形的小x(y)坐标小，或者大）,
     * 若在同一边则返回false，
     * 否则就是相交的情况。
     * @param linePointStart 线段起始点
     * @param linePointEnd 线段结束点
     * @param rectangleLeftTop 矩形左上点
     * @param rectangleRightBottom 矩形右下点
     * @return 是否相交
     */
    public static boolean isLineIntersectRectangle(Point linePointStart, Point linePointEnd, Point rectangleLeftTop, Point rectangleRightBottom){
        return isLineIntersectRectangle(linePointStart.x, linePointStart.y, linePointEnd.x, linePointEnd.y, rectangleLeftTop.x, rectangleLeftTop.y, rectangleRightBottom.x, rectangleRightBottom.y);
    }

    /**
     * 判断线段是否在矩形内
     *
     * 先看线段所在直线是否与矩形相交，
     * 如果不相交则返回false，
     * 如果相交，
     * 则看线段的两个点是否在矩形的同一边（即两点的x(y)坐标都比矩形的小x(y)坐标小，或者大）,
     * 若在同一边则返回false，
     * 否则就是相交的情况。
     * @param linePointX1 线段起始点x坐标
     * @param linePointY1 线段起始点y坐标
     * @param linePointX2 线段结束点x坐标
     * @param linePointY2 线段结束点y坐标
     * @param rectangleLeftTopX 矩形左上点x坐标
     * @param rectangleLeftTopY 矩形左上点y坐标
     * @param rectangleRightBottomX 矩形右下点x坐标
     * @param rectangleRightBottomY 矩形右下点y坐标
     * @return 是否相交
     */
    private static boolean isLineIntersectRectangle(int linePointX1,
                                                    int linePointY1,
                                                    int linePointX2,
                                                    int linePointY2,
                                                    int rectangleLeftTopX,
                                                    int rectangleLeftTopY,
                                                    int rectangleRightBottomX,
                                                    int rectangleRightBottomY)
    {
        int  lineHeight = linePointY1 - linePointY2;
        int lineWidth = linePointX2 - linePointX1;  // 计算叉乘
        int c = linePointX1 * linePointY2 - linePointX2 * linePointY1;
        if ((lineHeight * rectangleLeftTopX + lineWidth * rectangleLeftTopY + c >= 0 && lineHeight * rectangleRightBottomX + lineWidth * rectangleRightBottomY + c <= 0)
                || (lineHeight * rectangleLeftTopX + lineWidth * rectangleLeftTopY + c <= 0 && lineHeight * rectangleRightBottomX + lineWidth * rectangleRightBottomY + c >= 0)
                || (lineHeight * rectangleLeftTopX + lineWidth * rectangleRightBottomY + c >= 0 && lineHeight * rectangleRightBottomX + lineWidth * rectangleLeftTopY + c <= 0)
                || (lineHeight * rectangleLeftTopX + lineWidth * rectangleRightBottomY + c <= 0 && lineHeight * rectangleRightBottomX + lineWidth * rectangleLeftTopY + c >= 0))
        {

            if (rectangleLeftTopX > rectangleRightBottomX) {
                int temp = rectangleLeftTopX;
                rectangleLeftTopX = rectangleRightBottomX;
                rectangleRightBottomX = temp;
            }
            if (rectangleLeftTopY < rectangleRightBottomY) {
                int temp1 = rectangleLeftTopY;
                rectangleLeftTopY = rectangleRightBottomY;
                rectangleRightBottomY = temp1;   }
            if ((linePointX1 < rectangleLeftTopX && linePointX2 < rectangleLeftTopX)
                    || (linePointX1 > rectangleRightBottomX && linePointX2 > rectangleRightBottomX)
                    || (linePointY1 > rectangleLeftTopY && linePointY2 > rectangleLeftTopY)
                    || (linePointY1 < rectangleRightBottomY && linePointY2 < rectangleRightBottomY)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

}
