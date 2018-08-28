package cn.qingchengfit.student.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.TextView;
import cn.qingchengfit.saascommon.calendar.Calendar;
import cn.qingchengfit.saascommon.calendar.WeekBar;
import cn.qingchengfit.student.R;

public class SimpleWeekBar extends WeekBar {

  private int mPreSelectedIndex;

  public SimpleWeekBar(Context context) {
    super(context);
    LayoutInflater.from(context).inflate(R.layout.st_week_bar, this, true);
    setBackgroundColor(Color.WHITE);
    setClickable(false);
  }

  @Override
  protected void onDateSelected(Calendar calendar, int weekStart, boolean isClick) {
    //getChildAt(mPreSelectedIndex).setSelected(false);
    //int viewIndex = getViewIndexByCalendar(calendar, weekStart);
    //getChildAt(viewIndex).setSelected(true);
    //mPreSelectedIndex = viewIndex;
  }

  /**
   * 当周起始发生变化，使用自定义布局需要重写这个方法，避免出问题
   *
   * @param weekStart 周起始
   */
  @Override
  protected void onWeekStartChange(int weekStart) {
    for (int i = 0; i < getChildCount(); i++) {
      ((TextView) getChildAt(i)).setText(getWeekString(i, weekStart));
    }
  }

  /**
   * 或者周文本，这个方法仅供父类使用
   * @param index index
   * @param weekStart weekStart
   * @return 或者周文本
   */
  private String getWeekString(int index, int weekStart) {
    String[] weeks = getContext().getResources().getStringArray(R.array.st_week_string_array);

    if (weekStart == 1) {
      return weeks[index];
    }
    if (weekStart == 2) {
      return weeks[index == 6 ? 0 : index + 1];
    }
    return weeks[index == 0 ? 6 : index - 1];
  }
}
