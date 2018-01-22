package cn.qingchengfit.shop.ui.items.inventory;

import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemInventoryRecordBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/16.
 */

public class InventoryRecordItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemInventoryRecordBinding>> {
  private IInventoryRecordData data;

  public InventoryRecordItem(IInventoryRecordData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_inventory_record;
  }

  @Override public DataBindingViewHolder<ItemInventoryRecordBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemInventoryRecordBinding> holder, int position, List payloads) {

  }
}
