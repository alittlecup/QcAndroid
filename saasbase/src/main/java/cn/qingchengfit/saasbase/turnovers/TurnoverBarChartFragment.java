package cn.qingchengfit.saasbase.turnovers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoverBarChartFragmentBinding;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.model.MyXAxisValueFormatter;
import cn.qingchengfit.saascommon.model.NumChartYValueFormatter;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.widget.MyMarkerView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

public class TurnoverBarChartFragment extends SaasCommonFragment
    implements OnChartValueSelectedListener {
  TurnoverBarChartFragmentBinding mBinding;
  @Inject IPermissionModel permissionModel;
  @Inject IStaffModel staffModel;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = TurnoverBarChartFragmentBinding.inflate(inflater, container, false);
    loadFilterOptions();
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initBarChart(mBinding.barChart);
    mBinding.chartDetail.setLabel("营业流水");
    mBinding.chartDetail.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (permissionModel.check(PermissionServerUtils.MODULE_SHOP_TURNOVER)) {
          routeTo("staff", "/turnover/home", null);
        } else {
          DialogUtils.showAlert(getContext(), R.string.alert_permission_forbid);
        }
      }
    });
  }

  public void loadFilterOptions() {
    staffModel.qcGetTurnoversFilterItems()
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            updateTradeTypes(response.data.trade_types);
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> {
        });
  }

  private Map<Integer, TurnoverTradeType> tradeTypes = new HashMap<>();
  private List<Integer> colors;

  private void updateTradeTypes(List<TurnoverTradeType> tradeTypes) {
    if (tradeTypes != null && !tradeTypes.isEmpty()) {
      colors = new ArrayList<>();
      for (TurnoverTradeType type : tradeTypes) {
        this.tradeTypes.put(type.getTrade_type(), type);
        colors.add(Color.parseColor("#" + type.getColor()));
      }
      if (data != null) {
        setBarChartData(data);
      }
    }
  }

  private void initBarChart(BarChart chart) {
    chart.setOnChartValueSelectedListener(this);

    chart.getDescription().setEnabled(false);

    chart.setMaxVisibleValueCount(40);

    chart.setPinchZoom(false);

    chart.setDrawGridBackground(false);
    chart.setDrawBarShadow(false);

    chart.setDrawValueAboveBar(true);
    chart.setHighlightFullBarEnabled(true);

    chart.setDragEnabled(false);
    chart.setScaleEnabled(false);

    XAxis xAxis = chart.getXAxis();
    ArrayList<String> xValues = new ArrayList<>();
    xValues.add("");
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DATE, -7);
    for (int i = 0; i < 7; i++) {
      c.add(Calendar.DATE, 1);
      xValues.add(DateUtils.Date2YYYYMMDD(c.getTime()));
    }
    xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
    xAxis.removeAllLimitLines();
    xAxis.enableGridDashedLine(10f, 10f, 0f);
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setGridColor(Color.TRANSPARENT);// 去掉网格中竖线的显示
    xAxis.setLabelCount(2, true);
    xAxis.setDrawLabels(true);
    xAxis.setTextColor(Color.parseColor("#999999"));
    xAxis.setAxisLineColor(Color.parseColor("#dddddd"));
    xAxis.setYOffset(MeasureUtils.dpToPx(3f, getResources()));
    YAxis leftAxis = chart.getAxisLeft();
    leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
    //leftAxis.setAxisMaximum(Ymax == 0 ? 5 : (Ymax / 3f) * 4f);//Y轴最大高度

    leftAxis.setDrawZeroLine(false);//设置Y轴坐标是否从0开始
    leftAxis.setStartAtZero(true);
    // limit lines are drawn behind data (and not on top)
    leftAxis.setDrawLimitLinesBehindData(false);
    leftAxis.setLabelCount(5, true);
    leftAxis.setTextColor(Color.parseColor("#999999"));
    leftAxis.setAxisLineColor(Color.TRANSPARENT);
    leftAxis.setGridColor(Color.parseColor("#dddddd"));
    leftAxis.setSpaceBottom(MeasureUtils.dpToPx(10f, getResources()));
    leftAxis.setDrawAxisLine(false);
    leftAxis.setXOffset(MeasureUtils.dpToPx(5f, getResources()));
    leftAxis.setValueFormatter(new NumChartYValueFormatter());
    chart.setExtraBottomOffset(35f);

    chart.setExtraLeftOffset(0);
    chart.getAxisRight().setEnabled(false);

    MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view, getResources().getColor(R.color.toolbar), "");
    mv.setChartView(chart); // For bounds control
    chart.setMarker(mv); // Set the marker to the chart
    chart.getLegend().setEnabled(false);
  }

  private List<TurBarChartData> data;

  public void setBarChartData(List<TurBarChartData> data) {
    List<ITurnoverBarChartData> chartDatas = new ArrayList<>();
    if (data != null && !data.isEmpty()) {
      this.data = data;
      if (colors != null && !tradeTypes.isEmpty()) {
        Collections.reverse(data);
        for (int i = 0; i < data.size(); i++) {
          TurBarChartData turBarChartData = data.get(i);
          if (turBarChartData.getTotal().getAmount() <= 0) {
            chartDatas.add(new TurBarData(i, new float[]{0},turBarChartData.getDate()));
          } else {
            chartDatas.add(new TurBarData(i, getChartFloat(turBarChartData.getStat(), tradeTypes),turBarChartData.getDate()));
          }
        }
      }
      upDataChartData(chartDatas);
    }
  }

  private float[] getChartFloat(List<TurnoversChartStatData> stat,
      Map<Integer, TurnoverTradeType> tradeTypes) {
    float[] chartYs = new float[tradeTypes.size()];
    Set<Integer> integers = tradeTypes.keySet();
    Integer[] ts = integers.toArray(new Integer[integers.size()]);
    for (int i = 0; i < ts.length; i++) {
      for (TurnoversChartStatData data : stat) {
        if (ts[i] == data.getTrade_type()) {
          chartYs[i] = data.getAmount();
        }
      }
    }
    return chartYs;
  }

  private void upDataChartData(List<ITurnoverBarChartData> data) {
    if (data != null && !data.isEmpty()) {
      List<BarEntry> barEntries = new ArrayList<>();
      for (ITurnoverBarChartData data1 : data) {
        barEntries.add(new BarEntry(data1.getX(), data1.getY(),data1.getData()));
      }
      BarDataSet set1;
      if (mBinding.barChart.getData() != null
          && mBinding.barChart.getData().getDataSetCount() > 0) {
        set1 = (BarDataSet) mBinding.barChart.getData().getDataSetByIndex(0);
        set1.setValues(barEntries);
        mBinding.barChart.getData().notifyDataChanged();
        mBinding.barChart.notifyDataSetChanged();
      } else {
        set1 = new BarDataSet(barEntries, "");
        set1.setColors(colors);
        set1.setHighLightAlpha(0);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData barData = new BarData(dataSets);
        barData.setValueFormatter(new StackedValueFormatter(false, "", 1));
        barData.setValueTextColor(Color.WHITE);

        mBinding.barChart.setData(barData);
      }

      mBinding.barChart.invalidate();
    }
  }

  @Override public void onValueSelected(Entry e, Highlight h) {

  }

  @Override public void onNothingSelected() {

  }
}
