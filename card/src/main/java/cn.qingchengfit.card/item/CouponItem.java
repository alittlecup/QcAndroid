package cn.qingchengfit.card.item;

import android.view.View;
import cn.qingchengfit.card.R;
import cn.qingchengfit.card.bean.Coupon;
import cn.qingchengfit.card.databinding.CaCouponItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
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
    //mBinding.tvDesc.setText();
  }
}
