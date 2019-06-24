package com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.activity.R;
import com.util.SouthUtil;

/**
 * Created by dell on 2018/3/19.
 */

public class MyTitleView extends View {

    private String text = "";
    private float textSize = SouthUtil.dp2px(30);
//    private Paint mPaintbg;
    private Paint mPaintText;
    private Rect mBound;

    private int width;
    private int height;

    private int mColor = Color.parseColor("#00F5FF");

    public MyTitleView(Context context) {
        this(context, null);
    }

    public MyTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTitleView, defStyleAttr, 0);

        text = array.getString(R.styleable.MyTitleView_title1);
        textSize=array.getDimension(R.styleable.MyTitleView_titleSize1,  SouthUtil.dp2px(30));
        array.recycle();

        //字体的颜色
        mPaintText = new Paint();
//        mPaintText.setStrokeWidth(SouthUtil.dp2px(5));
        mPaintText.setColor(Color.BLUE);
        mPaintText.setTextSize(textSize);
        mPaintText.setAntiAlias(true);
        mBound = new Rect();
        mPaintText.getTextBounds(text, 0, text.length(), mBound);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.rotate(-90, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.drawText(text, (width - mBound.width()) / 2, (height + mBound.height()) / 2, mPaintText);
        canvas.restore();

    }


    public void setText(String str) {
        text = str;
        mBound = new Rect();
        mPaintText.getTextBounds(text, 0, text.length(), mBound);

        invalidate();
    }

    //设置背景颜色
    public void setTextColor(String color) {
        mColor = Color.parseColor(color);
        mPaintText.setColor(mColor);
        invalidate();
    }


}
