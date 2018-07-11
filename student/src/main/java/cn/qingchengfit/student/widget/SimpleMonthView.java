package cn.qingchengfit.student.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import cn.qingchengfit.saascommon.calendar.Calendar;
import cn.qingchengfit.saascommon.calendar.MonthView;

public class SimpleMonthView extends MonthView {

  private int mH;
  private int mW;

  public SimpleMonthView(Context context) {
    super(context);
  }

  @Override protected void onPreviewHook() {
    mH = dipToPx(getContext(), 2);
    mW = dipToPx(getContext(), 10);
  }

  @Override protected void onLoopStart(int x, int y) {

  }

  @Override protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y,
      boolean hasScheme) {
    canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mSelectedPaint);
    return true;
  }

  @Override protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
    canvas.drawRect(x + mW, y + mItemHeight - mH * 2, x + mItemWidth - mW, y + mItemHeight - mH,
        mSchemePaint);
  }

  @Override
  protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme,
      boolean isSelected) {
    float baselineY = mTextBaseLine + y;
    int cx = x + mItemWidth / 2;

    if (isSelected) {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mSelectTextPaint);
    } else if (hasScheme) {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
          calendar.isCurrentDay() ? mCurDayTextPaint
              : calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
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
