package cn.qingchengfit.saasbase.course.detail;

import android.graphics.drawable.Drawable;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemScheduleOrderBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class ScheduleOrderItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemScheduleOrderBinding>> {
  private ScheduleOrders.ScheduleOrder order;
  public ScheduleOrders.ScheduleOrder getData(){
    return order;
  }

  public ScheduleOrderItem(ScheduleOrders.ScheduleOrder scheduleOrder) {
    this.order = scheduleOrder;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_schedule_order;
  }

  @Override public DataBindingViewHolder<ItemScheduleOrderBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemScheduleOrderBinding> holder, int position, List payloads) {
    ItemScheduleOrderBinding dataBinding = holder.getDataBinding();
    PhotoUtils.smallCircle(dataBinding.imgMemberPhoto, order.getUser().getAvatar());
    dataBinding.tvName.setText(order.getUser().getUsername());
    dataBinding.tvPhone.setText("手机" + order.getUser().getPhone());
    String text = "";
    int colorRes = -1;
    dataBinding.tvCancel.setVisibility(View.VISIBLE);
    switch (order.getStatus()) {
      case 0:
        text = "已预约";
        colorRes = R.color.red;
        break;
      case 1:
        text = "已完成";
        colorRes = R.color.green;
        break;
      case 2:
        text = "已取消";
        colorRes = R.color.grey;
        dataBinding.tvCancel.setVisibility(View.GONE);
        break;
      case 5:
        text = "已签课";
        colorRes = R.color.iris;
        break;
    }
    dataBinding.tvOrderStatus.setText(text);
    Drawable drawable =
        DrawableUtils.tintDrawable(dataBinding.getRoot().getContext(), R.drawable.circle_green,
            colorRes);
    dataBinding.tvOrderStatus.setCompoundDrawables(drawable, null, null, null);
    dataBinding.tvCancel.setOnClickListener(v -> holder.onClick(v));
    holder.getDataBinding().getRoot().setClickable(false);
  }
}
