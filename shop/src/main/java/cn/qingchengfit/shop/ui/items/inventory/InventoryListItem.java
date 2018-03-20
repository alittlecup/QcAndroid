package cn.qingchengfit.shop.ui.items.inventory;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemProductInventoryBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import cn.qingchengfit.shop.util.SpanUtils;
import cn.qingchengfit.shop.util.ViewUtil;
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

  public IInventoryItemData getData() {
    return data;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemProductInventoryBinding> holder, int position, List payloads) {
    ItemProductInventoryBinding dataBinding = holder.getDataBinding();
    Context context = dataBinding.getRoot().getContext();
    dataBinding.productName.setText(data.getName());
    dataBinding.productSaleStatus.setText(
        context.getString(data.getProductStatus() ? R.string.on_sale : R.string.off_sale));
    ViewUtil.smallCornner4dp(dataBinding.productImage, data.getImageUri());
    dataBinding.goodsContent.setText(data.getCategoryDetail());
    SpannableStringBuilder spannableStringBuilder = new SpanUtils().append(data.getInventoryCount())
        .setFontSize(18, true)
        .setForegroundColor(context.getColor(R.color.colorPrimary))
        .append("/" + data.getProductUnit())
        .setFontSize(10, true)
        .setForegroundColor(context.getColor(R.color.colorPrimary))
        .append("(总共)")
        .setFontSize(10, true)
        .setForegroundColor(context.getColor(R.color.text_color_gray))
        .create();
    dataBinding.inventoryCount.setText(spannableStringBuilder);
  }
}
