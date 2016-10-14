package me.yumengsong.ychat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import static java.lang.Boolean.TRUE;

/**
 * Custom View:
 * 1. attr.xml
 * 2. onMeasure
 * 3. onDraw
 * Created by yumeng on 10/6/16.
 */

public class ChangeColorIconWithText extends View {
    public static final int CHANGED_COLOR = 0xFF45C01A;
    public static final int DEFAULT_COLOR = 0xFF555555;
    private int mColor;
    private Bitmap mIconBitmap;
    private String mText;
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha = 1.0f; //0.0-1.0

    private Rect mIconRect;
    private Rect mTextBound;

    private Paint mTextPaint;

    public ChangeColorIconWithText(Context context) {
        this(context, null);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconWithText);
        int n = typedArray.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorIconWithText_icon:
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) typedArray.getDrawable(attr);
                    mIconBitmap = bitmapDrawable.getBitmap();
                    break;
                case R.styleable.ChangeColorIconWithText_color:
                    mColor = typedArray.getColor(attr, CHANGED_COLOR);
                    break;
                case R.styleable.ChangeColorIconWithText_text:
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.ChangeColorIconWithText_text_size:
                    mTextSize = (int) typedArray.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            }
        }
        typedArray.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setColor(DEFAULT_COLOR);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mTextBound.height());

        int left = (getMeasuredWidth() - iconWidth) / 2;
        int top = (getMeasuredHeight() - mTextBound.height() - iconWidth) / 2;

        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha); //math.ceil(x)返回大于参数x的最小整数,即对浮点数向上取整
        //Prepare in the memory: mBitmap, setAlpha, color, xfermode to draw the icon
        setupTargetBitmap(alpha);

        //1. draw source text 2. set color-changing for the text
        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);

        canvas.drawBitmap(mIconBitmap, 0, 0, null);
    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 + (mIconRect.width() - mTextBound.width()) / 2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 + (mIconRect.width() - mTextBound.width()) / 2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /**
     * Draw the color-changing icon in the memory.
     */
    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAlpha(alpha);
        mPaint.setAntiAlias(TRUE); //去锯齿
        mPaint.setDither(TRUE);
        mCanvas.drawRect(mIconRect, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        inValidateView();
    }

    /**
     * 重绘
     */
    private void inValidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //UI线程
            invalidate();
        } else {
            postInvalidate();
        }
    }


//    private int mColor = 0xFF45C01A;
//    private Bitmap mIconBitmap;
//    private String mText = "微信";
//    private int mTextSize = (int) TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
//
//    private Canvas mCanvas;
//    private Bitmap mBitmap;
//    private Paint mPaint;
//
//    private float mAlpha;
//
//    private Rect mIconRect;
//    private Rect mTextBound;
//    private Paint mTextPaint;
//
//    public ChangeColorIconWithText(Context context)
//    {
//        this(context, null);
//    }
//
//    public ChangeColorIconWithText(Context context, AttributeSet attrs)
//    {
//        this(context, attrs, 0);
//    }
//
//    /**
//     * 获取自定义属性的值
//     *
//     * @param context
//     * @param attrs
//     * @param defStyleAttr
//     */
//    public ChangeColorIconWithText(Context context, AttributeSet attrs,
//                                   int defStyleAttr)
//    {
//        super(context, attrs, defStyleAttr);
//
//        TypedArray a = context.obtainStyledAttributes(attrs,
//                R.styleable.ChangeColorIconWithText);
//
//        int n = a.getIndexCount();
//
//        for (int i = 0; i < n; i++)
//        {
//            int attr = a.getIndex(i);
//            switch (attr)
//            {
//                case R.styleable.ChangeColorIconWithText_icon:
//                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
//                    mIconBitmap = drawable.getBitmap();
//                    break;
//                case R.styleable.ChangeColorIconWithText_color:
//                    mColor = a.getColor(attr, 0xFF45C01A);
//                    break;
//                case R.styleable.ChangeColorIconWithText_text:
//                    mText = a.getString(attr);
//                    break;
//                case R.styleable.ChangeColorIconWithText_text_size:
//                    mTextSize = (int) a.getDimension(attr, TypedValue
//                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
//                                    getResources().getDisplayMetrics()));
//                    break;
//            }
//
//        }
//
//        a.recycle();
//
//        mTextBound = new Rect();
//        mTextPaint = new Paint();
//        mTextPaint.setTextSize(mTextSize);
//        mTextPaint.setColor(0Xff555555);
//        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
//
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//    {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
//                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
//                - getPaddingBottom() - mTextBound.height());
//
//        int left = getMeasuredWidth() / 2 - iconWidth / 2;
//        int top = getMeasuredHeight() / 2 - (mTextBound.height() + iconWidth)
//                / 2;
//        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
//
//        int alpha = (int) Math.ceil(255 * mAlpha);
//
//        // 内存去准备mBitmap , setAlpha , 纯色 ，xfermode ， 图标
//        setupTargetBitmap(alpha);
//        // 1、绘制原文本 ； 2、绘制变色的文本
//        drawSourceText(canvas, alpha);
//        drawTargetText(canvas, alpha);
//
//        canvas.drawBitmap(mBitmap, 0, 0, null);
//
//    }
//
//    /**
//     * 绘制变色的文本
//     *
//     * @param canvas
//     * @param alpha
//     */
//    private void drawTargetText(Canvas canvas, int alpha)
//    {
//        mTextPaint.setColor(mColor);
//        mTextPaint.setAlpha(alpha);
//        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
//        int y = mIconRect.bottom + mTextBound.height();
//        canvas.drawText(mText, x, y, mTextPaint);
//
//    }
//
//    /**
//     * 绘制原文本
//     *
//     * @param canvas
//     * @param alpha
//     */
//    private void drawSourceText(Canvas canvas, int alpha)
//    {
//        mTextPaint.setColor(0xff333333);
//        mTextPaint.setAlpha(255 - alpha);
//        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
//        int y = mIconRect.bottom + mTextBound.height();
//        canvas.drawText(mText, x, y, mTextPaint);
//
//    }
//
//    /**
//     * 在内存中绘制可变色的Icon
//     */
//    private void setupTargetBitmap(int alpha)
//    {
//        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
//                Bitmap.Config.ARGB_8888);
//        mCanvas = new Canvas(mBitmap);
//        mPaint = new Paint();
//        mPaint.setColor(mColor);
//        mPaint.setAntiAlias(true);
//        mPaint.setDither(true);
//        mPaint.setAlpha(alpha);
//        mCanvas.drawRect(mIconRect, mPaint);
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        mPaint.setAlpha(255);
//        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
//    }
//
//    private static final String INSTANCE_STATUS = "instance_status";
//    private static final String STATUS_ALPHA = "status_alpha";
//
//    @Override
//    protected Parcelable onSaveInstanceState()
//    {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
//        bundle.putFloat(STATUS_ALPHA, mAlpha);
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state)
//    {
//        if (state instanceof Bundle)
//        {
//            Bundle bundle = (Bundle) state;
//            mAlpha = bundle.getFloat(STATUS_ALPHA);
//            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
//            return;
//        }
//        super.onRestoreInstanceState(state);
//    }
//
//    public void setIconAlpha(float alpha)
//    {
//        this.mAlpha = alpha;
//        invalidateView();
//    }
//
//    /**
//     * 重绘
//     */
//    private void invalidateView()
//    {
//        if (Looper.getMainLooper() == Looper.myLooper())
//        {
//            invalidate();
//        } else
//        {
//            postInvalidate();
//        }
//    }
}
