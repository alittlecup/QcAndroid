package cn.qingchengfit.shop.ui.inventory.product;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductInventoryBinding;
import cn.qingchengfit.shop.vo.Product;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/product/inventory") public class ProductInventoryPage
    extends ShopBaseFragment<PageProductInventoryBinding, ProductInventoryViewModel> {
  private ProductInventoryFilterView filterView;
  @Need Product product;

  @Override protected void subscribeUI() {
    mViewModel.getAddInventoryEvent().observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/update/inventory");
      routeTo(uri, new cn.qingchengfit.shop.ui.inventory.product.UpdateInventoryPageParams().action(
          UpdateInventoryPage.ADD).build());
    });
    mViewModel.getReduceInventoryEvent().observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/update/inventory");
      routeTo(uri, new cn.qingchengfit.shop.ui.inventory.product.UpdateInventoryPageParams().action(
          UpdateInventoryPage.REDUCE).build());
    });
    mViewModel.getLiveItems().observe(this, item -> {
      mViewModel.items.set(item);
    });
    mViewModel.fragVisible.observe(this,
        aBoolean -> mBinding.fragFilter.setVisibility(aBoolean ? View.VISIBLE : View.GONE));

    mViewModel.indexEvent.observe(this, index -> filterView.showPage(index));
  }

  @Override
  public PageProductInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductInventoryBinding.inflate(inflater, container, false);
    initFragment();
    initToolbar();
    mBinding.setViewModel(mViewModel);
    return mBinding;
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(product.getProductName()));
  }

  private void initFragment() {
    filterView = new ProductInventoryFilterView();
    stuff(R.id.frag_filter, filterView);
  }
}
