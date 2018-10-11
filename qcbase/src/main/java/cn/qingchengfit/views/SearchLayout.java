package cn.qingchengfit.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;

/**
 * Created by huangbaole on 2018/4/18.
 */

public class SearchLayout extends LinearLayout {
  TextView hint;
  LinearLayout background;

  public SearchLayout(Context context) {
    this(context, null);
  }

  public SearchLayout(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public SearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    inflate(context, R.layout.search_layout, this);
    hint = findViewById(R.id.tv_hint);
    background = findViewById(R.id.bg);
  }

  public void setHint(String hint) {
    this.hint.setText(hint);
  }

  public void setBackgroundResource(int drawRef) {
    background.setBackgroundResource(drawRef);
  }
}
