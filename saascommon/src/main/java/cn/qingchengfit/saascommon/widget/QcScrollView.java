package cn.qingchengfit.saascommon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class QcScrollView extends ScrollView {
  public QcScrollView(Context context) {
    super(context);
  }

  public QcScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public QcScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  //设置接口
  public interface OnScrollListener{
    void onScroll(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
  }
  private OnScrollListener listener;

  public void setOnScrollListener(OnScrollListener listener) {
    this.listener = listener;
  }


  //重写原生onScrollChanged方法，将参数传递给接口，由接口传递出去
  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    if(listener != null){
      listener.onScroll(this,l,t,oldl,oldt);
    }
  }


}
