package cn.qingchengfit.checkout.item;

import android.os.Bundle;
import android.view.View;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.bean.CheckoutBill;
import cn.qingchengfit.checkout.bean.OrderListItemData;
import cn.qingchengfit.checkout.bean.PayChannel;
import cn.qingchengfit.checkout.databinding.ChOrderListItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.utils.DrawableUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class OrderListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ChOrderListItemBinding>> {
  OrderListItemData data;

  public OrderListItemData getData() {
    return data;
  }

  public OrderListItem(OrderListItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.ch_order_list_item;
  }

  @Override public DataBindingViewHolder<ChOrderListItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    DataBindingViewHolder<ChOrderListItemBinding> holder =
        new DataBindingViewHolder<ChOrderListItemBinding>(view, adapter);

    return holder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ChOrderListItemBinding> holder, int position, List payloads) {
    ChOrderListItemBinding dataBinding = holder.getDataBinding();
    dataBinding.tvDate.setText(data.getOrderCreateDate());
    dataBinding.tvMoney.setText(data.getOrderMoney() + "元");
    holder.getDataBinding().btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("orderId", ((CheckoutBill)data).getId());
        RouteUtil.routeTo(dataBinding.getRoot().getContext(),"checkout","/order/confirm", bundle);
      }
    });
    switch (data.getType()) {
      case PayChannel.ALIPAY_QRCODE:
        dataBinding.tvType.setBackground(
            DrawableUtils.tintDrawable(dataBinding.getRoot().getContext(),
                R.drawable.ch_order_list_item_circle, R.color.ck_ali_blue));
        dataBinding.tvType.setText("支");

        break;
      case PayChannel.WEIXIN_QRCODE:
        dataBinding.tvType.setBackground(
            DrawableUtils.tintDrawable(dataBinding.getRoot().getContext(),
                R.drawable.ch_order_list_item_circle, R.color.ck_wx_green));
        dataBinding.tvType.setText("微");
        break;
    }
  }
}
