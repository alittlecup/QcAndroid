package com.example.huangbaole.shop.ui.inventory.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.anbillon.flabellum.annotations.Leaf;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageUpdateInventoryBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop",path = "/update/inventory")
public class UpdateInventoryPage extends ShopBaseFragment<PageUpdateInventoryBinding,UpdateInventoryViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageUpdateInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageUpdateInventoryBinding.inflate(inflater);
    return mBinding;
  }
}
