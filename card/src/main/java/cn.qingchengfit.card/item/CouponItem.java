package cn.qingchengfit.card.item;

import android.view.View;
import cn.qingchengfit.card.R;
import cn.qingchengfit.saasbase.cards.bean.Coupon;
import cn.qingchengfit.card.databinding.CaCouponItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class CouponItem extends AbstractFlexibleItem<DataBindingViewHolder<CaCouponItemBinding>> {
  private Coupon data;

  public CouponItem(Coupon coupon) {
    this.data = coupon;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.ca_coupon_item;
  }

  @Override public DataBindingViewHolder<CaCouponItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<CaCouponItemBinding> holder, int position, List payloads) {
    CaCouponItemBinding mBinding = holder.getDataBinding();
    mBinding.tvName.setText(data.getDescription());
    switch (data.getCoupon_tpl().getCoupon_type()) {
      case 1:
        mBinding.tvTips.setText("代金");
        mBinding.tvTotal.setText(data.getCoupon_tpl().getAccount() + "元");
        break;
      case 2:
        mBinding.tvTips.setText("满减");
        mBinding.tvTotal.setText(data.getCoupon_tpl().getAccount() + "元");
        break;
      case 3:
        mBinding.tvTips.setText("折扣");
        mBinding.tvTotal.setText(data.getCoupon_tpl().getAccount() + "折");

        break;
    }
    StringBuilder stringBuilder = new StringBuilder("还剩");
    if (DateUtils.isOverCurrent(DateUtils.formatDateFromServer(data.getStart()))) {
      int interval = DateUtils.interval(data.getEnd(), data.getStart());
      stringBuilder.append(interval);
    } else {
      int i = DateUtils.dayNumFromToday(DateUtils.formatDateFromServer(data.getEnd()));
      stringBuilder.append(i);
    }
    stringBuilder.append("天");
    mBinding.tvDates.setText(
        new StringBuilder().append(DateUtils.getYYYYMMDDfromServer(data.getStart()))
            .append("-")
            .append(DateUtils.getYYYYMMDDfromServer(data.getEnd())).append(stringBuilder));
    mBinding.tvDesc.setText(data.getCoupon_tpl().getUse_condition().getDescription());
  }
}
