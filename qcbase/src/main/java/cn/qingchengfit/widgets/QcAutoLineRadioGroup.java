package cn.qingchengfit.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import cn.qingchengfit.utils.MeasureUtils;

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
 * Created by Paper on 2017/5/8.
 * 用于单选 但是可以全不选的状态，点击选中，再点取消
 */

//可自动换行的RadioGroup
public class QcAutoLineRadioGroup extends LinearLayout
    implements CompoundButton.OnCheckedChangeListener {

  private int mScreenWidth;
  private int mHorizontalSpacing;
  private int mVerticalSpacing;
  private CheckedChange checkedChange;
  public boolean isSingleSelected;

  public QcAutoLineRadioGroup(Context context) {
    super(context);
    init();
  }

  public QcAutoLineRadioGroup(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public QcAutoLineRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public QcAutoLineRadioGroup(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  public void setSpacing(int horizontalSpacing, int verticalSpacing) {
    mHorizontalSpacing = horizontalSpacing;
    mVerticalSpacing = verticalSpacing;
  }

  private void init() {
    DisplayMetrics dm = getResources().getDisplayMetrics();
    mScreenWidth = dm.widthPixels;
    mHorizontalSpacing = MeasureUtils.dpToPx(10f, getResources());
    mVerticalSpacing = MeasureUtils.dpToPx(10f, getResources());
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    measureChildren(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(widthSize, heightSize);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int mTotalHeight = 0;
    int mTotalWidth = 0;
    int mTempHeight = 0;
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View childView = getChildAt(i);
      int measureHeight = childView.getMeasuredHeight();
      int measuredWidth = childView.getMeasuredWidth();
      mTempHeight = (measureHeight > mTempHeight) ? measureHeight : mTempHeight;
      if ((measuredWidth + mTotalWidth + mHorizontalSpacing) > mScreenWidth) {
        mTotalWidth = 0;
        mTotalHeight += (mTempHeight + mVerticalSpacing);
        mTempHeight = 0;
      }
      childView.layout(mTotalWidth + mHorizontalSpacing, mTotalHeight,
          measuredWidth + mTotalWidth + mHorizontalSpacing, mTotalHeight + measureHeight);
      mTotalWidth += (measuredWidth + mHorizontalSpacing);
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (getChildCount() > 0) {
      for (int i = 0; i < getChildCount(); i++) {
        View v = getChildAt(i);
        if (v instanceof QcCheckable) {
          ((QcCheckable) v).addCheckedChangeListener(this);
        }
      }
    }
  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (getChildCount() > 0) {
      for (int i = 0; i < getChildCount(); i++) {
        View v = getChildAt(i);
        if (v instanceof QcCheckable && !((QcCheckable) v).isOrContainCheck(buttonView)) {
          ((QcCheckable) v).setChecked(false);
        }
      }
    }
    if (checkedChange != null) checkedChange.onCheckedChange();
  }

  /**
   * @return -1 表示没有任何选中
   */
  public int getCheckedPos() {
    if (getChildCount() > 0) {
      for (int i = 0; i < getChildCount(); i++) {
        View v = getChildAt(i);
        if (v instanceof QcCheckable && ((QcCheckable) v).isChecked()) {
          return i;
        }
      }
    }
    return -1;
  }

  public void setCheckPos(int pos) {
    for (int i = 0; i < getChildCount(); i++) {
      View v = getChildAt(i);
      if (v instanceof QcCheckable) {
        ((QcCheckable) v).setChecked(false);
      }
    }
    if (getChildCount() > pos && pos >= 0) {
      View v = getChildAt(pos);
      if (v instanceof QcCheckable) {
        ((QcCheckable) v).setChecked(true);
      }
    }
  }

  public void clearCheck() {
    for (int i = 0; i < getChildCount(); i++) {
      View v = getChildAt(i);
      if (v instanceof QcCheckable) {
        ((QcCheckable) v).setChecked(false);
      }
    }
  }

  public CheckedChange getCheckedChange() {
    return checkedChange;
  }

  public void setCheckedChange(CheckedChange checkedChange) {
    this.checkedChange = checkedChange;
  }

  public interface CheckedChange {
    void onCheckedChange();
  }
}
