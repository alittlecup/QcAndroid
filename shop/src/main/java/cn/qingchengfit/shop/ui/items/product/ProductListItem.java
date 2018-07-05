package cn.qingchengfit.shop.ui.items.product;

import android.graphics.Color;
import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemProductListBinding;
import cn.qingchengfit.shop.util.SpanUtils;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.utils.DateUtils;
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

  public IProductItemData getData() {
    return data;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemProductListBinding> holder, int position, List payloads) {
    ItemProductListBinding dataBinding = holder.getDataBinding();
    dataBinding.productName.setText(data.getProductName());
    dataBinding.productAddDate.setText("添加时间：" + DateUtils.Date2YYYYMMDDHHmm(
        DateUtils.formatDateFromServer(data.getProductAddTime())));
    SpanUtils spanUtils = new SpanUtils().append("￥")
        .setFontSize(12, true)
        .append(data.getProductPrices())
        .setFontSize(18, true);
    if (!data.isSinglePrices()) {
      spanUtils.append("起").setFontSize(12, true);
    }
    dataBinding.productPrice.setText(spanUtils.create());
    dataBinding.productPrice.setTextColor(Color.RED);
    dataBinding.productDetail.setText(dataBinding.getRoot()
        .getContext()
        .getString(R.string.product_detail_format, data.getProductSales(),
            data.getProductInventory(), data.getProductPriority()));
    dataBinding.imageMask.setVisibility(data.getProductStatus() ? View.GONE : View.VISIBLE);
    ViewUtil.smallCornner4dp(dataBinding.productImage, data.getProductImage());
  }
}
