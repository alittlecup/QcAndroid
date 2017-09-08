package cn.qingchengfit.staffkit.views.student;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by fb on 2017/9/6.
 */

public class MySnapHelper extends LinearSnapHelper {
  private OrientationHelper mHorizontalHelper, mVerticalHelper;

  @Nullable
  @Override
  public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View targetView) {
    int[] out = new int[2];
    if (layoutManager.canScrollHorizontally()) {
      out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
    } else {
      out[0] = 0;
    }
    if (layoutManager.canScrollVertically()) {
      out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager));
    } else {
      out[1] = 0;
    }
    return out;
  }

  private int distanceToStart(View targetView, OrientationHelper helper) {
    return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
  }

  @Nullable
  @Override
  public View findSnapView(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof LinearLayoutManager) {
      if (layoutManager.canScrollHorizontally()) {
        return findStartView(layoutManager, getHorizontalHelper(layoutManager));
      } else {
        return findStartView(layoutManager, getVerticalHelper(layoutManager));
      }
    }

    return super.findSnapView(layoutManager);
  }



  private View findStartView(RecyclerView.LayoutManager layoutManager,
      OrientationHelper helper) {
    if (layoutManager instanceof LinearLayoutManager) {
      int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
      boolean isLastItem = ((LinearLayoutManager) layoutManager)
          .findLastCompletelyVisibleItemPosition()
          == layoutManager.getItemCount() - 1;

      if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
        return null;
      }

      if (firstChild % 8 == 0){
        return layoutManager.findViewByPosition(0);
      }else if (firstChild % 8 < 4){
        layoutManager.scrollToPosition(0);
        return null;
      }else if (layoutManager.getItemCount() > 8){
        return layoutManager.findViewByPosition(firstChild / 8 + 8);
      }else{
        return null;
      }
    }
    return super.findSnapView(layoutManager);
  }


  private OrientationHelper getHorizontalHelper(
      @NonNull RecyclerView.LayoutManager layoutManager) {
    if (mHorizontalHelper == null) {
      mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
    }
    return mHorizontalHelper;
  }

  private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
    if (mVerticalHelper == null) {
      mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
    }
    return mVerticalHelper;

  }

}
