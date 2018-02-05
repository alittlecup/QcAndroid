package cn.qingchengfit.recruit.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/7/13.
 */

public class LinearLayoutManagerPlus extends SmoothScrollLinearLayoutManager {
  private static final String TAG = LinearLayoutManagerPlus.class.getSimpleName();
  private int[] mMeasuredDimension = new int[2];

  public LinearLayoutManagerPlus(Context context) {
    super(context);
  }

  public LinearLayoutManagerPlus(Context context, int orientation, boolean reverseLayout) {
    super(context, orientation, reverseLayout);
  }

  @Override
  public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec,
      int heightSpec) {
    final int widthMode = View.MeasureSpec.getMode(widthSpec);
    final int heightMode = View.MeasureSpec.getMode(heightSpec);
    final int widthSize = View.MeasureSpec.getSize(widthSpec);
    final int heightSize = View.MeasureSpec.getSize(heightSpec);
    Log.i(TAG, "onMeasure called. \nwidthMode "
        + widthMode
        + " \nheightMode "
        + heightSpec
        + " \nwidthSize "
        + widthSize
        + " \nheightSize "
        + heightSize
        + " \ngetItemCount() "
        + getItemCount());
    int width = 0;
    int height = 0;
    for (int i = 0; i < getItemCount(); i++) {
      measureScrapChild(recycler, i,
          View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
          View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);
      if (getOrientation() == HORIZONTAL) {
        width = width + mMeasuredDimension[0];
        if (i == 0) {
          height = mMeasuredDimension[1];
        }
      } else {
        height = height + mMeasuredDimension[1];
        if (i == 0) {
          width = mMeasuredDimension[0];
        }
      }
    }
    switch (widthMode) {
      case View.MeasureSpec.EXACTLY:
        width = widthSize;
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.UNSPECIFIED:
    }
    switch (heightMode) {
      case View.MeasureSpec.EXACTLY:
        height = heightSize;
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.UNSPECIFIED:
    }
    setMeasuredDimension(width, height);
  }

  private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
      int heightSpec, int[] measuredDimension) {
    try {
      View view = recycler.getViewForPosition(position);//fix 动态添加时报IndexOutOfBoundsException
      if (view != null) {
        RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
        int childWidthSpec =
            ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
        int childHeightSpec =
            ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(),
                p.height);
        view.measure(childWidthSpec, childHeightSpec);
        measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
        measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
        recycler.recycleView(view);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }
  }
}
