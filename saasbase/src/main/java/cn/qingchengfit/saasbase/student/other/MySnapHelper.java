package cn.qingchengfit.saasbase.student.other;

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
  private OnSelectListener listener;
  private int count;

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

  public void setListener(OnSelectListener listener) {
    this.listener = listener;
  }

  public void setCount(int count) {
    this.count = count;
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

  //对返回的position处理，如果不是页面起始position则返回起始position
  @Override
  public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
      int velocityY) {
    int targePosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    if (targePosition % count != 0){
      targePosition = targePosition - targePosition % count;
    }
    if (listener != null){
      listener.onPageSelect(targePosition / count);
    }
    return targePosition;
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

      if (firstChild % count == 0){
        return layoutManager.findViewByPosition(0);
      }else if (layoutManager.getItemCount() > count){
        return layoutManager.findViewByPosition(firstChild / count + count);
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

  public interface OnSelectListener{
    void onPageSelect(int position);
  }

}
