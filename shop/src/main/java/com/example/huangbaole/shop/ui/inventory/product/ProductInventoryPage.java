package com.example.huangbaole.shop.ui.inventory.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.anbillon.flabellum.annotations.Leaf;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageProductInventoryBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop",path = "/product/inventory")
public class ProductInventoryPage extends ShopBaseFragment<PageProductInventoryBinding,ProductInventoryViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageProductInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductInventoryBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
