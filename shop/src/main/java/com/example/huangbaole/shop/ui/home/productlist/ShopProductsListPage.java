package com.example.huangbaole.shop.ui.home.productlist;

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

  }

  @Override
  public PageProductListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductListBinding.inflate(inflater,container,false);
    mBinding.text.setOnClickListener(view->{
      mBinding.text.toggle();
    });
    return mBinding;
  }
}

