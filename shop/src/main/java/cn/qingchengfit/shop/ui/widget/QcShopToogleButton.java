package cn.qingchengfit.shop.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.QcCheckable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/26.
 */

public class QcShopToogleButton extends CompoundButton implements QcCheckable {
  private TextPaint mTextPaint;
  private boolean mChecked;
  private int checkedColor, unCheckedColor;
  private String checkedText, unCheckedText;
  private Drawable uncheckeDrawable;
  private Drawable checkedUpDrawable;
  private Drawable checkedDownDrawable;

  private boolean isArrowUp = true;

  @Override
  public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
    this.onCheckedChangeListener = onCheckedChangeListener;
  }

  private OnCheckedChangeListener onCheckedChangeListener;

  public QcShopToogleButton(Context context) {
    this(context, null, 0);
  }

  public QcShopToogleButton(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public QcShopToogleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.QcShopToogleButton, defStyleAttr, 0);
    int dr = a.getResourceId(R.styleable.QcShopToogleButton_unchecked_drawable, -1);
    if (dr > 0) {
      uncheckeDrawable = ContextCompat.getDrawable(getContext(), dr).mutate();
    }
    int dr2 = a.getResourceId(R.styleable.QcShopToogleButton_checked_up_drawable, -1);
    if (dr2 > 0) {
      checkedUpDrawable = ContextCompat.getDrawable(getContext(), dr2).mutate();
    }
    int dr3 = a.getResourceId(R.styleable.QcShopToogleButton_checked_down_drawable, -1);
    if (dr3 > 0) {
      checkedDownDrawable = ContextCompat.getDrawable(getContext(), dr3).mutate();
    }

    checkedText = a.getString(R.styleable.QcShopToogleButton_checked_text);
    unCheckedText = a.getString(R.styleable.QcShopToogleButton_unchecked_text);
    checkedColor = a.getColor(R.styleable.QcShopToogleButton_checked_color, Color.BLACK);
    unCheckedColor = a.getColor(R.styleable.QcShopToogleButton_unchecked_color, Color.GRAY);
    mChecked = a.getBoolean(R.styleable.QcShopToogleButton_chekced, false);

    mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setTextSize(MeasureUtils.dpToPx(14f, context.getResources()));
  }

  @Override public boolean performClick() {
    if (!hasOnClickListeners()) {
      toggle();
    }
    if (isChecked()) {
      isArrowUp = !isArrowUp;
      invalidate();
      return true;
    } else {
      return super.performClick();
    }
  }

  @Override public boolean isChecked() {
    return mChecked;
  }

  @Override public void toggle() {
    mChecked = !mChecked;
    mTextPaint.setColor(mChecked ? checkedColor : unCheckedColor);
    if (onCheckedChangeListener != null) {
      onCheckedChangeListener.onCheckedChanged(this, mChecked);
    }
    for (CompoundButton.OnCheckedChangeListener listener : listeners) {
      listener.onCheckedChanged(this, mChecked);
    }
    invalidate();
  }

  @Override public void setChecked(boolean checked) {
    mChecked = checked;
    if (mTextPaint != null) {
      mTextPaint.setColor(mChecked ? checkedColor : unCheckedColor);
      if (onCheckedChangeListener != null) onCheckedChangeListener.onCheckedChanged(null, mChecked);
      invalidate();
    }
  }
  private float drawableWidth=10;

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.save();
    canvas.restore();
    String txt = mChecked ? checkedText : unCheckedText;
    if (txt == null) txt = "";
    float textLenth = mTextPaint.measureText(txt);
    float leftText = (getWidth() - (textLenth + MeasureUtils.dpToPx(drawableWidth, getResources()))) / 2;
    //if (textLenth > getWidth() - MeasureUtils.dpToPx(20f, getResources()) - (checkedUpDrawable == null ? 0 : checkedUpDrawable.getIntrinsicWidth())) {
    //  int ind = (int) ((getWidth() - MeasureUtils.dpToPx(20f, getResources()) - (checkedUpDrawable == null ? 0 : checkedUpDrawable.getIntrinsicWidth())) / textLenth * txt.length());
    //  txt = txt.substring(0, ind).concat("…");
    //}
    float topText = (getMeasuredHeight() - mTextPaint.getFontMetrics().bottom + mTextPaint.getFontMetrics().top) / 2 - mTextPaint.getFontMetrics().top;
    mTextPaint.setColor(mChecked ? checkedColor : unCheckedColor);
    float textHeight = mTextPaint.getFontMetrics().descent - mTextPaint.getFontMetrics().ascent;

    //float leftText = canvas.getWidth() / 2 - getRightPadding() - textLenth / 2;
    if (leftText < 0) leftText = 0;
    if (topText < 0) topText = 0;
    canvas.drawText(txt, leftText, topText, mTextPaint);
    Drawable curDrawable = null;
    if (mChecked) {
      if (isArrowUp && checkedUpDrawable != null) {
        curDrawable = checkedUpDrawable;
      } else if (!isArrowUp && checkedDownDrawable != null) {
        curDrawable = checkedDownDrawable;
      }
    } else {
      curDrawable = uncheckeDrawable;
    }
    //final int drawableHeight = curDrawable.getIntrinsicHeight();
    //final int drawableWidth = curDrawable.getIntrinsicWidth();
    //int top = canvas.getHeight() / 2 - drawableHeight / 2;
    //if (top < 0) top = 0;
    curDrawable.setBounds((int) (leftText + textLenth + 10), (int) (topText-textHeight+10),
        (int) (leftText + textLenth + 10+MeasureUtils.dpToPx(drawableWidth, getResources())), (int) (topText+10));
    curDrawable.draw(canvas);
  }

  public float getRightPadding() {
    return uncheckeDrawable == null ? 0 : uncheckeDrawable.getIntrinsicWidth();
  }

  @Override public void addCheckedChangeListener(OnCheckedChangeListener listener) {
    listeners.add(listener);
  }

  List<OnCheckedChangeListener> listeners = new ArrayList<>();

  @Override public boolean isOrContainCheck(View v) {
    return v.getId() == this.getId();
  }
}
