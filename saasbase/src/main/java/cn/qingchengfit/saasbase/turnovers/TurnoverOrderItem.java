package cn.qingchengfit.saasbase.turnovers;

import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversOrderItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.DrawableUtils;
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
    dataBinding.tvDate.setText(data.getDate());
    boolean hasName = !TextUtils.isEmpty(data.getSellerName());
    dataBinding.tvSellerChange.setVisibility(hasName ? View.VISIBLE : View.GONE);
    dataBinding.tvSellerName.setVisibility(hasName ? View.VISIBLE : View.GONE);
    dataBinding.btnAllot.setVisibility(hasName ? View.GONE : View.VISIBLE);
    dataBinding.tvSellerName.setText(data.getSellerName());

    if (!TurnoversHomePage.trade_types.isEmpty()) {
      TurnoverTradeType turnoverTradeType =
          TurnoversHomePage.trade_types.get(Integer.parseInt(data.getFeatureName()));
      dataBinding.tvFeatureMoney.setText(
          "[" + turnoverTradeType.getDesc() + "] " + data.getMoney() + "元");
      int trade_type = turnoverTradeType.getTrade_type();
      if (trade_type == 1 || trade_type == 2 || trade_type == 17 || trade_type == 16) {
        dataBinding.llSeller.setVisibility(View.VISIBLE);
      } else {
        dataBinding.llSeller.setVisibility(View.GONE);
      }
    }
    if (!TurnoversHomePage.paymentChannels.isEmpty()) {
      TurFilterData turFilterData = TurnoversHomePage.paymentChannels.get(data.getPayType());
      dataBinding.tvType.setText(turFilterData.getShort_text());
      dataBinding.tvType.setBackground(
          DrawableUtils.tintDrawable(dataBinding.tvType.getContext(), R.drawable.circle_green,
              "#" + turFilterData.getColor()));
    }
    dataBinding.tvCheckoutName.setText("收款人："+data.getCheckoutName());
  }
}
