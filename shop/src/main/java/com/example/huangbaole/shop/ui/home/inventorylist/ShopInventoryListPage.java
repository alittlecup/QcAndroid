package com.example.huangbaole.shop.ui.home.inventorylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageInventoryListBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListPage
    extends ShopBaseFragment<PageInventoryListBinding, ShopInventoryListViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageInventoryListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageInventoryListBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
