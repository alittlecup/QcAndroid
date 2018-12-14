package cn.qingchengfit.saasbase.turnovers;

import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversOrderItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class TurnoverOrderItem
    extends AbstractFlexibleItem<DataBindingViewHolder<TurnoversOrderItemBinding>> {

  public TurnoverOrderItem(ITurnoverOrderItemData data) {
    this.data = data;
  }

  private ITurnoverOrderItemData data;

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.turnovers_order_item;
  }

  @Override public DataBindingViewHolder<TurnoversOrderItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<TurnoversOrderItemBinding> holder, int position, List payloads) {
    TurnoversOrderItemBinding dataBinding = holder.getDataBinding();
    dataBinding.tvCheckoutName.setText(data.getCheckoutName());
    dataBinding.tvFeatureMoney.setText("[" + data.getFeatureName() + "] " + data.getMoney() + "元");
    dataBinding.tvDate.setText(data.getDate());
    boolean hasName = !TextUtils.isEmpty(data.getSellerName());
    dataBinding.tvSellerChange.setVisibility(hasName ? View.VISIBLE : View.GONE);
    dataBinding.tvSellerName.setVisibility(hasName ? View.VISIBLE : View.GONE);
    dataBinding.btnAllot.setVisibility(hasName ? View.GONE : View.VISIBLE);
    dataBinding.tvSellerName.setText(data.getSellerName());
    dataBinding.tvType.setText(data.getPayType());
  }
}
