package com.example.maskview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * https://blog.csdn.net/desireyaoo/article/details/53642009
 */
public class MaskPierceView extends View {

    private Bitmap mSrcRect; // 遮罩层
    private Bitmap mDstCircle; // 镂空层圆形形状

    private int mPiercedX, mPiercedY;
    private int mPiercedRadius;

    private int mScreenWidth;   // 屏幕的宽
    private int mScreenHeight;  // 屏幕的高

    public MaskPierceView(Context context) {
        super(context);
    }

    public MaskPierceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 默认铺满的Layout
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        if (mScreenWidth == 0) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
        }

    }

    /**
     * @param mPiercedX      镂空的圆心坐标
     * @param mPiercedY      镂空的圆心坐标
     * @param mPiercedRadius 镂空的圆半径
     */
    public void setPiercePosition(int mPiercedX, int mPiercedY, int mPiercedRadius) {
        this.mPiercedX = mPiercedX;
        this.mPiercedY = mPiercedY;
        this.mPiercedRadius = mPiercedRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mSrcRect = makeSrcRect(); // 遮罩层形状
        mDstCircle = makeDstCircle(); // 镂空层圆形形状

        Paint paint = new Paint();
        paint.setFilterBitmap(false); // 也有函数setFilterBitmap可以用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
        canvas.saveLayer(0, 0, mScreenWidth, mScreenHeight, null, Canvas.ALL_SAVE_FLAG); // 新建一个图层

        canvas.drawBitmap(mDstCircle, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)); //  混合模式：镂空层圆形形状注意这里使用了镂空
        paint.setAlpha(160); //  镂空层圆形形状使用透明度

        canvas.drawBitmap(mSrcRect, 0, 0, paint); // 画上遮罩层形状
        paint.setXfermode(null); // 还原混合模式
    }

    /**
     * @return 遮罩层形状
     */
    private Bitmap makeSrcRect() {
        // 创建一个跟屏幕一样大小的遮罩层图
        Bitmap bm = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvcs = new Canvas(bm); // 画布
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 画笔抗锯齿
        paint.setColor(Color.BLACK); // 颜色
        canvcs.drawRect(new RectF(0, 0, mScreenWidth, mScreenHeight), paint);
        return bm;
    }

    /**
     * @return 镂空层圆形形状
     */
    private Bitmap makeDstCircle() {
        Bitmap bm = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvcs = new Canvas(bm); // 画布
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 画笔抗锯齿
        paint.setColor(Color.WHITE); // 颜色

        canvcs.drawCircle(mPiercedX, mPiercedY, mPiercedRadius, paint);
        return bm;
    }

}
