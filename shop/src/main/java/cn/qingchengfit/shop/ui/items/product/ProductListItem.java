package cn.qingchengfit.shop.ui.items.product;

import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemProductListBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class ProductListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemProductListBinding>> {
  private IProductItemData data;

  public ProductListItem(IProductItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_product_list;
  }

  @Override public DataBindingViewHolder<ItemProductListBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemProductListBinding> holder, int position, List payloads) {
    ItemProductListBinding dataBinding = holder.getDataBinding();
    dataBinding.productName.setText(data.getProductName());
    dataBinding.productAddDate.setText(data.getProductAddTime());
    dataBinding.productPrice.setText(data.getProductPrices());
    dataBinding.productDetail.setText(dataBinding.getRoot()
        .getContext()
        .getString(R.string.product_detail_format, data.getProductSales(),
            data.getProductInventory(), data.getProductPriority()));
    PhotoUtils.smallCornner4dp(dataBinding.productImage, data.getProductImages()[0]);
  }
}
