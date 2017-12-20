package com.example.huangbaole.shop.ui.product.addsuccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageProductAddSuccessBinding;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductAddSuccessPage extends ShopBaseFragment<PageProductAddSuccessBinding,ProductAddSuccessViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageProductAddSuccessBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductAddSuccessBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
