package com.example.huangbaole.shop.ui.home.productlist;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageProductListBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsListPage extends ShopBaseFragment<PageProductListBinding,ShopProductsViewModel> {
  @Override protected void subscribeUI() {
    mViewModel.getProductEvent().observe(this,aVoid -> {
      Uri uri=Uri.parse("shop://shop/shop/product");
      routeTo(uri,null);
    });
  }

  @Override
  public PageProductListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductListBinding.inflate(inflater,container,false);
    mBinding.setViewModel(mViewModel);
    return mBinding;
  }
}

