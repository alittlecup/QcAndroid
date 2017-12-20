package com.example.huangbaole.shop.ui.product.prices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageProductPricesBinding;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductPricesPage extends ShopBaseFragment<PageProductPricesBinding,ProductPricesViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageProductPricesBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductPricesBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
