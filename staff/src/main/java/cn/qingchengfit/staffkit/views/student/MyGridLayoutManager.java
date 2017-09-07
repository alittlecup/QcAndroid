package cn.qingchengfit.staffkit.views.student;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;

/**
 * Created by fb on 2017/9/6.
 */

public class MyGridLayoutManager extends SmoothScrollGridLayoutManager {
  private int mLastPosition;
  private int spanCount;

  public MyGridLayoutManager(Context context, int spanCount) {
    super(context, spanCount);
  }

  public MyGridLayoutManager(Context context, int spanCount, int orientation,
      boolean reverseLayout) {
    super(context, spanCount, orientation, reverseLayout);
  }

  @Override public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    //super.onLayoutChildren(recycler, state);
    if (getItemCount() == 0) {
      detachAndScrapAttachedViews(recycler);
      return;
    }
    if (getChildCount() == 0 && state.isPreLayout()) {
      return;
    }
    detachAndScrapAttachedViews(recycler);
    mLastPosition = getItemCount() - 1;
    reLayout(recycler);
  }

  @Override public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
  }

  private void reLayout(RecyclerView.Recycler recycler){
    for(int i = 0; i< mLastPosition; i++){
      View childView = recycler.getViewForPosition(i);
      if (i != mLastPosition - 1) {
        childView.setLayoutParams(new RecyclerView.LayoutParams(getWidth() / 4, getHeight() / 2));
      }
      if (i == mLastPosition - 1){
        childView.setLayoutParams(new RecyclerView.LayoutParams(getWidth() / (i % 4), getHeight() / (i % 4)));
      }
      addView(childView);
    }
  }

}
