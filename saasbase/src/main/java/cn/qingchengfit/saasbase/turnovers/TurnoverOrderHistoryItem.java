package cn.qingchengfit.saasbase.turnovers;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoverOrderHistoryItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class TurnoverOrderHistoryItem
    extends AbstractFlexibleItem<DataBindingViewHolder<TurnoverOrderHistoryItemBinding>> {
  ITurOrderHistoryData data;

  public TurnoverOrderHistoryItem(ITurOrderHistoryData history) {
    this.data = history;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.turnover_order_history_item;
  }

  @Override
  public DataBindingViewHolder<TurnoverOrderHistoryItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<TurnoverOrderHistoryItemBinding> holder, int position, List payloads) {
    TurnoverOrderHistoryItemBinding dataBinding = holder.getDataBinding();
    dataBinding.tvDate.setText(data.getDate());
    dataBinding.tvChangeBy.setText("by "+data.getSellerName());
    dataBinding.tvSellerName.setText(data.getChangeByName());
  }
}
