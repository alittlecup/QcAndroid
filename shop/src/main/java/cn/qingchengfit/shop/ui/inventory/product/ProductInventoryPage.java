package cn.qingchengfit.shop.ui.inventory.product;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductInventoryBinding;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/product/inventory") public class ProductInventoryPage
    extends ShopBaseFragment<PageProductInventoryBinding, ProductInventoryViewModel> {
  private ProductInventoryFilterView filterView;
  CommonFlexAdapter adapter;
  @Need String productId;
  Product product;

  @Override protected void subscribeUI() {
    mViewModel.getAddInventoryEvent().observe(this, aVoid -> {
      routeTo("/update/inventory", new UpdateInventoryPageParams().action(UpdateInventoryPage.ADD)
          .productID(product.getId())
          .build());
    });
    mViewModel.getReduceInventoryEvent().observe(this, aVoid -> {
      routeTo("/update/inventory",
          new UpdateInventoryPageParams().action(UpdateInventoryPage.REDUCE)
              .productID(product.getId())
              .build());
    });
    mViewModel.getLiveItems().observe(this, item -> {
      mViewModel.items.set(item);
      hideLoadingTrans();
    });
    mViewModel.fragVisible.observe(this, aBoolean -> {
      mBinding.fragFilter.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
      if(!aBoolean){
        mBinding.qcRadioGroup.clearCheck();
      }
    });

    mViewModel.indexEvent.observe(this, index -> {
      filterView.showPage(index);
    });

    mViewModel.loadProductResult.observe(this, product1 -> {
      this.product = product1;
      initToolbar(product1.getProductName());
    });
  }

  @Override
  public PageProductInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductInventoryBinding.inflate(inflater, container, false);
    initFragment();
    initRecyclerView();
    mBinding.setViewModel(mViewModel);
    mViewModel.loadProduct(productId);
    loadSource();
    mViewModel.loadGoodName(productId);
    initToolbar(mBinding.includeToolbar.toolbar);
    return mBinding;
  }

  private void loadSource() {
    mViewModel.getParams().put("product_id", productId);
    mViewModel.loadSource(mViewModel.getParams());
    showLoadingTrans();
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
  }

  private void initToolbar(String title) {
    mBinding.setToolbarModel(new ToolbarModel(title));
  }

  private void initFragment() {
    filterView = new ProductInventoryFilterView();
    stuff(R.id.frag_filter, filterView);
  }
}
