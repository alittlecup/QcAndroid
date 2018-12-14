package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
  }

  @Override
  public TurnoversHomePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = TurnoversHomePageBinding.inflate(inflater, container, false);
    mBinding.setPage(this);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Log.d("TAG", "onViewCreated: " + view);
    filterFragement = new TurnoversFilterFragement();
    chartFragment = new TurnoverChartFragment();
    stuff(filterFragement);
    stuff(R.id.fl_chart, chartFragment);
    initToolbar();
    initRecyclerView();
  }

  public void onQcRadioGroupButtonClick(View view, int index) {
    if (((QcFilterToggle) view).isChecked()) {
      filterFragement.dismiss();
      mBinding.flFilter.setVisibility(View.GONE);
    } else {
      filterFragement.showPage(index);
      mBinding.flFilter.setVisibility(View.VISIBLE);
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
