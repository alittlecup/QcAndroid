package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fb on 2017/9/18.
 */

public class FilterTextLayout extends LinearLayout implements View.OnClickListener {

  private TextView tvFilter;
  private ImageView imgFilter;
  private String label;
  private int drawable;
  private boolean isClicked;
  private OnClickFilterListener listener;

  public FilterTextLayout(Context context) {
    super(context);
    onFinishInflate();
  }

  public FilterTextLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
    onFinishInflate();
  }

  public FilterTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
    onFinishInflate();
  }

  private void init(Context context, @Nullable AttributeSet attrs){
    LayoutInflater.from(context).inflate(R.layout.layout_filter_item, this, true);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FilterTextLayout);
    label = ta.getString(R.styleable.FilterTextLayout_filter_label);
    drawable = ta.getResourceId(R.styleable.FilterTextLayout_img_filter, R.drawable.vd_filter_arrow_down);
    ta.recycle();
  }

  public void setListener(OnClickFilterListener listener) {
    this.listener = listener;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    tvFilter = (TextView) findViewById(R.id.text_filter);
    imgFilter = (ImageView) findViewById(R.id.img_filter);
    tvFilter.setText(label);
    imgFilter.setImageResource(drawable);
    setOnClickListener(this);
  }

  public void setLabel(String label) {
    this.label = label;
    tvFilter.setText(label);
  }

  public String getLabel() {
    return label;
  }

  public void isUp(){
    imgFilter.setImageResource(R.drawable.vd_filter_arrow_up);
  }

  public void isDown(){
    imgFilter.setImageResource(R.drawable.vd_filter_arrow_down);
  }

  @Override public void onClick(View v) {
    v.setTag(!isClicked);
    imgFilter.setImageResource((boolean)v.getTag() ? R.drawable.vd_filter_arrow_up : R.drawable.vd_filter_arrow_down);
    if (listener != null) {
      listener.onSelectFilter(v);
    }
  }

  public interface OnClickFilterListener{
     void onSelectFilter(View v);
  }

}
