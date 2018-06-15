package cn.qingchengfit.saasbase.mvvm_student.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;

public class CountTextView extends LinearLayout {
  TextView mCountTv;
  TextView mTextTv;
  private int mCount;
  private String mText;

  public CountTextView(Context context) {
    this(context, null);
  }

  public CountTextView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public CountTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountTextView);
      mCount = typedArray.getInt(R.styleable.CountTextView_ct_count, 0);
      mText = typedArray.getString(R.styleable.CountTextView_ct_text);
      typedArray.recycle();
    }
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.widget_count_text, this, true);
    mTextTv = findViewById(R.id.tv_text);
    mTextTv.setText(mText);
    mCountTv = findViewById(R.id.tv_count);
    mCountTv.setText(mCount == 0 ? "- -" : String.valueOf(mCount));
  }

  public void setCount(int count) {
    mCount = count;
    mCountTv.setText(mCount == 0 ? "- -" : String.valueOf(mCount));
  }

  public void setText(String text) {
    mText = text;
    mTextTv.setText(mText);
  }
}
