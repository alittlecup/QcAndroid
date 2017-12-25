package cn.qingchengfit.shop.ui.inventory.product;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.anbillon.flabellum.annotations.Leaf;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductInventoryBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/product/inventory") public class ProductInventoryPage
    extends ShopBaseFragment<PageProductInventoryBinding, ProductInventoryViewModel> {
  private ProductInventoryFilterView filterView;

  @Override protected void subscribeUI() {
    mViewModel.getAddInventoryEvent().observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/update/inventory");
      routeTo(uri, null);
    });
  }

  @Override
  public PageProductInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductInventoryBinding.inflate(inflater, container, false);
    initFragment();
    mBinding.setViewModel(mViewModel);
    return mBinding;
  }

  private void initFragment() {
    filterView = new ProductInventoryFilterView();
    stuff(R.id.frag_filter, filterView);
  }
}
