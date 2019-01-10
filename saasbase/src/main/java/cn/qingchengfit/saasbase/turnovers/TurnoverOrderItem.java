package cn.qingchengfit.saasbase.turnovers;

import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversOrderItemBinding;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DrawableUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class TurnoverOrderItem
    extends AbstractFlexibleItem<DataBindingViewHolder<TurnoversOrderItemBinding>> {

  public TurnoverOrderItem(ITurnoverOrderItemData data) {
    this.data = data;
  }

  public ITurnoverOrderItemData getData() {
    return data;
  }

  public void setData(ITurnoverOrderItemData data) {
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
    DataBindingViewHolder<TurnoversOrderItemBinding> holder =
        new DataBindingViewHolder<>(view, adapter);

    return holder;
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
      if(turnoverTradeType==null)return;
      dataBinding.tvFeatureMoney.setText(
          "[" + turnoverTradeType.getDesc() + "] " + data.getMoney() + "元");
    }
    if (!TurnoversHomePage.paymentChannels.isEmpty()) {
      TurFilterData turFilterData = TurnoversHomePage.paymentChannels.get(data.getPayType());
      if(turFilterData==null)return;
      dataBinding.tvType.setText(turFilterData.getShort_text());
      dataBinding.tvType.setBackground(
          DrawableUtils.tintDrawable(dataBinding.tvType.getContext(), R.drawable.circle_green,
              "#" + turFilterData.getColor()));
    }
    if (TextUtils.isEmpty(data.getCheckoutName())) {
      dataBinding.tvCheckoutName.setText("操作人：");
    } else {
      dataBinding.tvCheckoutName.setText("操作人：" + data.getCheckoutName());
    }
    holder.getDataBinding().btnAllot.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null) {
          listener.onItemClick(holder.getDataBinding().getRoot(), position);
        }
      }
    });
    holder.getDataBinding().tvSellerChange.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RouteUtil.routeTo(dataBinding.getRoot().getContext(), "staff", "/turnover/order",
            new BundleBuilder().withString("turId", data.getID()).build());
      }
    });
  }

  public void setListener(OnRecycleItemClickListener listener) {
    this.listener = listener;
  }

  private OnRecycleItemClickListener listener;
}
