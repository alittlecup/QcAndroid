package cn.qingchengfit.saasbase.course.detail;

import android.graphics.drawable.Drawable;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemScheduleOrderBinding;
import cn.qingchengfit.saasbase.items.FunHeaderItem;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.ISectionable;
import java.util.List;

public class ScheduleOrderItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemScheduleOrderBinding>>
    implements ISectionable<DataBindingViewHolder<ItemScheduleOrderBinding>, FunHeaderItem> {
  protected ScheduleOrders.ScheduleOrder order;

  public ScheduleOrders.ScheduleOrder getData() {
    return order;
  }
  FunHeaderItem funHeaderItem;
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
    dataBinding.tvPhone.setText("手机：" + order.getUser().getPhone());
    String text = "";
    int colorRes = R.color.green;
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
      case 4:
        text = "已签课";
        colorRes = R.color.iris;
        break;
    }
    dataBinding.tvOrderStatus.setText(text);
    Drawable drawable =
        DrawableUtils.tintDrawable(dataBinding.getRoot().getContext(), R.drawable.circle_green,
            colorRes);
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    dataBinding.tvOrderStatus.setCompoundDrawables(drawable, null, null, null);
    dataBinding.tvCount.setText(order.getCount() + "人");
    dataBinding.tvRemarks.setText("备注:" + order.getRemarks());
    dataBinding.imgGender.setImageResource(
        order.getUser().getGender() == 0 ? R.drawable.vd_gender_male : R.drawable.vd_gender_female);
    dataBinding.tvCancel.setOnClickListener(v -> holder.onClick(v));
    holder.getDataBinding().getRoot().setClickable(false);
  }

  @Override public FunHeaderItem getHeader() {
    return funHeaderItem;
  }

  @Override public void setHeader(FunHeaderItem header) {
    this.funHeaderItem=header;
  }
}
