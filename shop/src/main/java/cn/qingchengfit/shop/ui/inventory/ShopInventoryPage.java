package cn.qingchengfit.shop.ui.inventory;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopInventoryBinding;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/inventory") public class ShopInventoryPage
    extends ShopBaseFragment<PageShopInventoryBinding, ShopInventoryViewModel> {
  InventoryFilterView filterView;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, item -> {
      mViewModel.items.set(item);
    });
    mViewModel.indexEvent.observe(this, index -> {
      filterView.showPage(index);
    });
    mViewModel.fragVisible.observe(this, aBoolean -> {
      mBinding.fragFilter.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
      if(!aBoolean){
        mBinding.qcRadioGroup.clearCheck();
      }
    });
  }

  @Override
  public PageShopInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopInventoryBinding.inflate(inflater, container, false);
    initFragment();
    initToolbar();
    initRecyclerView();
    mBinding.setViewModel(mViewModel);
    mViewModel.loadSource(mViewModel.getParams());
    return mBinding;
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter);
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.inventory_reocrd)));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initFragment() {
    filterView = new InventoryFilterView();
    stuff(R.id.frag_filter, filterView);
  }
}
