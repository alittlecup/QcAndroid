package cn.qingchengfit.saasbase.student.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by fb on 2017/9/7.
 */

public class MyIndicator extends CircleIndicator {


  public MyIndicator(Context context) {
    super(context);
  }

  public MyIndicator(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  public void createIndicators(int count){
    removeAllViews();
    if (count < 0){
      return;
    }
    addIndicator(mIndicatorBackgroundResId, mAnimationOut);
    for (int i = 1; i < count; i++) {
      //            if (i == count - 1)
      //                addIndicator(mIndicatorUnselectedBackgroundResId, mAnimationIn, true);
      //            else
      addIndicator(mIndicatorUnselectedBackgroundResId, mAnimationIn);
    }
  }

  @Override public void onPageSelected(int position) {
    if (mAnimationIn.isRunning()) mAnimationIn.end();
    if (mAnimationOut.isRunning()) mAnimationOut.end();
    try {
      View currentIndicator = getChildAt(mCurrentPosition);
      currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
      mAnimationIn.setTarget(currentIndicator);
      mAnimationIn.start();

      View selectedIndicator = getChildAt(position);
      selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
      mAnimationOut.setTarget(selectedIndicator);
      mAnimationOut.start();

      mCurrentPosition = position;
    } catch (Exception e) {

    }
  }
}
