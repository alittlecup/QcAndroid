package cn.qingchengfit.student.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.student.R;

public class CountDateView extends LinearLayout implements Checkable {
  TextView mCountTv;
  TextView mContentTv;
  View mIndicator;
  private boolean _isChecked;

  public CountDateView(Context context) {
    this(context, null);
  }

  public CountDateView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public CountDateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private OnCheckedChangeListener mOnCheckedChangeListener;

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.st_count_date_view, this, true);
    mCountTv = findViewById(R.id.tv_count);
    mContentTv = findViewById(R.id.tv_content);
    mIndicator = findViewById(R.id.view_indicator);
    setClickable(true);
    setChecked(_isChecked);
  }

  public void setCount(String count) {
    mCountTv.setText(count);
  }

  public void setContent(String content) {
    mContentTv.setText(content);
  }

  public void setContentColor(int color) {
    DrawableCompat.setTint(mContentTv.getCompoundDrawables()[0], color);
  }

  @Override public boolean performClick() {
    toggle();

    final boolean handled = super.performClick();
    if (!handled) {
      // View only makes a sound effect if the onClickListener was
      // called, so we'll need to make one here instead.
      playSoundEffect(SoundEffectConstants.CLICK);
    }

    return handled;
  }

  public void setIndicatorDrawable(@DrawableRes int drawable) {
    mIndicator.setBackgroundResource(drawable);
  }

  @Override public void setChecked(boolean checked) {
    if (checked != _isChecked) {
      _isChecked = checked;
      refreshDrawableState();
      mIndicator.setVisibility(_isChecked ? VISIBLE : INVISIBLE);
      mCountTv.setTextColor(
          getResources().getColor(_isChecked ? R.color.text_black : R.color.text_grey));
      if (mOnCheckedChangeListener != null) {
        mOnCheckedChangeListener.onCheckedChanged(this, _isChecked);
      }
    }
  }

  @Override public boolean isChecked() {
    return _isChecked;
  }

  @Override public void toggle() {
    if (!isChecked()) {
      setChecked(!_isChecked);
    }
  }

  /**
   * Interface definition for a callback to be invoked when the checked state
   * of a compound button changed.
   */
  public interface OnCheckedChangeListener {
    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked The new checked state of buttonView.
     */
    void onCheckedChanged(CountDateView buttonView, boolean isChecked);
  }

  /**
   * Register a callback to be invoked when the checked state of this button
   * changes.
   *
   * @param listener the callback to call on checked state change
   */
  public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
    mOnCheckedChangeListener = listener;
  }
}
