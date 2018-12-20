package cn.qingchengfit.saasbase.turnovers;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversChooseItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class TurnoversFilterItem
    extends AbstractFlexibleItem<DataBindingViewHolder<TurnoversChooseItemBinding>> {
  ITurnoverFilterItemData data;

  public TurnoversFilterItem(ITurnoverFilterItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.turnovers_choose_item;
  }

  @Override public DataBindingViewHolder<TurnoversChooseItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<TurnoversChooseItemBinding> holder, int position, List payloads) {
    TurnoversChooseItemBinding dataBinding = holder.getDataBinding();
    dataBinding.itemText.setText(data.getText());
    dataBinding.itemText.setChecked(adapter.isSelected(position));
  }
}
