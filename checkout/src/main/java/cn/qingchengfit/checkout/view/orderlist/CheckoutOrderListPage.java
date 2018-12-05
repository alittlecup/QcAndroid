package cn.qingchengfit.checkout.view.orderlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.databinding.ChCheckoutOrderListBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.ArrayList;

public class CheckoutOrderListPage
    extends CheckoutCounterFragment<ChCheckoutOrderListBinding, CheckoutOrderListVM> {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override
  public ChCheckoutOrderListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return ChCheckoutOrderListBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initRecyclerView();
    initRefreshLayout();
  }

  private void initRefreshLayout() {
    mBinding.swipeLayout.setOnRefreshListener(this::onRefresh);
  }

  private void onRefresh() {
    mViewModel.loadOrderList();
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("二维码收款记录"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
