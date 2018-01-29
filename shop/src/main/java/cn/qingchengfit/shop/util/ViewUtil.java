package cn.qingchengfit.shop.util;

import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangbaole on 2018/1/29.
 */

public final class ViewUtil {
  @MainThread public static void resetRecyclerViewHeight(RecyclerView recyclerView) {;
    recyclerView.post(new Runnable() {
      @Override public void run() {
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        View childAt = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        layoutParams.height = childAt.getHeight() * recyclerView.getAdapter().getItemCount();
        recyclerView.setLayoutParams(layoutParams);
      }
    });

  }
}
