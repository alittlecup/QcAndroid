package cn.qingchengfit.saasbase.course.detail;

import android.graphics.drawable.Drawable;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemScheduleOrderBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class ScheduleCandidateItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemScheduleOrderBinding>> {
  private ScheduleCandidate order;
  private String limitDate;

  public ScheduleCandidate getData() {
    return order;
  }

  public ScheduleCandidateItem(ScheduleCandidate scheduleOrder, String limitDate) {
    this.order = scheduleOrder;
    this.limitDate = limitDate;
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
    dataBinding.tvRemarks.setText(
        "订阅时间：" + DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(order.getCreateAt())));
    String text = "";
    int colorRes = R.color.green;
    dataBinding.tvCancel.setVisibility(View.GONE);
    switch (order.getStatus()) {
      case 1:
        if (DateUtils.isOverCurrent(DateUtils.formatDateFromServer(limitDate))) {
          text = "已过期";
          colorRes = R.color.grey;
        } else {
          text = "已订阅";
          colorRes = R.color.red;
        }
        break;
      case 3:
        text = "预约成功";
        colorRes = R.color.green;
        break;
      case 2:
        text = "已取消预约";
        colorRes = R.color.grey;
        break;
    }
    dataBinding.tvOrderStatus.setText(text);
    Drawable drawable =
        DrawableUtils.tintDrawable(dataBinding.getRoot().getContext(), R.drawable.circle_green,
            colorRes);
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    dataBinding.tvOrderStatus.setCompoundDrawables(drawable, null, null, null);
    dataBinding.imgGender.setImageResource(
        order.getUser().getGender() == 0 ? R.drawable.vd_gender_male : R.drawable.vd_gender_female);
    dataBinding.tvCancel.setOnClickListener(v -> holder.onClick(v));
    holder.getDataBinding().getRoot().setClickable(false);
  }
}
