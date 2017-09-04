package cn.qingchengfit.widgets;

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
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/3/15.
 */

public class QcToggleButton extends View implements Checkable{
    private int baseline;
    private boolean mChecked;
    private Drawable buttonDrawable;
    private Paint mTextPaint;
    private String text;
    private int colorOn;
    private int colorOff;
    private float textLenth,textHeight,textSize;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public QcToggleButton(Context context) {
        this(context,null,0);
    }

    public QcToggleButton(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public QcToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QcToggleButtom, defStyleAttr, 0);
        int dr = a.getResourceId(R.styleable.QcToggleButtom_qc_vc_drawable,-1);
        if (dr > 0){
            buttonDrawable = ContextCompat.getDrawable(getContext(),dr);
        }
        if (buttonDrawable != null)
            buttonDrawable = buttonDrawable.mutate();
        text = a.getString(R.styleable.QcToggleButtom_qc_text);
        colorOn = a.getColor(R.styleable.QcToggleButtom_qc_color_on, Color.BLACK);
        colorOff = a.getColor(R.styleable.QcToggleButtom_qc_color_off, Color.GRAY);
        mChecked = a.getBoolean(R.styleable.QcToggleButtom_qc_checked,false);
        textSize = a.getDimension(R.styleable.QcToggleButtom_qc_text_size,getResources().getDimension(R.dimen.common_font));
        a.recycle();
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);

        setClickable(true);

    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.restore();
        textLenth = mTextPaint.measureText(text);
        textHeight = mTextPaint.getFontMetrics().descent-mTextPaint.getFontMetrics().ascent;
        float topText = (getMeasuredHeight() - mTextPaint.getFontMetrics().bottom + mTextPaint.getFontMetrics().top) / 2 - mTextPaint.getFontMetrics().top;
        mTextPaint.setColor(mChecked?colorOn:colorOff);
        float leftText = canvas.getWidth()/2- getRightPadding()-textLenth/2;
        if (leftText < 0) leftText = 0 ;
        if (topText < 0 ) topText = 0;
        canvas.drawText(text,leftText,topText,mTextPaint);
        if (buttonDrawable != null) {
            DrawableCompat.setTint(buttonDrawable,mChecked?colorOn:colorOff);
            final int drawableHeight = buttonDrawable.getIntrinsicHeight();
            final int drawableWidth = buttonDrawable.getIntrinsicWidth();
            int top = canvas.getHeight()/2-drawableHeight/2;
            if (top < 0) top =0;
            buttonDrawable.setBounds((int)(leftText+textLenth+10),top,(int)(leftText+textLenth+10+drawableWidth),top+drawableHeight);
            buttonDrawable.draw(canvas);
        }
    }

    public float getRightPadding(){
        return buttonDrawable == null ? 0: buttonDrawable.getIntrinsicWidth();
    }

    public void setChangeColor(int colorChange){
        mTextPaint.setColor(colorChange);
        DrawableCompat.setTint(buttonDrawable, colorChange);
    }

    @Override public boolean performClick() {
        if (!hasOnClickListeners()) {
            toggle();
        }
        return super.performClick();
    }

    @Override public boolean isChecked() {
        return mChecked;
    }

    @Override public void setChecked(boolean checked) {
        mChecked = checked;
        invalidate();
    }

    @Override public void toggle() {
        mChecked = !mChecked;
        mTextPaint.setColor(mChecked?colorOn:colorOff);
        DrawableCompat.setTint(buttonDrawable,mChecked?colorOn:colorOff);
        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChanged(null,mChecked);
        invalidate();
    }

    public CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
}
