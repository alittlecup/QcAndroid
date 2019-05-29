package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import cn.qingchengfit.utils.MeasureUtils;

/**
 * Created by fb on 2017/3/20.
 */

public class CircleTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private int color;
    private RectF rectF;
    private float borderWidth;
    private String content;
    private float contentSize;
    private Rect bounds = new Rect();

    public CircleTextView(Context context) {
        super(context);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setColor(int color) {
        this.color = color;
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleBackground);
        color = typedArray.getColor(R.styleable.CircleBackground_border_color, ContextCompat.getColor(context,R.color.qc_allotsale_green) );
        borderWidth = typedArray.getDimension(R.styleable.CircleBackground_border_width, MeasureUtils
            .dpToPx(3f, context.getResources()));
        content = typedArray.getString(R.styleable.CircleBackground_circle_content);
        contentSize = typedArray.getDimension(R.styleable.CircleBackground_contentSize, 13);
        typedArray.recycle();
        if (mPaint == null){
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
        if (rectF == null){
            rectF = new RectF();
        }
        mPaint.setTextSize(contentSize);
        mPaint.setColor(color);

    }

    @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float measureWidth = getPaint().measureText(String.valueOf(getText()));
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        float textSize = getTextSize();
        if (width * 0.85 < measureWidth) {
            textSize = (width * 0.85f / measureWidth) * textSize;
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.top = borderWidth;
        rectF.right = getMeasuredWidth() - borderWidth;
        rectF.left = borderWidth;
        rectF.bottom = getMeasuredHeight() - borderWidth ;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        canvas.drawArc(rectF, 45, -270, false, mPaint);
        mPaint.getTextBounds(content, 0, content.length(), bounds);
        mPaint.setStrokeWidth(1f);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(content, getMeasuredWidth()/2 - bounds.width()/2, getMeasuredHeight() * 0.85f, mPaint);
    }
}
