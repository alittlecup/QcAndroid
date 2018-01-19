package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import cn.qingchengfit.utils.MeasureUtils;
import java.util.ArrayList;
import java.util.List;

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

public class QcFilterToggle extends CompoundButton implements QcCheckable {
    List<CompoundButton.OnCheckedChangeListener> listeners = new ArrayList<>();
  private int baseline;
  private boolean mChecked;
  private Drawable buttonDrawableOn;
  private Drawable buttonDrawableOff;
  private Paint mTextPaint;
  private String textOn = "";
  private String textOff = "";
  private int colorOn;
  private int colorOff;
  private float textLenth, textHeight, textSize;
  private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

  public QcFilterToggle(Context context) {
    this(context, null, 0);
  }

  public QcFilterToggle(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

    public QcFilterToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QcFilterToggle, defStyleAttr, 0);
        int dr = a.getResourceId(R.styleable.QcFilterToggle_ft_vc_drawable_on,-1);
        if (dr > 0){
            buttonDrawableOn = ContextCompat.getDrawable(getContext(),dr).mutate();
        }
        int dr2 = a.getResourceId(R.styleable.QcFilterToggle_ft_vc_drawable_off,-1);
        if (dr2 > 0){
            buttonDrawableOff = ContextCompat.getDrawable(getContext(),dr2).mutate();
        }
        textOn = a.getString(R.styleable.QcFilterToggle_ft_text_on);
        textOff = a.getString(R.styleable.QcFilterToggle_ft_text_off);
        colorOn = a.getColor(R.styleable.QcFilterToggle_ft_color_on, Color.BLACK);
        colorOff = a.getColor(R.styleable.QcFilterToggle_ft_color_off, Color.GRAY);
        mChecked = a.getBoolean(R.styleable.QcFilterToggle_ft_checked,false);
        textSize = a.getDimension(R.styleable.QcFilterToggle_ft_text_size,getResources().getDimension(R.dimen.common_font));
        a.recycle();
        if (buttonDrawableOn != null)
          DrawableCompat.setTint(buttonDrawableOn,colorOn);
        if (buttonDrawableOff != null)
          DrawableCompat.setTint(buttonDrawableOff,colorOff);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        if (TextUtils.isEmpty(textOn))
            textOn = textOff;
        if (TextUtils.isEmpty(textOff))
            textOff = textOn;
        setClickable(true);

    }

  public void setButtonDrawableOn(Drawable buttonDrawableOn) {
    this.buttonDrawableOn = buttonDrawableOn;
  }

  public void setButtonDrawableOff(Drawable buttonDrawableOff) {
    this.buttonDrawableOff = buttonDrawableOff;
  }

  public void setStyle(@StyleRes int resId) {
    final TypedArray a = getContext().obtainStyledAttributes(resId, R.styleable.QcFilterToggle);
    int dr = a.getResourceId(R.styleable.QcFilterToggle_ft_vc_drawable_on, -1);
    if (dr > 0) {
      buttonDrawableOn = ContextCompat.getDrawable(getContext(), dr);
    }
    int dr2 = a.getResourceId(R.styleable.QcFilterToggle_ft_vc_drawable_off, -1);
    if (dr2 > 0) {
      buttonDrawableOff = ContextCompat.getDrawable(getContext(), dr2);
    }
    textOn = a.getString(R.styleable.QcFilterToggle_ft_text_on);
    textOff = a.getString(R.styleable.QcFilterToggle_ft_text_off);
    colorOn = a.getColor(R.styleable.QcFilterToggle_ft_color_on, Color.BLACK);
    colorOff = a.getColor(R.styleable.QcFilterToggle_ft_color_off, Color.GRAY);
    mChecked = a.getBoolean(R.styleable.QcFilterToggle_ft_checked, false);
    textSize = a.getDimension(R.styleable.QcFilterToggle_ft_text_size,
        getResources().getDimension(R.dimen.common_font));
    a.recycle();
    mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setTextSize(textSize);
    if (TextUtils.isEmpty(textOn)) textOn = textOff;
    if (TextUtils.isEmpty(textOff)) textOff = textOn;
    setClickable(true);
  }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.restore();
      String txt = mChecked ? textOn : textOff;
        if (txt == null) txt = "";textLenth = mTextPaint.measureText(txt);
      if (textLenth > getWidth() - MeasureUtils.dpToPx(20f, getResources()) - (
          buttonDrawableOn == null ? 0 : buttonDrawableOn.getIntrinsicWidth())) {
        int ind = (int) ((getWidth()
            - MeasureUtils.dpToPx(20f, getResources())
            - (buttonDrawableOn == null ? 0 : buttonDrawableOn.getIntrinsicWidth())) / textLenth * txt.length());
        txt = txt.substring(0, ind).concat("…");
      }

        textHeight = mTextPaint.getFontMetrics().descent-mTextPaint.getFontMetrics().ascent;
        float topText = (getMeasuredHeight() - mTextPaint.getFontMetrics().bottom + mTextPaint.getFontMetrics().top) / 2 - mTextPaint.getFontMetrics().top;
        mTextPaint.setColor(mChecked?colorOn:colorOff);
        float leftText = canvas.getWidth()/2- getRightPadding()-textLenth/2;
        if (leftText < 0) leftText = 0 ;
        if (topText < 0 ) topText = 0;
      canvas.drawText(txt, leftText, topText, mTextPaint);
        if (mChecked && buttonDrawableOn != null) {
            final int drawableHeight = buttonDrawableOn.getIntrinsicHeight();
            final int drawableWidth = buttonDrawableOn.getIntrinsicWidth();
            int top = canvas.getHeight()/2-drawableHeight/2;
            if (top < 0) top =0;
            buttonDrawableOn.setBounds((int)(leftText+textLenth+10),top,(int)(leftText+textLenth+10+drawableWidth),top+drawableHeight);
            buttonDrawableOn.draw(canvas);
        }else if (buttonDrawableOff != null){
            final int drawableHeight = buttonDrawableOff.getIntrinsicHeight();
            final int drawableWidth = buttonDrawableOff.getIntrinsicWidth();
            int top = canvas.getHeight()/2-drawableHeight/2;
            if (top < 0) top =0;
            buttonDrawableOff.setBounds((int)(leftText+textLenth+10),top,(int)(leftText+textLenth+10+drawableWidth),top+drawableHeight);
            buttonDrawableOff.draw(canvas);
        }
    }

  public float getRightPadding() {
    return buttonDrawableOff == null ? 0 : buttonDrawableOff.getIntrinsicWidth();
  }

  @Override
    public boolean performClick() {
    if (!hasOnClickListeners()) {
      toggle();
    }
    return super.performClick();
  }

  @Override
    public boolean isChecked() {
    return mChecked;
  }

  @Override public void setChecked(boolean checked) {
    mChecked = checked;
    if (mTextPaint != null) {
      mTextPaint.setColor(mChecked ? colorOn : colorOff);
      if (onCheckedChangeListener != null) onCheckedChangeListener.onCheckedChanged(null, mChecked);
      invalidate();
    }
  }

    @Override public void toggle() {
        mChecked = !mChecked;
        mTextPaint.setColor(mChecked?colorOn:colorOff);
      if (onCheckedChangeListener != null) {
        onCheckedChangeListener.onCheckedChanged(this, mChecked);
      }
      for (CompoundButton.OnCheckedChangeListener listener : listeners) {
        listener.onCheckedChanged(this, mChecked);
      }
        invalidate();
    }

    public CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

  public void setText(String s) {
    textOff = textOn = s;
    invalidate();
  }

  public void setText(String s, boolean status) {
    if (status) {
      textOn = s;
    } else {
      textOff = s;
    }
    invalidate();
  }

    public void setTextColorRes(@ColorRes int color) {
        colorOff = ContextCompat.getColor(getContext(), color);
        invalidate();
  }

  @Override public void addCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
    listeners.add(listener);
  }

  @Override public boolean isOrContainCheck(View v) {
    return v.getId() == this.getId();
  }
}
