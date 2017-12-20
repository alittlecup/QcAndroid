package com.example.huangbaole.shop.ui.product.deliverchannel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageProductDeliverBinding;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductDeliverPage extends ShopBaseFragment<PageProductDeliverBinding,ProductDeliverViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageProductDeliverBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductDeliverBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
