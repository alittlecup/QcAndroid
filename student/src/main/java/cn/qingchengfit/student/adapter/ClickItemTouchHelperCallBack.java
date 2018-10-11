package cn.qingchengfit.student.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import eu.davidea.flexibleadapter.helpers.ItemTouchHelperCallback;

public class ClickItemTouchHelperCallBack extends ItemTouchHelperCallback {
  public ClickItemTouchHelperCallBack(AdapterCallback adapterCallback) {
    super(adapterCallback);
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //允许上下的拖动
    int swipeFlags = ItemTouchHelper.LEFT;   //只允许从右向左侧滑
    return makeMovementFlags(dragFlags,swipeFlags);
  }
}
