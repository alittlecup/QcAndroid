package cn.qingchengfit.shop.ui.home.categorylist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageCategoryListBinding;
import cn.qingchengfit.shop.ui.category.ShopCategoryPage;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryListPage
    extends ShopBaseFragment<PageCategoryListBinding, ShopCategoryListViewModel> {
  CommonFlexAdapter adapter;
  @Override protected void subscribeUI() {
    mViewModel.getAddEvent().observe(this,aVoid -> {
      ShopCategoryPage.getInstance(new Category(),ShopCategoryPage.DELETE).show(getChildFragmentManager(),"");
    });
    mViewModel.getLiveItems().observe(this,items->{
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
    adapter=new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter);
  }
}
