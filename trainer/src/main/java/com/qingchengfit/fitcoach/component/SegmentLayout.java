package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CustomSetmentLayout.onSegmentChangeListener;

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
public class SegmentLayout extends LinearLayout
    implements onSegmentChangeListener, View.OnClickListener {

  public SegmentListener segmentListener;
  public TouchUpListener listener;
  ImageView segmentImg;
  TextView segmentText;
  Drawable[] drawables;
  private String text;

  public SegmentLayout(Context context) {
    super(context);
    init(context);
  }

  public SegmentLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public SegmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
    LayoutInflater.from(context).inflate(R.layout.layout_segment, this, true);
    segmentImg = findViewById(R.id.segment_img);
    segmentText = findViewById(R.id.segment_text);
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

  }

  @Override public void onCheckChange(boolean isChecked) {
    if (isChecked) {
      segmentImg.setImageDrawable(drawables[1]);
      //            segmentText.setTextColor(Color.argb(255, 39, 191, 189));
      segmentText.setTextColor(getResources().getColor(R.color.primary));
    } else {
      segmentImg.setImageDrawable(drawables[0]);
      segmentText.setTextColor(Color.argb(255, 102, 102, 102));
    }
  }

  public void setDrawables(Drawable[] drawables) {
    this.drawables = drawables;
    segmentImg.setImageDrawable(drawables[0]);
  }

  public void setDrawables(int res0, int res1) {
    this.drawables = new Drawable[] {
        getContext().getResources().getDrawable(res0), getContext().getResources().getDrawable(res1)
    };
    segmentImg.setImageDrawable(drawables[0]);
  }

  public void setText(String s) {
    this.text = s;
    segmentText.setText(text);
  }

  @Override public void onClick(View v) {
    if (listener != null) listener.onTouchUp(v);
    if (segmentListener != null) segmentListener.onSegmentClick(v);
  }

  public interface TouchUpListener {
    void onTouchUp(View v);
  }

  public interface SegmentListener {
    void onSegmentClick(View v);
  }
}
