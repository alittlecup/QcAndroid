package cn.qingchengfit.card.item;

import android.view.View;
import cn.qingchengfit.card.R;
import cn.qingchengfit.card.bean.UserWithCoupons;
import cn.qingchengfit.card.databinding.CaChooseCouponsItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class ChooseCouponsItem
    extends AbstractFlexibleItem<DataBindingViewHolder<CaChooseCouponsItemBinding>> {
  public UserWithCoupons getData() {
    return data;
  }

  private UserWithCoupons data;

  public ChooseCouponsItem(UserWithCoupons user) {
    this.data = user;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.ca_choose_coupons_item;
  }

  @Override public DataBindingViewHolder<CaChooseCouponsItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<CaChooseCouponsItemBinding> holder, int position, List payloads) {
    CaChooseCouponsItemBinding mDataBinding = holder.getDataBinding();
    PhotoUtils.smallCircle(mDataBinding.imgAvatar, data.getUser().getAvatar());
    mDataBinding.tvName.setText(data.getUser().getUsername());
    mDataBinding.tvCouponsCounts.setText(
        String.format(holder.itemView.getContext().getResources().getString(R.string.coupons_count),
            data.getCoupons().size()));
  }
}
