package cn.qingchengfit.saasbase.mvvm_student;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by huangbaole on 2017/12/4.
 */

public class QcToggleButton extends android.support.v7.widget.AppCompatCheckedTextView {

    private boolean mChecked;
    private Drawable buttonDrawable;
    private Paint mTextPaint;
    private String text;
    private int colorOn;
    private int colorOff;
    private float textLenth, textHeight, textSize;

    public QcToggleButton(Context context) {
        this(context, null, 0);
    }

    public QcToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QcToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, cn.qingchengfit.widgets.R.styleable.QcToggleButtom, defStyleAttr, 0);
        int dr = a.getResourceId(cn.qingchengfit.widgets.R.styleable.QcToggleButtom_qc_vc_drawable, -1);
        if (dr > 0) {
            buttonDrawable = ContextCompat.getDrawable(getContext(), dr);
        }
        if (buttonDrawable != null)
            buttonDrawable = buttonDrawable.mutate();
        text = a.getString(cn.qingchengfit.widgets.R.styleable.QcToggleButtom_qc_text);
        colorOn = a.getColor(cn.qingchengfit.widgets.R.styleable.QcToggleButtom_qc_color_on, Color.BLACK);
        colorOff = a.getColor(cn.qingchengfit.widgets.R.styleable.QcToggleButtom_qc_color_off, Color.GRAY);
        mChecked = a.getBoolean(cn.qingchengfit.widgets.R.styleable.QcToggleButtom_qc_checked, false);
        textSize = a.getDimension(cn.qingchengfit.widgets.R.styleable.QcToggleButtom_qc_text_size, getResources().getDimension(cn.qingchengfit.widgets.R.dimen.common_font));
        a.recycle();
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);

        setClickable(true);
    }



    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.save();
//        canvas.restore();
//        textLenth = mTextPaint.measureText(text);
//        textHeight = mTextPaint.getFontMetrics().descent - mTextPaint.getFontMetrics().ascent;
//        float topText = (getMeasuredHeight() - mTextPaint.getFontMetrics().bottom + mTextPaint.getFontMetrics().top) / 2 - mTextPaint.getFontMetrics().top;
//        mTextPaint.setColor(isChecked() ? colorOn : colorOff);
//        float leftText = canvas.getWidth() / 2 - getRightPadding() - textLenth / 2;
//        if (leftText < 0) leftText = 0;
//        if (topText < 0) topText = 0;
//        canvas.drawText(text, leftText, topText, mTextPaint);
//        if (buttonDrawable != null) {
//            DrawableCompat.setTint(buttonDrawable, isChecked() ? colorOn : colorOff);
//            final int drawableHeight = buttonDrawable.getIntrinsicHeight();
//            final int drawableWidth = buttonDrawable.getIntrinsicWidth();
//            int top = canvas.getHeight() / 2 - drawableHeight / 2;
//            if (top < 0) top = 0;
//            buttonDrawable.setBounds((int) (leftText + textLenth + 10), top, (int) (leftText + textLenth + 10 + drawableWidth), top + drawableHeight);
//            buttonDrawable.draw(canvas);
//        }
        super.onDraw(canvas);
        DrawableCompat.setTint(buttonDrawable, isChecked() ? colorOn : colorOff);
        if (isChecked()) {
            setTextColor(colorOn);
        } else {
            setTextColor(colorOff);
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, buttonDrawable, null);
        setCompoundDrawablePadding(10);
    }
}
