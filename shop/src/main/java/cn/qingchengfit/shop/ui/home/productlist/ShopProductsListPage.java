package cn.qingchengfit.shop.ui.home.productlist;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductListBinding;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsListPage
    extends ShopBaseFragment<PageProductListBinding, ShopProductsViewModel> {
  CommonFlexAdapter adapter;
  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.items.set(items);
    });

    mViewModel.getProductEvent().observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/shop/product");
      routeTo(uri, null);
    });
  }

  @Override
  public PageProductListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductListBinding.inflate(inflater, container, false);
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

