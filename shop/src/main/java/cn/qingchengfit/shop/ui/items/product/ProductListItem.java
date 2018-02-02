package cn.qingchengfit.shop.ui.items.product;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemProductListBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import cn.qingchengfit.utils.DateUtils;
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

  public IProductItemData getData() {
    return data;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemProductListBinding> holder, int position, List payloads) {
    ItemProductListBinding dataBinding = holder.getDataBinding();
    dataBinding.productName.setText(data.getProductName());
    dataBinding.productAddDate.setText(DateUtils.getYYYYMMDDfromServer(data.getProductAddTime()));
    SpannableString spannableString = new SpannableString("￥" + data.getProductPrices() + "起");
    spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    spannableString.setSpan(new AbsoluteSizeSpan(18, true), 1, spannableString.length() - 1,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    spannableString.setSpan(new AbsoluteSizeSpan(12, true), spannableString.length() - 1,
        spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.length(),
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    dataBinding.productPrice.setText(spannableString);
    dataBinding.productDetail.setText(dataBinding.getRoot()
        .getContext()
        .getString(R.string.product_detail_format, data.getProductSales(),
            data.getProductInventory(), data.getProductPriority()));
    dataBinding.imageMask.setVisibility(data.getProductStatus() ? View.GONE : View.VISIBLE);
    PhotoUtils.smallCornner4dp(dataBinding.productImage, data.getProductImage());
  }
}
