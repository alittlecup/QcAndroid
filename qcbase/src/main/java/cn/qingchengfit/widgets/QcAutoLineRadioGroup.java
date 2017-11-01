package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
  private OnCheckoutPositionListener onCheckoutPositionListener;
  private Context context;

  public QcAutoLineRadioGroup(Context context) {
    super(context);
  }

  public QcAutoLineRadioGroup(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public QcAutoLineRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public QcAutoLineRadioGroup(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  public void setSpacing(int horizontalSpacing, int verticalSpacing) {
    mHorizontalSpacing = horizontalSpacing;
    mVerticalSpacing = verticalSpacing;
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QcRadioGroup);
    this.context = context;
    isSingleSelected = ta.getBoolean(R.styleable.QcRadioGroup_qc_single, true);
    DisplayMetrics dm = getResources().getDisplayMetrics();
    mScreenWidth = dm.widthPixels;
    mHorizontalSpacing = MeasureUtils.dpToPx(10f, getResources());
    mVerticalSpacing = MeasureUtils.dpToPx(10f, getResources());
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    //int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    //measureChildren(widthMeasureSpec, heightMeasureSpec);
    //setMeasuredDimension(widthSize, heightSize);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);

    int height ;
    if (heightMode == MeasureSpec.EXACTLY) {
      height = heightSize;
    } else {
      int childCount = getChildCount();
      if(childCount<=0){
        height = 0;
      }else{
        int row = 1;
        int widthSpace = width;
        for(int i = 0;i<childCount; i++){
          View view = getChildAt(i);
          int childW = view.getMeasuredWidth();
          if(widthSpace >= childW ){
            widthSpace -= childW;
          }else{
            row ++;
            widthSpace = width-childW;
          }
          widthSpace -= mHorizontalSpacing;
        }
        int childH = getChildAt(0).getMeasuredHeight();
        height = (childH * row) + mVerticalSpacing * (row-1);

      }
    }

    setMeasuredDimension(width, height);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    //int mTotalHeight = 0;
    //int mTotalWidth = 0;
    //int mTempHeight = 0;
    //int childCount = getChildCount();
    //for (int i = 0; i < childCount; i++) {
    //  View childView = getChildAt(i);
    //  int measureHeight = childView.getMeasuredHeight();
    //  int measuredWidth = childView.getMeasuredWidth();
    //  mTempHeight = (measureHeight > mTempHeight) ? measureHeight : mTempHeight;
    //  if ((measuredWidth + mTotalWidth + mHorizontalSpacing) > mScreenWidth) {
    //    mTotalWidth = 0;
    //    mTotalHeight += (mTempHeight + mVerticalSpacing);
    //    mTempHeight = 0;
    //  }
    //  childView.layout(mTotalWidth + mHorizontalSpacing, mTotalHeight,
    //      measuredWidth + mTotalWidth + mHorizontalSpacing, mTotalHeight + measureHeight);
    //  mTotalWidth += (measuredWidth + mHorizontalSpacing);
    //}
    int row = 0;
    int right = 0;
    int botom;
    for (int i = 0; i < getChildCount(); i++) {
      View childView = getChildAt(i);
      int childW = childView.getMeasuredWidth();
      int childH = childView.getMeasuredHeight();
      right += childW;
      botom = row * (childH + mVerticalSpacing) + childH;
      if (right > (r - mHorizontalSpacing)) {
        row++;
        right = childW;
        botom = row * (childH + mVerticalSpacing) + childH;
      }
      childView.layout(right - childW, botom - childH, right, botom);

      right += mHorizontalSpacing;
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

  public void setOnCheckoutPositionListener(OnCheckoutPositionListener onCheckoutPositionListener) {
    this.onCheckoutPositionListener = onCheckoutPositionListener;
  }

  public void setSingleSelected(boolean singleSelected) {
    isSingleSelected = singleSelected;
  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (getChildCount() > 0) {
      if (isSingleSelected) {
        for (int i = 0; i < getChildCount(); i++) {
          View v = getChildAt(i);
          if (v instanceof QcCheckable && !((QcCheckable) v).isOrContainCheck(buttonView)) {
            ((QcCheckable) v).setChecked(false);
          }
        }
      }
    }
    if (checkedChange != null) checkedChange.onCheckedChange();

    if (onCheckoutPositionListener != null){
      onCheckoutPositionListener.onCheckPosition(getCheckedPosList());
    }
  }

  //多选情况下获取选中的position
  public List<Integer> getCheckedPosList(){
    List<Integer> checkedList = new ArrayList<>();
    if (getChildCount() > 0){
      for (int i = 0; i < getChildCount(); i++) {
        View v = getChildAt(i);
        if (v instanceof QcCheckable && ((QcCheckable) v).isChecked()) {
          checkedList.add(i);
        }
      }
    }
    return checkedList;
  }

  public void addChild(View btn){
    if (btn instanceof QcCheckable){
      ((QcCheckable) btn).addCheckedChangeListener(this);
    }
    addView(btn);
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

  public interface OnCheckoutPositionListener{
    void onCheckPosition(List<Integer> checkedList);
  }

}
