package cn.qingchengfit.shop.ui.items.inventory;

import android.graphics.Paint;
import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemInventorySingleTextBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/3/20.
 */

public class InventorySingleTextItem extends
    AbstractFlexibleItem<DataBindingViewHolder<ItemInventorySingleTextBinding>> {
  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_inventory_single_text;
  }

  @Override public DataBindingViewHolder<ItemInventorySingleTextBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view,adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemInventorySingleTextBinding> holder, int position, List payloads) {
    ItemInventorySingleTextBinding dataBinding = holder.getDataBinding();
    dataBinding.allInventoryRecord.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    dataBinding.allInventoryRecord.getPaint().setAntiAlias(true);
  }
}
