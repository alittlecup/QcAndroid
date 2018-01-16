package cn.qingchengfit.shop.ui.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopInventoryBinding;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/inventory") public class ShopInventoryPage
    extends ShopBaseFragment<PageShopInventoryBinding, ShopInventoryViewModel> {
  InventoryFilterView filterView;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, item -> {
      mViewModel.items.set(item);
    });
    mViewModel.indexEvent.observe(this, index -> {
      filterView.showPage(index);
    });
    mViewModel.fragVisible.observe(this,aBoolean -> {
      mBinding.fragFilter.setVisibility(aBoolean? View.VISIBLE:View.GONE);
    });
  }

  @Override
  public PageShopInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopInventoryBinding.inflate(inflater, container, false);
    initFragment();
    return mBinding;
  }

  private void initFragment() {
    filterView = new InventoryFilterView();
    stuff(R.id.frag_filter, filterView);
  }
}
