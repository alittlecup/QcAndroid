package com.example.huangbaole.shop.ui.home.categorylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.huangbaole.shop.base.ShopBaseFragment;
import com.example.huangbaole.shop.databinding.PageCategoryListBinding;
import com.example.huangbaole.shop.ui.category.ShopCategoryPage;
import com.example.huangbaole.shop.vo.Category;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryListPage
    extends ShopBaseFragment<PageCategoryListBinding, ShopCategoryListViewModel> {
  @Override protected void subscribeUI() {
    mViewModel.getAddEvent().observe(this,aVoid -> {
      ShopCategoryPage.getInstance(new Category(),ShopCategoryPage.DELETE).show(getChildFragmentManager(),"");
    });
  }

  @Override
  public PageCategoryListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageCategoryListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    return mBinding;
  }
}
