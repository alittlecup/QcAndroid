package cn.qingchengfit.student.widget;

import android.content.Context;
import android.util.AttributeSet;

public class SearchView extends android.support.v7.widget.SearchView {
  boolean isExpanded=false;
  public SearchView(Context context) {
    super(context);
  }

  public SearchView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public void onActionViewCollapsed() {
    super.onActionViewCollapsed();
    if (listener != null) {
      listener.onActionViewCollapsed();
      isExpanded=false;
    }
  }

  @Override public void onActionViewExpanded() {
    super.onActionViewExpanded();
    if (listener != null) {
      listener.onActionViewExpanded();
      isExpanded=true;
    }
  }

  private onActionViewListener listener;

  public boolean isExpanded() {
    return isExpanded;
  }

  public void setOnActionViewCollapsed(onActionViewListener listener) {
    this.listener = listener;
  }

  public interface onActionViewListener {
    void onActionViewCollapsed();

    void onActionViewExpanded();
  }
}
