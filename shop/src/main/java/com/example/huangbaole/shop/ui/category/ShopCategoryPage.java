package com.example.huangbaole.shop.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.anbillon.flabellum.annotations.Leaf;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageShopCategoryBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop",path = "/shop/category/")
public class ShopCategoryPage  extends ShopBaseFragment<PageShopCategoryBinding,ShopCategoryViewModel>{
  @Override protected void subscribeUI() {

  }

  @Override
  public PageShopCategoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageShopCategoryBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
