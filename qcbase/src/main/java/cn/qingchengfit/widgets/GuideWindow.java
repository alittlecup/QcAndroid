package cn.qingchengfit.widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.qingchengfit.utils.MeasureUtils;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/11/25.
 */

public class GuideWindow {

  public static final int DOWN = 0;
  public static final int UP = 1;

  private Context context;
  private String contentStr;
  private int location;
  private PopupWindow popupWindow;
  private View popview;
  private TextView textView;

  public GuideWindow(Context context, String contentStr, int location) {
    this.context = context;
    this.contentStr = contentStr;
    this.location = location;
    initView();
  }

  public void initView() {
    popupWindow = new PopupWindow(context);
    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    popview = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow, null);
    popupWindow.setContentView(popview);
    popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

    textView = (TextView) popview.findViewById(R.id.text_content);
    textView.setText(contentStr);
    switch (location) {
      case DOWN:
        textView.setBackgroundResource(R.drawable.qcw_ic_bubble_up);
        break;
      case UP:
        textView.setBackgroundResource(R.drawable.qcw_ic_bubble_down);
        break;
    }
    popupWindow.setOutsideTouchable(false);
    popupWindow.setFocusable(false);
  }

  public boolean isShowing(){
    if (popupWindow == null){
      return false;
    }else {
      return popupWindow.isShowing();
    }

  }

  public void dismiss(){
    if (popupWindow != null && popupWindow.isShowing()){
      popupWindow.dismiss();
    }
  }

  public void show(View view) {
    int vh = view.getMeasuredHeight();
    int vw = view.getMeasuredWidth();
    switch (location) {
      case DOWN:
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        textView.setMaxWidth(w_screen - view.getLeft() - 60);
        popview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.showAsDropDown(view, (100 - vh / 2), 0);
        break;
      case UP:
        textView.setMaxWidth(view.getRight() - 50);
        popview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popview.getMeasuredWidth();
        int popupHeight = popview.getMeasuredHeight();
        int x = -popupWidth + vw/2+ MeasureUtils.dpToPx(45f,context.getResources());
        popupWindow.showAsDropDown(view, x, -(popupHeight + vh));
        break;
    }
  }
}
