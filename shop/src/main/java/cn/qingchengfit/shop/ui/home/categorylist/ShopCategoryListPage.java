package cn.qingchengfit.shop.ui.home.categorylist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageCategoryListBinding;
import cn.qingchengfit.shop.ui.category.ShopCategoryPage;
import cn.qingchengfit.shop.ui.items.category.CategoryListItem;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryListPage
    extends ShopBaseFragment<PageCategoryListBinding, ShopCategoryListViewModel> {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getAddEvent().observe(this, aVoid -> {
      ShopCategoryPage.getInstance(new Category(), ShopCategoryPage.ADD)
          .show(getChildFragmentManager(), "");
    });
    mViewModel.getDeleteEvent().observe(this, category -> {
      ShopCategoryPage.getInstance(category, ShopCategoryPage.DELETE)
          .show(getChildFragmentManager(), "");
    });
    mViewModel.getUpdateEvent().observe(this, category -> {
      ShopCategoryPage.getInstance(category, ShopCategoryPage.UPDATE)
          .show(getChildFragmentManager(), "");
    });
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.items.set(items);
    });
  }

  @Override
  public PageCategoryListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageCategoryListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initRecyclerView();
    return mBinding;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter);
    Category category = new Category();
    category.setPriority(10);
    category.setName("健身餐");
    category.setProduct_count(4);
    List<CategoryListItem> items = new ArrayList<>();
    items.add(new CategoryListItem(category, mViewModel));
    items.add(new CategoryListItem(category, mViewModel));
    mViewModel.items.set(items);
  }
}
