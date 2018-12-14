package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoverChartFragmentBinding;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.views.TurnoverPieChartRenderer;

public class TurnoverChartFragment extends SaasCommonFragment {
  TurnoversVM turnoversVM;
  TurnoverChartFragmentBinding mBinding;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    turnoversVM = ViewModelProviders.of(getParentFragment()).get(TurnoversVM.class);
    mBinding = TurnoverChartFragmentBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initPieChart();
  }

  private void initPieChart() {
    mBinding.pieChart.setRenderer(new TurnoverPieChartRenderer(mBinding.pieChart,mBinding.pieChart.getAnimator(),mBinding.pieChart.getViewPortHandler()));
    mBinding.pieChart.setHoleRadius(64f);
    mBinding.pieChart.setCenterTextColor(getResources().getColor(R.color.text_black));
    mBinding.pieChart.setHighlightPerTapEnabled(false);
    mBinding.pieChart.setClickable(false);
  }
}
