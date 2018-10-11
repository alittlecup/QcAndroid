package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fb on 2018/1/4.
 */

public class ExpandTextView extends LinearLayout implements View.OnClickListener{

  private final static int MAX_LINE = 1;

  @DrawableRes
  private int drawableRight;
  private TextView tvContent;
  private ImageView imgExpand;
  private String content;
  private int maxLine;

  public ExpandTextView(Context context) {
    super(context);
  }

  public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public ExpandTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private void init(Context context, @Nullable AttributeSet attrs){
    LayoutInflater.from(getContext()).inflate(R.layout.layout_expand_textview, this, true);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
    drawableRight = typedArray.getResourceId(R.styleable.ExpandTextView_el_tv_drawable, R.drawable.vd_filter_arrow_down);
    content = typedArray.getString(R.styleable.ExpandTextView_el_tv_content);
    maxLine = typedArray.getInt(R.styleable.ExpandTextView_el_tv_max_line, MAX_LINE);
    typedArray.recycle();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    tvContent = (TextView)findViewById(R.id.tv_expand_content);
    imgExpand = (ImageView)findViewById(R.id.image_expand);

    tvContent.setText(content);

    imgExpand.setImageResource(drawableRight);
    imgExpand.setVisibility(GONE);
    imgExpand.setOnClickListener(this);
  }

  public void setContent(String content) {
    this.content = content;
    tvContent.setText(content);
    invalidate();
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (tvContent.getLineCount() > maxLine
        || tvContent.getPaint().measureText(tvContent.getText().toString())
        > tvContent.getMeasuredWidth()) {
      tvContent.setMaxLines(maxLine);
      imgExpand.setVisibility(VISIBLE);
    }
  }

  @Override public void onClick(View view) {
    if (maxLine == MAX_LINE){
      maxLine = Integer.MAX_VALUE;
      tvContent.setMaxLines(maxLine);
      imgExpand.setImageResource(R.drawable.vd_filter_arrow_up);
    }else{
      maxLine = MAX_LINE;
      tvContent.setMaxLines(maxLine);
      imgExpand.setImageResource(drawableRight);
    }
  }
}
