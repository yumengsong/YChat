package me.yumengsong.ychat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

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

    private int Alpha;

    private Rect mIconRect;
    private Rect mTextBound;

    private Paint mTextPaint;

    public ChangeColorIconWithText(Context context) {
        super(context);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    }
}
