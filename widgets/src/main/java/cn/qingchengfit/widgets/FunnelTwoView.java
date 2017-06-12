package cn.qingchengfit.widgets;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.util.ArrayList;
import java.util.List;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/13.
 */
public class FunnelTwoView extends View
        implements ValueAnimator.AnimatorUpdateListener {

    public static float ANGLE_SCALE = 1.85f;
    private int maxMoney;
    private float phaseX = 1f;
    private int textAlpha = 255;
    private float mLastX;
    private float mLastY;
    private float mLastWidth;
    private int maxHight;
    private Path mPath;
    private Paint mPaintLine;
    private Paint mPaintText;
    private ObjectAnimator xAnimator;
    private ObjectAnimator alphaAnimator;
    private List<Float> datas = new ArrayList<>();
    private ArrayList<Paint> mPaints = new ArrayList<>();
    private ArrayList<Paint> mPaintsLine = new ArrayList<>();
    private ArrayList<Paint> mPaintsText = new ArrayList<>();
    private ArrayList<String> colors = new ArrayList<>();

    private ArrayList<Float> mPathHeights = new ArrayList<>();
    private ArrayList<Float> mPathAngleWidths = new ArrayList<>();

    private float mTotalHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300,
            getResources().getDisplayMetrics());

    private float maxLineH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300,
            getResources().getDisplayMetrics());
    private float minLineH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
            getResources().getDisplayMetrics());

    private float lineStartOffsetX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
            getResources().getDisplayMetrics());
    private float textStartOffsetX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7,
            getResources().getDisplayMetrics());

    private float maxWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 164,
            getResources().getDisplayMetrics());

    private float startOffsetX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
            getResources().getDisplayMetrics());
    private float startOffsetY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
            getResources().getDisplayMetrics());

    private float offsetX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
            getResources().getDisplayMetrics());
    private float offsetY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 62,
            getResources().getDisplayMetrics());
    private float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
            getResources().getDisplayMetrics());

    private float lineOffset_30 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
            getResources().getDisplayMetrics());
    private float lineOffset_3 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
            getResources().getDisplayMetrics());
    private float lineOffset_20 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
            getResources().getDisplayMetrics());
    private float lineOffset_24 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
            getResources().getDisplayMetrics());
    //private float lineOffset_1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
    //        getResources().getDisplayMetrics());
    private float lineOffset_2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
            getResources().getDisplayMetrics());
    private float lineOffset_4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
            getResources().getDisplayMetrics());
    //private float lineOffset_9 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9,
    //        getResources().getDisplayMetrics());
    //private float lineOffset_21 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21,
    //        getResources().getDisplayMetrics());
    private float lineOffset_58 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 58,
            getResources().getDisplayMetrics());

    public FunnelTwoView(Context context) {
        this(context, null);
    }

    public FunnelTwoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunnelTwoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static float getAngleScale() {
        return ANGLE_SCALE;
    }

    public static void setAngleScale(float angleScale) {
        ANGLE_SCALE = angleScale;
    }

    //private void calculate() {
    //    mPathHeights.clear();
    //    mPathAngleWidths.clear();
    //    for (int i = 0; i < datas.size(); i++) {
    //        Float mPathHeight = 124f;
    //        mPathHeights.add(i, mPathHeight);
    //        Float mPathAngleWidth = mPathHeight / ANGLE_SCALE;
    //        mPathAngleWidths.add(i, mPathAngleWidth);
    //    }
    //}

    public void setData(List<Float> datas) {
        this.datas = datas;
        stopAnimator();
        init();
        //calculate();
        getMaxHight();
        requestLayout();
    }

    private void getMaxHight() {
        maxHight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 210,
                getResources().getDisplayMetrics());
    }

    private void init() {
        this.colors.add("#6eb8f1");
        this.colors.add("#f9944e");
        this.colors.add("#0db14b");
        mPaints.clear();
        for (int i = 0; i < datas.size(); i++) {
            Paint mPaint = new Paint();
            mPaint.setColor(Color.parseColor(colors.get(i)));
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setDither(true);
            mPaint.setAntiAlias(true);
            if (i == 2) {
                mPaint.setAlpha(75);
            } else {
                mPaint.setAlpha(80);
            }
            mPaints.add(mPaint);

            mPaintLine = new Paint();
            mPaintLine.setColor(Color.parseColor(colors.get(i)));
            mPaintLine.setStyle(Paint.Style.FILL);
            mPaintLine.setStrokeWidth(4);
            mPaintsLine.add(mPaintLine);

            mPaintText = new Paint();
            mPaintText.setColor(Color.parseColor(colors.get(i)));
            mPaintText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                    getResources().getDisplayMetrics()));
            mPaintText.setAntiAlias(true);
            mPaintText.setTextAlign(Paint.Align.CENTER);
            mPaintsText.add(mPaintText);
        }
    }

    private void draw2(Canvas canvas, Paint mPaint, int i) {
        if (i == 0) {
            mLastX = startOffsetX;
            mLastY = startOffsetY;
            mLastWidth = maxWidth;
        }
        mPath = new Path();
        mPath.moveTo(mLastX, mLastY);
        mPath.lineTo(mLastX + mLastWidth, mLastY);
        mPath.lineTo(mLastX + mLastWidth - offsetX, mLastY + offsetY);
        mPath.lineTo(mLastX + offsetX, mLastY + offsetY);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mLastWidth = mLastWidth - 2 * offsetX;
        mLastX = mLastX + offsetX;
        mLastY = mLastY + offsetY + margin;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 3; i++) {
            draw2(canvas, mPaints.get(i), i);
            switch (i) {
                case 0:
                    //drawLine1(canvas, mPaintsLine.get(0));
                    drawLine2(canvas, mPaintsLine.get(0),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 35,
                                    getResources().getDisplayMetrics()),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25,
                                    getResources().getDisplayMetrics()));
                    drawText1(canvas, mPaintsText.get(0));
                    break;
                case 1:
                    drawLine2(canvas, mPaintsLine.get(1),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50,
                                    getResources().getDisplayMetrics()),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 107,
                                    getResources().getDisplayMetrics()));
                    drawText2(canvas, mPaintsText.get(1));
                    break;
                case 2:
                    drawLine3(canvas, mPaintsLine.get(2),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 210,
                                    getResources().getDisplayMetrics()),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30,
                                    getResources().getDisplayMetrics()));
                    drawText3(canvas, mPaintsText.get(2));
                    break;
            }
        }
    }

    private void drawLine2(Canvas canvas, Paint mPaint, float startX,
            float startY) {// (35,25) (45,107)
        mPath = new Path();
        mPath.moveTo(startX, startY);
        mPath.lineTo(startX - lineOffset_30, startY);

        //mPath.lineTo(startX - lineOffset_30 + lineOffset_3, startY + lineOffset_24);
        //
        //mPath.moveTo(startX - lineOffset_30 + lineOffset_3 + lineOffset_3,
        //        startY + lineOffset_24 + lineOffset_20);

        mPath.lineTo(startX - lineOffset_30 + lineOffset_3 + lineOffset_3 + lineOffset_3,
                startY + lineOffset_24 + lineOffset_20 + lineOffset_24);

        mPath.lineTo(
                startX - lineOffset_30 + lineOffset_3 + lineOffset_3 + lineOffset_3 + lineOffset_30,
                startY + lineOffset_24 + lineOffset_20 + lineOffset_24);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);

        mPath = new Path();
        mPath.moveTo(
                startX - lineOffset_30 + lineOffset_3 + lineOffset_3 + lineOffset_3 + lineOffset_30,
                startY + lineOffset_24 + lineOffset_20 + lineOffset_24);
        mPath.lineTo(
                startX - lineOffset_30 + lineOffset_3 + lineOffset_3 + lineOffset_3 + lineOffset_30,
                startY + lineOffset_24 + lineOffset_20 + lineOffset_24 - lineOffset_2);
        mPath.lineTo(startX - lineOffset_30
                + lineOffset_3
                + lineOffset_3
                + lineOffset_3
                + lineOffset_30
                + lineOffset_4, startY + lineOffset_24 + lineOffset_20 + lineOffset_24);
        mPath.lineTo(
                startX - lineOffset_30 + lineOffset_3 + lineOffset_3 + lineOffset_3 + lineOffset_30,
                startY + lineOffset_24 + lineOffset_20 + lineOffset_24 + lineOffset_2);
        mPath.close();
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawLine3(Canvas canvas, Paint mPaint, float startX, float startY) {
        mPath = new Path();
        mPath.moveTo(startX, startY);
        mPath.lineTo(startX + lineOffset_30, startY);

        //mPath.lineTo(startX + lineOffset_30 - lineOffset_3 * 2, startY + lineOffset_58);
        //
        //mPath.moveTo(startX + lineOffset_30 - lineOffset_3 * 2 - lineOffset_3 * 2,
        //        startY + lineOffset_58 + lineOffset_20);

        mPath.lineTo(
                startX + lineOffset_30 - lineOffset_3 * 2 - lineOffset_3 * 2 - lineOffset_3 * 2,
                startY + lineOffset_58 + lineOffset_20 + lineOffset_58);

        mPath.lineTo(startX + lineOffset_30
                - lineOffset_3 * 2
                - lineOffset_3 * 2
                - lineOffset_3 * 2
                - lineOffset_30, startY + lineOffset_58 + lineOffset_20 + lineOffset_58);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);

        mPath = new Path();
        mPath.moveTo(startX + lineOffset_30
                - lineOffset_3 * 2
                - lineOffset_3 * 2
                - lineOffset_3 * 2
                - lineOffset_30, startY + lineOffset_58 + lineOffset_20 + lineOffset_58);
        mPath.lineTo(startX + lineOffset_30
                        - lineOffset_3 * 2
                        - lineOffset_3 * 2
                        - lineOffset_3 * 2
                        - lineOffset_30,
                startY + lineOffset_58 + lineOffset_20 + lineOffset_58 - lineOffset_2);
        mPath.lineTo(startX + lineOffset_30
                - lineOffset_3 * 2
                - lineOffset_3 * 2
                - lineOffset_3 * 2
                - lineOffset_30
                - lineOffset_4, startY + lineOffset_58 + lineOffset_20 + lineOffset_58);
        mPath.lineTo(startX + lineOffset_30
                        - lineOffset_3 * 2
                        - lineOffset_3 * 2
                        - lineOffset_3 * 2
                        - lineOffset_30,
                startY + lineOffset_58 + lineOffset_20 + lineOffset_58 + lineOffset_2);
        mPath.close();
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawText1(Canvas canvas, Paint mPaint) {
        String rate = "";
        String count = "";
        if (datas != null && !datas.isEmpty() && datas.size() > 2) {
            if (datas.get(0) == 0) {
                rate = "0%";
            } else {
                rate = String.valueOf((int) ((datas.get(1) / datas.get(0)) * 100)) + "%";
            }
            count = new StringBuilder().append("注册").append(String.valueOf(datas.get(0).intValue()))
                    .append("人")
                    .toString();
        }
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setAntiAlias(true);
        canvas.drawRect(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f,
                        getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35f,
                        getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70f,
                        getResources().getDisplayMetrics()), paint);
        //canvas.drawColor(Color.WHITE);
        canvas.drawText(rate, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65,
                        getResources().getDisplayMetrics()), mPaint);
        canvas.drawText(count, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 122,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42,
                        getResources().getDisplayMetrics()), mPaint);
    }

    private void drawText2(Canvas canvas, Paint mPaint) {
        String rate = "";
        String count = "";
        if (datas != null && !datas.isEmpty() && datas.size() > 2) {
            if (datas.get(1) == 0) {
                rate = "0%";
            } else {
                rate = String.valueOf((int) ((datas.get(2) / datas.get(1)) * 100)) + "%";
            }
            count = new StringBuilder().append("接洽").append(String.valueOf(datas.get(1).intValue()))
                    .append("人")
                    .toString();
        }

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setAntiAlias(true);
        canvas.drawRect(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 134f,
                        getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f,
                        getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 154f,
                        getResources().getDisplayMetrics()), paint);
        //canvas.drawColor(Color.WHITE);
        canvas.drawText(rate, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 149,
                        getResources().getDisplayMetrics()), mPaint);
        canvas.drawText(count, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 122,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110,
                        getResources().getDisplayMetrics()), mPaint);
    }

    private void drawText3(Canvas canvas, Paint mPaint) {
        String rate = "";
        String count = "";
        if (datas != null && !datas.isEmpty() && datas.size() > 2) {
            if (datas.get(0) == 0) {
                rate = "0%";
            } else {
                rate = String.valueOf((int) ((datas.get(2) / datas.get(0)) * 100)) + "%";
            }
            count = new StringBuilder().append("会员").append(String.valueOf(datas.get(2).intValue()))
                    .append("人")
                    .toString();
        }

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setAntiAlias(true);
        canvas.drawRect(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 215f,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90f,
                        getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 245f,
                        getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110f,
                        getResources().getDisplayMetrics()), paint);

        canvas.drawText(rate, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105,
                        getResources().getDisplayMetrics()), mPaint);
        canvas.drawText(count, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 122,
                getResources().getDisplayMetrics()),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 176,
                        getResources().getDisplayMetrics()), mPaint);
    }

    public void animateY() {
        xAnimator = ObjectAnimator.ofFloat(this, "phaseX", 0, 1);
        xAnimator.setDuration(2500);
        xAnimator.addUpdateListener(this);
        xAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        xAnimator.start();

        alphaAnimator = ObjectAnimator.ofInt(this, "textAlpha", 0, 255);
        alphaAnimator.setDuration(2000);
        alphaAnimator.addUpdateListener(this);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        //alphaAnimator.start();
    }

    public void stopAnimator() {
        if (xAnimator != null && alphaAnimator != null) {
            if (xAnimator.isRunning()) {
                xAnimator.end();
                alphaAnimator.end();
            }
        }
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 244,
                getResources().getDisplayMetrics()), getMeasuredLength(heightMeasureSpec, false));
    }

    private int getMeasuredLength(int length, boolean isWidth) {
        int specMode = MeasureSpec.getMode(length);
        int specSize = MeasureSpec.getSize(length);
        int size;
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else {
            size = maxHight;
            //+(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
            Log.i("FunnelMain", "onMeasure:" + size);
        }
        return size;
    }

    public float getPhaseX() {
        return phaseX;
    }

    public void setPhaseX(float phaseX) {
        this.phaseX = phaseX;
    }

    public int getTextAlpha() {
        return textAlpha;
    }

    public void setTextAlpha(int textAlpha) {
        this.textAlpha = textAlpha;
    }

    @Override public void onAnimationUpdate(ValueAnimator animation) {
        Log.i("Animation", "onAnimationUpdate:动画更新....");
        //calculate();
        postInvalidate();
    }

    @Override protected void onAnimationEnd() {
        super.onAnimationEnd();
        Log.i("Animation", "onAnimationUpdate:动画结束....");
        maxHight = 0;
    }
}
