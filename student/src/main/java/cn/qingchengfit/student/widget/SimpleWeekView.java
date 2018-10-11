package cn.qingchengfit.student.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import cn.qingchengfit.saascommon.calendar.Calendar;
import cn.qingchengfit.saascommon.calendar.WeekView;

/**
 * 简单周视图
 * Created by huanghaibin on 2017/11/29.
 */

public class SimpleWeekView extends WeekView {

  private int mH;
  private int mW;

  public SimpleWeekView(Context context) {
    super(context);
  }

  @Override protected void onPreviewHook() {

    mH = dipToPx(getContext(), 2);
    mW = dipToPx(getContext(), 10);
  }

  @Override
  protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
    canvas.drawRect(x, 0, x + mItemWidth, mItemHeight, mSelectedPaint);
    return true;
  }

  @Override protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
    canvas.drawRect(x + mW, mItemHeight - mH * 2, x + mItemWidth - mW, mItemHeight - mH,
        mSchemePaint);
  }

  @Override protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme,
      boolean isSelected) {
    float baselineY = mTextBaseLine;
    int cx = x + mItemWidth / 2;
    if (isSelected) {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
          calendar.isCurrentDay() ? mCurDayTextPaint
              : calendar.isCurrentMonth() ? mSelectTextPaint : mOtherMonthTextPaint);
    } else if (hasScheme) {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
          calendar.isCurrentDay() ? mCurDayTextPaint
              : calendar.isCurrentMonth() ? mSchemeTextPaint : mSchemeTextPaint);
    } else {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
          calendar.isCurrentDay() ? mCurDayTextPaint
              : calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
    }
  }

  /**
   * dp转px
   *
   * @param context context
   * @param dpValue dp
   * @return px
   */
  private static int dipToPx(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
