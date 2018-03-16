package cn.qingchengfit.shop.util;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by huangbaole on 2018/1/29.
 */

public final class ViewUtil {
  @MainThread public static void resetRecyclerViewHeight(RecyclerView recyclerView) {
    ;
    recyclerView.post(new Runnable() {
      @Override public void run() {
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        View childAt = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        layoutParams.height = childAt.getHeight() * recyclerView.getAdapter().getItemCount();
        recyclerView.setLayoutParams(layoutParams);
      }
    });
  }

  public static MaterialDialog instanceDelDialog(Context context, String content,
      MaterialDialog.SingleButtonCallback callback) {
    return new MaterialDialog.Builder(context).content(content)
        .autoDismiss(true)
        .positiveColorRes(cn.qingchengfit.saasbase.R.color.primary)
        .negativeColorRes(cn.qingchengfit.widgets.R.color.text_color_gray)
        .negativeText(cn.qingchengfit.widgets.R.string.pickerview_cancel)
        .positiveText(cn.qingchengfit.widgets.R.string.pickerview_submit)
        .onPositive(callback)
        .build();
  }
}
