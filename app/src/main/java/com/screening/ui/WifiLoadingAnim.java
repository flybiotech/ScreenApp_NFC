package com.screening.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by dell on 2018/7/24.
 */

public class WifiLoadingAnim extends View {


    public final int WIFI_STATE_SUCCESS = 0; //wifi连接成功
    public final int WIFI_STATE_PROCESS = 1;//wifi正在连接
    public final int WIFI_STATE_FAIL = 2;  // wifi连接失败，
    private float mWidth = 0f;
    private Paint mPaint;
    //WiFi图像有几条圆弧
    private int signalSize = 5;
    private static String TAG = "TAG_WifiLoadingAnim";


    private ValueAnimator valueAnimator;
    private float mAnimatedValue = 0.9f;

    //    这个构造方法只有一个参数Context上下文。当我们在JAVA代码中直接通过new关键在创建这个控件时，就会调用这个方法。
    public WifiLoadingAnim(Context context) {
        this(context, null);
    }

    /**
     * 　这个构造方法有两个参数：Context上下文和AttributeSet属性集。当我们需要在自定义控件中获取属性时，就默认调用这个构造方法。AttributeSet对象就是这个控件中定义的所有属性。
     * 　　我们可以通过AttributeSet对象的getAttributeCount()方法获取属性的个数，通过getAttributeName()方法获取到某条属性的名称，通过getAttributeValue()方法获取到某条属性的值。
     * 　　注意：不管有没有使用自定义属性，都会默认调用这个构造方法，“使用了自定义属性就会默认调用三个参数的构造方法”的说法是错误的。
     *
     * @param context
     * @param attrs
     */
    public WifiLoadingAnim(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 　　这个构造方法中有三个参数：Context上下文、AttributeSet属性集和defStyleAttr自定义属性的引用。这个构造方法不会默认调用，必须要手动调用，这个构造方法和两个参数的构造方法的唯一区别就是这个构造方法给我们默认传入了一个默认属性集。
     * 　　defStyleAttr指向的是自定义属性的<declare-styleable>标签中定义的自定义属性集，我们在创建TypedArray对象时需要用到defStyleAttr。
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */

    public WifiLoadingAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

//        mPaint.setColor(getResources().getColor(R.color.weakRed1));

    }


    /**
     * 自定义view的绘制过程，
     * Measure
     * View会先做一次测量，算出自己需要占用多大的面积。View的Measure过程给我们暴露了一个接口onMeasure，方法的定义是这样的，
     * <p>
     * Layout
     * Layout过程对于View类非常简单，同样View给我们暴露了onLayout方法
     * <p>
     * Draw
     * Draw过程，就是在canvas上画出我们需要的View样式。同样View给我们暴露了onDraw方法
     */

    @Override //属于测量过程
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > getHeight()) {
            mWidth = getMeasuredHeight();
        } else {
            mWidth = getMeasuredWidth();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();//锁画布(为了保存之前的画布状态)
        //平移，将画布的坐标原点向左右方向移动x，向上下方向移动y.canvas的默认位置是在（0,0）
        canvas.translate(0, mWidth / signalSize);
        //       signalSize WiFi图像有几条圆弧
        mPaint.setStrokeWidth(mWidth / signalSize / 2 / 2 / 2);
        int scale = (int) ((mAnimatedValue * signalSize - (int) (mAnimatedValue * signalSize)) * signalSize) + 1;
        RectF rect = null;
        float signalRadius = mWidth / 2 / signalSize;
        for (int i = 0; i < signalSize; i++) {

            if (i > signalSize - scale) {
                float radius = signalRadius * i;
                rect = new RectF(radius, radius, mWidth - radius, mWidth - radius);
                if (i < signalSize - 1) {
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(rect, -135, 90, false, mPaint);
                } else {
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawArc(rect, -135, 90, true, mPaint);
                }
            }

        }

//        canvas.drawText("正在加载WIFI...",135,30,mPaint);
        //把当前画布返回（调整）到上一个save()状态之前
        canvas.restore();
    }

    /**
     * View Animation包括Tween Animation（补间动画）和Frame Animation(逐帧动画)； Property Animator包括ValueAnimator和ObjectAnimation；
     * <p>
     * 动画类的命名不同：View Animation中动画类取名都叫XXXXAnimation,而在Property Animator中动画类的取名则叫XXXXAnimator
     */
    public void startAnim() {
        if (mOnWifiStateMsg != null) {
            mOnWifiStateMsg.wifiStateMsg(WIFI_STATE_PROCESS);
        }

        startViewAnim(0f, 1.0f, 4000);
    }

    public void stopAnim() {
        if (mOnWifiStateMsg != null) {

            mOnWifiStateMsg.wifiStateMsg(WIFI_STATE_SUCCESS);
        }
        if (valueAnimator != null) {
            /**
             * 也就是说，如果我们调用了setFillAfter(true)，动画结束的时候就不会调用clearAnimation()，mCurrentAnimation就不会被置空了。在我们实现的动画里，确实调用了setFillAfter(true)。。。
             总结：做动画时，如果调用了setFillAfter(true)，动画结束后如果要设置该view的可见性，需要先调用一次clearAnimation()。
             */
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();//用于取消动画
            valueAnimator.end();
//            mAnimatedValue = 0.9f;
            mAnimatedValue = 0.0f;
            valueAnimator.removeAllUpdateListeners();

            /**
             * 在这么多线程当中，把主要是负责控制UI界面的显示、更新和控件交互的线程称为UI线程，由于onCreate()方法是由UI线程执行的，所以也可以把UI线程理解为主线程。其余的线程可以理解为工作者线程。
             invalidate()得在UI线程中被调动，在工作者线程中可以通过Handler来通知UI线程进行界面更新。
             而postInvalidate()在工作者线程中被调用
             */
            postInvalidate();
        }
    }


    private void startViewAnim(float startF, final float endF, long time) {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(startF, endF);
        }


        valueAnimator.removeAllUpdateListeners();
        valueAnimator.setDuration(time);//动画执行的时间
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        //设置重复模式：逆向重复(倒序重新开始)  设置动画重复的模式，包含REVERSE（逆向重复）和RESTART（重新开始）
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        //就是监听动画的实时变化状态
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 获取ValueAnimator在运动时，当前运动点的值
                 *  valueAnimator.getAnimatedValue() 是Object 类型
                 */
                mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        //主要是监听Animation的四个状态，
        valueAnimator.addListener(new Animator.AnimatorListener() {
            //当动画开始时
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束时调用
            @Override
            public void onAnimationEnd(Animator animation) {
//                LogUtil.e(TAG,"onAnimationEnd  当动画结束时调用");
            }

            //当动画取消时
            @Override
            public void onAnimationCancel(Animator animation) {
            }

            //当动画重复时
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    public void setValueAnimator() {
        valueAnimator = null;
    }

    OnWifiStateMsg mOnWifiStateMsg;

    public interface OnWifiStateMsg {
        void wifiStateMsg(int mState);
    }

    public void setWifiStateMsg(OnWifiStateMsg mOnWifiStateMsg) {
        this.mOnWifiStateMsg = mOnWifiStateMsg;
    }


}
