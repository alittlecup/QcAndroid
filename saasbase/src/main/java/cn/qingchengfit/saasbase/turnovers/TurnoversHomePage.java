package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoversHomePageBinding;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "staff", path = "/turnover/home") public class TurnoversHomePage
    extends SaasBindingFragment<TurnoversHomePageBinding, TurnoversVM> {
  TurnoversFilterFragement filterFragement;
  CommonFlexAdapter adapter;
  TurnoverChartFragment chartFragment;

  @Override protected void subscribeUI() {
    mViewModel.getOrderDatas().observe(this, orderDatas -> {
      if (orderDatas != null && !orderDatas.isEmpty()) {
        List<TurnoverOrderItem> items = new ArrayList<>();
        for (ITurnoverOrderItemData data : orderDatas) {
          items.add(new TurnoverOrderItem(data));
        }
        adapter.updateDataSet(items);
      } else {
        //adapter.updateDataSet();
      }
    });
    mViewModel.filterVisible.observe(this, aBoolean -> {
      if (aBoolean) {
        mBinding.flFilter.setVisibility(View.VISIBLE);
        mBinding.flFilter.setAlpha(0f);
        mBinding.flFilter.animate().alpha(1).setDuration(20).start();
      } else {
        mBinding.flFilter.animate().alpha(0).setDuration(20).withEndAction(new Runnable() {
          @Override public void run() {
            mBinding.flFilter.setVisibility(View.GONE);
          }
        }).start();
      }
    });
  }

  @Override
  public TurnoversHomePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = TurnoversHomePageBinding.inflate(inflater, container, false);
    mBinding.setPage(this);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    filterFragement = new TurnoversFilterFragement();
    chartFragment = new TurnoverChartFragment();
    stuff(filterFragement);
    stuff(R.id.fl_chart, chartFragment);
    initToolbar();
    initRecyclerView();
  }

  public void onDateFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 0);
  }

  public void onFeatureFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 1);
  }

  public void onSellerFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 2);
  }

  public void onPaymentFilter(View view) {
    showFilterView(((QcFilterToggle) view).isChecked(), 3);
  }
  public void onDateArrowLeft(View view){
    mViewModel.
  }
  public void onDateArrowRight(View view){

  }

  public void showFilterView(boolean show, int index) {
    if (show) {
      filterFragement.showPage(index);
      mViewModel.filterVisible.setValue(true);
    } else {
      mViewModel.filterVisible.setValue(false);
    }
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("营业流水"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
  }

  @Override public int getLayoutRes() {
    return R.id.fl_filter;
  }
}
