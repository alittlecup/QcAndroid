package cn.qingchengfit.shop.ui.items.inventory;

import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemProductInventoryBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/16.
 */

public class InventoryListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemProductInventoryBinding>> {
  private IInventoryItemData data;

  public InventoryListItem(IInventoryItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_product_inventory;
  }

  @Override public DataBindingViewHolder<ItemProductInventoryBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemProductInventoryBinding> holder, int position, List payloads) {
    ItemProductInventoryBinding dataBinding = holder.getDataBinding();
    dataBinding.productName.setText(data.getName());
    dataBinding.productSaleStatus.setText(data.getProductStatus());
    PhotoUtils.smallCornner4dp(dataBinding.productImage, data.getImageUri());
    // TODO: 2018/1/16 缺少规格详情数据
  }
}
