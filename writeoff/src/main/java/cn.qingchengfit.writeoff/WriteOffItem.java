package cn.qingchengfit.writeoff;

import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.writeoff.databinding.WrItemWriteOffBinding;
import cn.qingchengfit.writeoff.vo.IWriteOffItemData;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class WriteOffItem
    extends AbstractFlexibleItem<DataBindingViewHolder<WrItemWriteOffBinding>> {
  IWriteOffItemData data;

  public WriteOffItem(IWriteOffItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.wr_item_write_off;
  }

  @Override public DataBindingViewHolder<WrItemWriteOffBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<WrItemWriteOffBinding> holder, int position, List payloads) {
    WrItemWriteOffBinding dataBinding = holder.getDataBinding();
    dataBinding.tvTicketName.setText(data.getTicketName());
    dataBinding.tvTicketMoney.setText(String.format(
        dataBinding.getRoot().getResources().getString(R.string.wr_write_off_ticker_money),
        data.getTicketMoney()));
    dataBinding.tvTicketTime.setText(
        String.format(dataBinding.getRoot().getResources().getString(R.string.
            wr_write_off_ticker_time), data.getTicketCheckTime()));
  }

  public IWriteOffItemData getData() {
    return data;
  }
}
