package cn.qingchengfit.shop.ui.home.inventorylist;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageInventoryListBinding;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListPage
    extends ShopBaseFragment<PageInventoryListBinding, ShopInventoryListViewModel> {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getShowAllRecord().observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/shop/inventory");
      routeTo(uri, null);
    });
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.items.set(items);
    });
  }

  @Override
  public PageInventoryListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageInventoryListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initRecyclerView();
    mViewModel.loadSource(mViewModel.getParams());
    return mBinding;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
  }
}
