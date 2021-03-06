package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CustomSetmentLayout.onSegmentChangeListener;
import java.util.Date;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/14 2015.
 */
public class ScheduleDateLayout extends LinearLayout
    implements onSegmentChangeListener, View.OnClickListener {

  public SegmentListener segmentListener;
  public TouchUpListener listener;
  TextView weekday;
  TextView date;
  TextView indicator;
  private Date Viewdate;

  public ScheduleDateLayout(Context context) {
    super(context);
    init(context);
  }

  public ScheduleDateLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ScheduleDateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setListener(TouchUpListener listener) {
    this.listener = listener;
  }

  public void setSegmentListener(SegmentListener segmentListener) {
    this.segmentListener = segmentListener;
  }

  public void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.layout_schedule_date, this, true);
    weekday = findViewById(R.id.weekday);
    date = findViewById(R.id.date);
    indicator = findViewById(R.id.indicator);
    setOnClickListener(this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
  }

  @Override public void onViewAdded(View child) {
    super.onViewAdded(child);
    //        segmentImg.setImageDrawable(drawables[0]);
    //       segmentText.setText(text);

  }

  public void setCheck(boolean isCheck) {
    onCheckChange(isCheck);
  }

  @Override public void onCheckChange(boolean isChecked) {
    if (isChecked) {
      weekday.setTextColor(Color.argb(255, 255, 255, 255));
      date.setTextColor(Color.argb(255, 255, 255, 255));
      indicator.setVisibility(VISIBLE);
    } else {
      weekday.setTextColor(Color.argb(153, 255, 255, 255));
      date.setTextColor(Color.argb(153, 255, 255, 255));
      indicator.setVisibility(INVISIBLE);
    }
  }

  public void setWeekday(String week, String d) {
    weekday.setText(week);
    date.setText(d);
  }

  @Override public void onClick(View v) {
    if (listener != null) listener.onTouchUp(v);
    if (segmentListener != null) segmentListener.onSegmentClick(v);
  }

  public Date getViewdate() {
    return Viewdate;
  }

  public void setViewdate(Date viewdate) {
    Viewdate = viewdate;
  }

  public interface TouchUpListener {
    void onTouchUp(View v);
  }

  public interface SegmentListener {
    void onSegmentClick(View v);
  }
}
