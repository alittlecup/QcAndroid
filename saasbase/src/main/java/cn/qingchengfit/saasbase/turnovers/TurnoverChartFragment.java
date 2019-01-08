package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    turnoversVM.getChartDatas().observe(this, pair -> {
      upDateChartStat(pair.first, pair.second);
    });
    turnoversVM.totalRate.observe(this, this::setGroupRate);
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initPieChart(mBinding.pieChart);
  }

  private void initPieChart(PieChart chart) {
    chart.setRenderer(
        new TurnoverPieChartRenderer(mBinding.pieChart, mBinding.pieChart.getAnimator(),
            mBinding.pieChart.getViewPortHandler()));
    chart.setUsePercentValues(true);
    chart.getDescription().setEnabled(false);

    chart.setDragDecelerationFrictionCoef(0.95f);
    chart.setExtraOffsets(35.f, 0.f, 35.f, 0.f);
    chart.setDrawHoleEnabled(true);
    chart.setHoleColor(Color.WHITE);

    chart.setTransparentCircleColor(Color.WHITE);
    chart.setTransparentCircleAlpha(110);

    chart.setHoleRadius(62.5f);
    chart.setTransparentCircleRadius(62.5f);

    chart.setDrawCenterText(true);

    chart.setRotationAngle(270);
    chart.setRotationEnabled(false);
    chart.setHighlightPerTapEnabled(true);

    chart.setOnChartValueSelectedListener(this);

    chart.animateY(1400, Easing.EaseInOutQuad);
  }

  public void upDateChartStat(List<ITurnoverChartData> datas, float total) {
    this.total = total;
    if (datas != null && !datas.isEmpty()) {
      List<PieEntry> entries = new ArrayList<>();
      List<Integer> colors = new ArrayList<>();
      for (ITurnoverChartData data : datas) {
        float present = data.getPresent();
        entries.add(new PieEntry(present, data.getLabel()));
        colors.add(Color.parseColor(data.getColor()));
      }

      PieDataSet dataSet = new PieDataSet(entries, "");
      dataSet.setValueLinePart1OffsetPercentage(130f);
      dataSet.setValueLinePart1Length(0.5f);
      dataSet.setValueLinePart2Length(0.6f);
      dataSet.setUsingSliceColorAsValueLineColor(true);
      dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

      dataSet.setSelectionShift(5f);
      dataSet.setSliceSpace(0f);
      dataSet.setColors(colors);
      PieData data = new PieData(dataSet);

      data.setValueFormatter(new PercentFormatter());
      data.setValueTextSize(11f);
      data.setValueTextColor(Color.BLACK);

      mBinding.pieChart.setData(data);
      mBinding.pieChart.highlightValue(null);
      mBinding.pieChart.invalidate();
      mBinding.pieChart.setCenterText("");
      turnoversVM.chartVisible.setValue(true);
    } else {
      turnoversVM.chartVisible.setValue(false);
    }
  }

  private float total = 0f;

  @Override public void onValueSelected(Entry e, Highlight h) {
    String label = ((PieEntry) e).getLabel();
    String[] split = label.split("/");
    String account = split[0].substring(1);
    if (total != 0) {
      float v = Float.valueOf(account) * 100 / total;
      if (v < 0.01f) {
        mBinding.pieChart.setCenterText("<0.01%\n" + split[1]);
      } else {
        DecimalFormat format = new DecimalFormat("#0.00");
        String format1 = format.format(v);
        mBinding.pieChart.setCenterText(format1 + "%\n" + split[1]);
      }
    }
  }

  private void setGroupRate(TurnoversChartStatData total) {
    if (total == null) {
      mBinding.imgRate.setVisibility(View.GONE);
      mBinding.tvCharge.setVisibility(View.GONE);
      return;
    }
    mBinding.tvTotalCount.setText("¥"+ total.getAmount());
    if (turnoversVM.dateType.getValue() != null
        && TurnoversTimeFilterFragment.TimeType.CUSTOMIZE != turnoversVM.dateType.getValue()) {
      float group_value = total.getGrowth_value();
      if (group_value < 0) {
        mBinding.imgRate.setImageDrawable(getResources().getDrawable(R.drawable.ic_turnover_down));
        mBinding.tvCharge.setTextColor(getResources().getColor(R.color.btn_text_primary_color));
      } else {
        mBinding.tvCharge.setTextColor(Color.parseColor("#F03F3F"));
        mBinding.imgRate.setImageDrawable(getResources().getDrawable(R.drawable.ic_turnover_up));
      }
      mBinding.tvCharge.setText("¥" + group_value + "\n" + total.getGrowth_rate() + "%");
      mBinding.imgRate.setVisibility(View.VISIBLE);
      mBinding.tvCharge.setVisibility(View.VISIBLE);
    } else {
      mBinding.imgRate.setVisibility(View.GONE);
      mBinding.tvCharge.setVisibility(View.GONE);
    }
  }

  @Override public void onNothingSelected() {

  }
}
