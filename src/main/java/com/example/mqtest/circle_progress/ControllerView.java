package com.example.mqtest.circle_progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.mqtest.circle_progress.Date.Transparent;

/**
 * Created by Administrator on 2018/three/19.
 */

public class ControllerView extends View {
    float mPro_line;
    int mPro_color;
    int mComplete_color;

    float upper_txt_size;
    float upper_txt_change_size;

    float down_txt_size;
    float down_txt_change_size;

    float pro_circle_radio;
    float click_circle_radio;

    float mCircleRadio;

    Context mContext;

    Paint mTextPaint;
    Paint mDownTextPaint;
    Paint mLinePaint;
    Path mPathLine;
    Paint mCirclePaint;

    Paint mBitPaint;

    int viewWidth;
    int viewHeigth;

    int mStep = 4;
    int mCurrentStep = 3;

    ImageView imageViewBtn;

    Bitmap bitmap;
    Bitmap[] bitmaps;
    Bitmap bitmap1;
    int bitHeight = 0;
    int bitWeight = 0;

    float mProX;
    float mProY;

    String[] upperText = {"开始", "第二步", "第三步", "完成"};
    String[] downText = {"开始", "请将摇杆置中放置", "请将摇杆置中放置", "已完成校准"};

    //文字离圆线距离
    int distanceByDescription ;

    //定义一个接口对象
    private OnItemClickListener listener1;
    //获得接口对象的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener1 = listener;
    }
    //定义一个接口
    public interface OnItemClickListener {
        void onItemselect();
    }


    public ControllerView(Context context) {
        this(context, null);
        mContext = context;
    }

    public ControllerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public ControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        distanceByDescription = Transparent.dip2px(mContext, 50);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ControllerView, defStyleAttr, 0);
        //颜色变化
        mPro_color = array.getColor(R.styleable.ControllerView_pro_color, getResources().getColor(R.color.colorGrey));
        mComplete_color = array.getColor(R.styleable.ControllerView_complete_color, getResources().getColor(R.color.colorblue));
        //上面文字大小
        upper_txt_size = array.getDimension(R.styleable.ControllerView_upper_txt_size, Transparent.dip2px(mContext, 13));
        upper_txt_change_size = array.getDimension(R.styleable.ControllerView_upper_txt_change_size, Transparent.dip2px(mContext, 23));
        //下面文字大小
        down_txt_size = array.getDimension(R.styleable.ControllerView_down_txt_size, Transparent.dip2px(mContext, 10));
        down_txt_change_size = array.getDimension(R.styleable.ControllerView_down_txt_change_size, Transparent.dip2px(mContext, 20));
        //圆大小
        pro_circle_radio = array.getDimension(R.styleable.ControllerView_pro_circle_size, Transparent.dip2px(mContext, 5));
        click_circle_radio = array.getDimension(R.styleable.ControllerView_click_circle_size, Transparent.dip2px(mContext, 10));
        initView();
    }

    private void initView() {
        bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.one);
        bitmap = bitmap1;
        bitHeight = bitmap1.getHeight();
        bitWeight = bitmap1.getWidth();

        //bitmap = bitmap1;
        int lineWidth = Transparent.dip2px(mContext, 2);
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);

        mPathLine = new Path();

        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(lineWidth);
        mLinePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);

        mBitPaint = new Paint();

        mDownTextPaint = new Paint();
        mDownTextPaint.setStyle(Paint.Style.FILL);

        mPictureProStatus();

    }

    //初始状态
    private void mPictureProStatus() {
        mCirclePaint.setColor(mComplete_color);
        mLinePaint.setColor(mComplete_color);
        mTextPaint.setColor(mComplete_color);
        mTextPaint.setTextSize(upper_txt_size);
        mDownTextPaint.setColor(mPro_color);
        mDownTextPaint.setTextSize(down_txt_size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureCaulation(widthMeasureSpec, 0), measureCaulation(heightMeasureSpec, 1));
    }

    private int measureCaulation(int measureSpec, int type) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //matchParent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        /*else if (specMode == MeasureSpec.AT_MOST) {

        } */
        else {
            if (type == 0) {
                result = getPaddingLeft() + getPaddingRight();
            } else {
                result = getPaddingTop() + getPaddingBottom();
            }
        }
        if (type == 0) {
            viewWidth = result;
        } else {
            viewHeigth = result;
            mPro_line = viewHeigth / 5;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPictureProStatus();

        mProX = viewWidth / 3;
        mProY = 6 * pro_circle_radio;
        //添加图片
        canvas.drawBitmap(bitmap, mProX - bitWeight - click_circle_radio, mProY - bitHeight / 2 + (mCurrentStep - 1) * mPro_line, mBitPaint);
        mLinePaint.setColor(mComplete_color);
        canvas.drawLine(mProX, mProY, mProX, mProY + (mCurrentStep - 1) * mPro_line, mLinePaint);
        if (mCurrentStep < mStep) {
            mLinePaint.setColor(mPro_color);
            canvas.drawLine(mProX, mProY + (mCurrentStep - 1) * mPro_line, mProX, mProY + (mStep - 1) * mPro_line, mLinePaint);
        }

        for (int i=0;i < mStep;i++) {
            if (mCurrentStep >= 0 && mCurrentStep <= mStep) {

                if (i == (mCurrentStep - 1)) {
                    mCircleRadio = click_circle_radio;
                    mTextPaint.setTextSize(upper_txt_change_size);
                    mDownTextPaint.setTextSize(down_txt_change_size);
                }else {
                    mCircleRadio = pro_circle_radio;
                    mTextPaint.setTextSize(upper_txt_size);
                    mDownTextPaint.setTextSize(down_txt_size);
                }
                if (i > (mCurrentStep - 1)) {
                    mCirclePaint.setColor(mPro_color);
                    mTextPaint.setColor(mPro_color);
                }
            }

            canvas.drawCircle(mProX, mProY + i * mPro_line, mCircleRadio, mCirclePaint);
            canvas.drawText(upperText[i], mProX + distanceByDescription, mProY + mTextPaint.getTextSize() / 3 + i * mPro_line, mTextPaint);
            canvas.drawText(downText[i], mProX + distanceByDescription, mProY + mTextPaint.getTextSize() + mDownTextPaint.getTextSize() / 3 + i * mPro_line, mDownTextPaint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xTouch = 0;
        float yTouch = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xTouch = event.getX();
                yTouch = event.getY();

            case MotionEvent.ACTION_MOVE:
                xTouch = event.getX();
                yTouch = event.getY();

            case MotionEvent.ACTION_UP:
                if (mCurrentStep > 0) {
                    if (xTouch >= mProX - bitWeight - click_circle_radio && xTouch <= mProX
                            && yTouch >= mProY - bitHeight / 2 + (mCurrentStep - 1) * mPro_line && yTouch <= mProY + bitHeight / 2 + (mCurrentStep - 1) * mPro_line) {
                        if (listener1 != null) {
                            listener1.onItemselect();
                        }
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
