package cn.qingchengfit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import cn.qingchengfit.utils.MeasureUtils;
import java.util.List;

import static cn.qingchengfit.utils.MeasureUtils.dpToPx;

/**
 * Created by fb on 2017/3/20.
 */

public class AutoLineGroup extends ViewGroup implements CompoundButton.OnCheckedChangeListener {

    private int mScreenWidth;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public AutoLineGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
        init();
    }

    public AutoLineGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLineGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AutoLineGroup(Context context){
        super(context);
        init();
    }

    public void setSpacing(int horizontalSpacing, int verticalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        mVerticalSpacing = verticalSpacing;
    }

    private void init() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
      mHorizontalSpacing = dpToPx(10f, getResources());
      mVerticalSpacing = dpToPx(10f, getResources());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
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
            childView.layout(mTotalWidth + mHorizontalSpacing, mTotalHeight, measuredWidth + mTotalWidth + mHorizontalSpacing, mTotalHeight + measureHeight);
            mTotalWidth += (measuredWidth + mHorizontalSpacing);


        }
    }

    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (getChildCount() > 0){
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof QcCheckable && !((QcCheckable) v).isOrContainCheck(buttonView)){
                    ((QcCheckable) v).setChecked(false);
                }
            }
        }
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0){
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof QcCheckable){
                    ((QcCheckable) v).addCheckedChangeListener(this);
                }
            }
        }
    }

  public void addChildren(List<String> list) {
    for (String s : list) {
      CheckBoxButton btn = (CheckBoxButton) LayoutInflater.from(getContext())
          .inflate(R.layout.layout_radiogroup_checkbox, null);
      btn.setContent(s);
      LayoutParams params = new LayoutParams(MeasureUtils.dpToPx(75f, getResources()),
          MeasureUtils.dpToPx(30f, getResources()));
      btn.addCheckedChangeListener(this);
      addView(btn, params);
    }
  }

  public void addChildren(String... list) {
    for (String s : list) {
      CheckBoxButton btn = (CheckBoxButton) LayoutInflater.from(getContext())
          .inflate(R.layout.layout_radiogroup_checkbox, null);
      btn.setContent(s);
      LayoutParams params = new LayoutParams(MeasureUtils.dpToPx(75f, getResources()),
          MeasureUtils.dpToPx(30f, getResources()));
      btn.addCheckedChangeListener(this);
      addView(btn, params);
    }
  }

  public int getCheckPos() {
    if (getChildCount() > 0) {
      for (int i = 0; i < getChildCount(); i++) {
        View v = getChildAt(i);
        if (v instanceof QcCheckable) {
          if (((QcCheckable) v).isChecked()) return i;
        }
      }
      return -1;
    }
    return -1;
  }

  public void clearAllCheck() {
    if (getChildCount() > 0) {
      for (int i = 0; i < getChildCount(); i++) {
        View v = getChildAt(i);
        if (v instanceof QcCheckable) {
          ((QcCheckable) v).setChecked(false);
                }
            }
        }
    }

}
