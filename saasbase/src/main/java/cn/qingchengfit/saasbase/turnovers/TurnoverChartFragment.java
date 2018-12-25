package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.TurnoverChartFragmentBinding;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.views.TurnoverPieChartRenderer;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnoverChartFragment extends SaasCommonFragment
    implements OnChartValueSelectedListener {
  TurnoversVM turnoversVM;
  TurnoverChartFragmentBinding mBinding;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    turnoversVM = ViewModelProviders.of(getParentFragment()).get(TurnoversVM.class);
    mBinding = TurnoverChartFragmentBinding.inflate(inflater, container, false);
    turnoversVM.getChartDatas().observe(this, stat -> {
      if (stat != null) {
        upDateChartStat(convertChartStats(stat.stat, stat.total.getAmount()),
            stat.total.getAmount());
      }
    });
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initPieChart(mBinding.pieChart);
  }

  private List<ITurnoverChartData> convertChartStats(List<TurnoversChartStatData> stat,
      float total) {
    List<ITurnoverChartData> datas = new ArrayList<>();
    if (stat != null && !stat.isEmpty()) {
      Collections.sort(stat, (o1, o2) -> (int) (o1.getAmount() * 100 - o2.getAmount() * 100));
      for (TurnoversChartStatData data : stat) {
        TurnoverTradeType turnoverTradeType =
            TurnoversHomePage.trade_types.get(data.getTrade_type());
        if (data.getAmount() > 0 && data.getAmount() * 100 / total <= 1) {
          datas.add(new TurnoverChartStat(total / 100f, turnoverTradeType.getColor(),
              turnoverTradeType.getDesc()));
        } else {
          datas.add(new TurnoverChartStat(data.getAmount(), turnoverTradeType.getColor(),
              turnoverTradeType.getDesc()));
        }
      }
    }
    return datas;
  }

  private void initPieChart(PieChart chart) {
    chart.setRenderer(
        new TurnoverPieChartRenderer(mBinding.pieChart, mBinding.pieChart.getAnimator(),
            mBinding.pieChart.getViewPortHandler()));
    chart.setUsePercentValues(true);
    chart.getDescription().setEnabled(false);

    chart.setDragDecelerationFrictionCoef(0.95f);
    chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
    chart.setDrawHoleEnabled(true);
    chart.setHoleColor(Color.WHITE);

    chart.setTransparentCircleColor(Color.WHITE);
    chart.setTransparentCircleAlpha(110);

    chart.setHoleRadius(70f);
    chart.setTransparentCircleRadius(75f);

    chart.setDrawCenterText(true);

    chart.setRotationAngle(270);
    chart.setRotationEnabled(true);
    chart.setHighlightPerTapEnabled(true);

    chart.setOnChartValueSelectedListener(this);

    chart.animateY(1400, Easing.EaseInOutQuad);
  }

  public void upDateChartStat(List<ITurnoverChartData> datas, float total) {
    if (datas != null && !datas.isEmpty()) {
      List<PieEntry> entries = new ArrayList<>();
      List<Integer> colors = new ArrayList<>();
      for (ITurnoverChartData data : datas) {
        float present = data.getPresent();
        entries.add(new PieEntry(present, data.getLabel()));
        if (present * 100 / total <= 5) {
          colors.add(Color.TRANSPARENT);
        } else {
          colors.add(Color.parseColor(data.getColor()));
        }
      }
      PieDataSet dataSet = new PieDataSet(entries, "");

      dataSet.setValueLinePart1OffsetPercentage(110f);
      dataSet.setValueLinePart1Length(0.4f);
      dataSet.setValueLinePart2Length(0.5f);
      dataSet.setUsingSliceColorAsValueLineColor(true);
      dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

      dataSet.setSelectionShift(3f);
      dataSet.setSliceSpace(0f);
      dataSet.setColors(colors);
      PieData data = new PieData(dataSet);

      data.setValueFormatter(new PercentFormatter());
      data.setValueTextSize(11f);
      data.setValueTextColor(Color.BLACK);

      mBinding.pieChart.setData(data);
      mBinding.pieChart.highlightValue(null);
      mBinding.pieChart.invalidate();
    }
  }

  @Override public void onValueSelected(Entry e, Highlight h) {
    mBinding.pieChart.setCenterText(e.getData() + "" + e.getX());
  }

  @Override public void onNothingSelected() {

  }
}
