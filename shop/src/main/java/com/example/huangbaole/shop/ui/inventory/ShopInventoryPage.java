package com.example.huangbaole.shop.ui.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.anbillon.flabellum.annotations.Leaf;
import com.example.huangbaole.shop.R;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageShopInventoryBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/inventory") public class ShopInventoryPage
    extends ShopBaseFragment<PageShopInventoryBinding, ShopInventoryViewModel> {
  InventoryFilterView filterView;

  @Override protected void subscribeUI() {

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
