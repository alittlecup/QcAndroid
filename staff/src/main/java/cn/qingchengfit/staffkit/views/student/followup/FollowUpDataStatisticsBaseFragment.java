package cn.qingchengfit.staffkit.views.student.followup;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.model.responese.AttendanceCharDataBean;
import cn.qingchengfit.saascommon.model.FollowUpDataStatistic;
import cn.qingchengfit.saascommon.model.MyXAxisValueFormatter;
import cn.qingchengfit.saascommon.widget.MyMarkerView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.attendance.MyYValueFormatter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import timber.log.Timber;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/5.
 */
public abstract class FollowUpDataStatisticsBaseFragment extends BaseFragment
    implements OnChartGestureListener, OnChartValueSelectedListener {

  public boolean touchable;

  LineChart lineChart;
  TextView tvTotalCountLable;
  TextView tvTotalCount;
  TextView tvStartDay;
  TextView tvEndDay;
  List<FollowUpDataStatistic.DateCountsBean> datas = new ArrayList<>();
  List<FollowUpDataStatistic.DateCountsBean> datasExpand = new ArrayList<>();
  private int Ymax;
  private boolean isAttendance = false;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutResouseId(), container, false);
    lineChart = view.findViewById(R.id.lineChart);
    tvTotalCountLable = view.findViewById(R.id.tv_total_count_lable);
    tvTotalCount = view.findViewById(R.id.tv_total_count);
    tvStartDay = view.findViewById(R.id.start_day);
    tvEndDay = view.findViewById(R.id.end_day);
    initView();
    return view;
  }

  public abstract int getLayoutResouseId();

  public abstract int getLineColor();

  public abstract int getFillColor();

  public abstract int getStatus();

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  public void setAttendance(boolean attendance) {
    isAttendance = attendance;
  }

  private void initView() {

    lineChart.setOnChartGestureListener(this);
    lineChart.setOnChartValueSelectedListener(this);
    lineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
    lineChart.setDrawGridBackground(false);
    lineChart.setDrawBorders(false);  //是否在折线图上添加边框

    // no description text
    lineChart.getDescription().setEnabled(false);
    // 如果没有数据的时候，会显示这个，类似listview的emtpyview
    lineChart.setNoDataText("暂无数据");
    lineChart.setNoDataTextColor(Color.parseColor("#6eb8f1"));

    // enable touch gestures
    lineChart.setTouchEnabled(touchable);
    // enable scaling and dragging
    lineChart.setDragEnabled(true);
    lineChart.setScaleEnabled(false);
    // if disabled, scaling can be done on x- and y-axis separately
    lineChart.setPinchZoom(false);

    lineChart.setDragDecelerationEnabled(false);

    // create a custom MarkerView (extend MarkerView) and specify the layout to use for it
    if (isAttendance) {
      MyMarkerView mv =
          new MyMarkerView(getActivity(), R.layout.custom_marker_view, getLineColor(), "人次");
      mv.setChartView(lineChart); // For bounds control
      lineChart.setMarker(mv); // Set the marker to the chart
    } else {
      MyMarkerView mv =
          new MyMarkerView(getActivity(), R.layout.custom_marker_view, getLineColor());
      mv.setChartView(lineChart); // For bounds control
      lineChart.setMarker(mv); // Set the marker to the chart
    }

    XAxis xAxis = lineChart.getXAxis();
    xAxis.removeAllLimitLines();
    xAxis.enableGridDashedLine(10f, 10f, 0f);
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setGridColor(Color.TRANSPARENT);// 去掉网格中竖线的显示
    //        xAxis.setLabelCount(0, true);
    xAxis.setDrawLabels(false);
    xAxis.setTextColor(Color.parseColor("#999999"));
    xAxis.setXOffset(20);
    xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

    YAxis leftAxis = lineChart.getAxisLeft();
    leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
    leftAxis.setAxisMaximum(Ymax == 0 ? 5 : (Ymax / 2f) * 3f);//Y轴最大高度
    leftAxis.setSpaceTop(10);
    leftAxis.setStartAtZero(true);//设置Y轴坐标是否从0开始

    // limit lines are drawn behind data (and not on top)
    leftAxis.setDrawLimitLinesBehindData(false);
    leftAxis.setLabelCount(4, true);
    leftAxis.setTextColor(Color.parseColor("#999999"));
    leftAxis.setAxisLineColor(Color.TRANSPARENT);
    leftAxis.setGridColor(Color.parseColor("#dddddd"));

    lineChart.getAxisRight().setEnabled(false);
    lineChart.animateX(500);

    // get the legend (only possible after setting data)
    Legend l = lineChart.getLegend();

    // modify the legend ...
    l.setForm(Legend.LegendForm.NONE);

    //返回 能见到的X的最小值下标 返回值是int类型
    //lineChart.getLowestVisibleX();
    //返回 能见到的X的最大值下标 返回值是int类型
    //lineChart.getHighestVisibleX();

    // // dont forget to refresh the drawing
    // mChart.invalidate();
  }

  private void setData(int offDays) {
    // 设置X轴现实数据
    ArrayList<String> xValues = new ArrayList<String>();

    FollowUpDataStatistic.DateCountsBean bean = new FollowUpDataStatistic.DateCountsBean();

    datasExpand.clear();
    datasExpand.addAll(datas);

    Date date = DateUtils.formatDateFromYYYYMMDD(datas.get(0).date);
    String dateStr = DateUtils.Date2MMDD(date);
    xValues.add(dateStr);
    xValues.add(dateStr);
    xValues.add(dateStr);

    bean.count = "0";
    bean.date = dateStr;
    datasExpand.add(0, bean);
    datasExpand.add(1, bean);
    datasExpand.add(2, bean);

    for (int i = 0; i < datas.size(); i++) {
      // x轴显示的数据
      date = DateUtils.formatDateFromYYYYMMDD(datas.get(i).date);
      dateStr = DateUtils.Date2MMDD(date);
      xValues.add(dateStr);
    }

    date = DateUtils.formatDateFromYYYYMMDD(datas.get(datas.size() - 1).date);
    dateStr = DateUtils.Date2MMDD(date);
    xValues.add(dateStr);
    xValues.add(dateStr);
    xValues.add(dateStr);

    bean.count = "0";
    bean.date = dateStr;
    datasExpand.add(bean);
    datasExpand.add(bean);
    datasExpand.add(bean);

    XAxis xAxis = lineChart.getXAxis();
    xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
    xAxis.setAvoidFirstLastClipping(true);
    // 折线图数据
    ArrayList<Entry> values = new ArrayList<Entry>();

    for (int i = 0; i < datas.size(); i++) {
      values.add(new Entry(i + 3, Float.parseFloat(datas.get(i).count), datas.get(i).date));
    }

    LineDataSet set1;

    ArrayList<Entry> valuesSet2 = new ArrayList<Entry>();
    for (int i = 0; i < datasExpand.size(); i++) {
      valuesSet2.add(new Entry(i, -1, datasExpand.get(i).date));
    }
    LineDataSet set2;

    if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
      set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
      set1.setValues(values);
      if (isAttendance) {
        set1.setValueFormatter(new IValueFormatter() {
          @Override public String getFormattedValue(float value, Entry entry, int dataSetIndex,
              ViewPortHandler viewPortHandler) {
            return value + "人次";
          }
        });
      }

      set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
      set2.setValues(valuesSet2);

      lineChart.getData().notifyDataChanged();
      lineChart.notifyDataSetChanged();
    } else {
      // create a dataset and give it a type
      set1 = new LineDataSet(values, null);

      set2 = new LineDataSet(valuesSet2, null);
      //set2.setColor(Color.TRANSPARENT);
      //set2.setCircleColor(Color.TRANSPARENT);
      //set2.setHighLightColor(Color.TRANSPARENT);
      //set2.setHighlightEnabled(false);
      //set2.setDrawValues(false);

      set1.setColor(getLineColor());
      set1.setCircleColor(getLineColor());
      set1.setHighLightColor(getLineColor());
      set1.setHighlightLineWidth(2);
      set1.setDrawHorizontalHighlightIndicator(false);
      set1.setDrawCircleHole(true);//
      set1.setLineWidth(2f);
      set1.setCircleRadius(4f);
      set1.setValueTextSize(9f);

      set1.setDrawFilled(true);
      if (isAttendance) {
        set1.setValueFormatter(new MyYValueFormatter());
      }
      set1.setDrawValues(false);
      if (Utils.getSDKInt() >= 18) {
        // fill drawable only supported on api level 18 and above
        Drawable drawable = null;
        switch (getStatus()) {
          case 0:
            drawable = ContextCompat.getDrawable(getActivity(), R.drawable.shape_fade_line_chart_0);
            break;
          case 1:
            drawable = ContextCompat.getDrawable(getActivity(), R.drawable.shape_fade_line_chart_1);
            break;
          case 2:
            drawable = ContextCompat.getDrawable(getActivity(), R.drawable.shape_fade_line_chart_2);
            break;
        }

        set1.setFillDrawable(drawable);
      } else {
        set1.setFillColor(getFillColor());
      }

      ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
      dataSets.add(set1); // add the datasets
      dataSets.add(set2); // add the datasets

      // create a data object with the datasets
      LineData data = new LineData(dataSets);
      // set data
      lineChart.setData(data);
      // lineChart.invalidate();
    }
    // 设置X轴最大的显示范围
    lineChart.setVisibleXRangeMaximum(6);
    // 设置X轴最小的显示范围
    lineChart.setVisibleXRangeMinimum(6);
    // 将图的左边移动到指定的index处
    lineChart.moveViewToX(datas.size() - 4);
    lineChart.highlightValue(offDays + 2, 0);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  public void setData(FollowUpDataStatistic.NewCreateUsersBean data, int offDays) {
    if (tvTotalCountLable == null) return;
    switch (getStatus()) {
      case 0:
        tvTotalCountLable.setText("最近" + offDays + "天新增注册共计");
        break;
      case 1:
        tvTotalCountLable.setText("最近" + offDays + "天跟进会员共计");
        break;
      case 2:
        tvTotalCountLable.setText("最近" + offDays + "天新增会员共计");
        break;
    }
    tvTotalCount.setText(data.count);
    // TODO 补全数据
    getDefaultList(offDays);
    doData(data.date_counts);
    getDataListMax(data.date_counts);
    initView();
    tvStartDay.setText(DateUtils.Date2MMDD(DateUtils.addDay(new Date(), -offDays)));
    tvEndDay.setText(DateUtils.Date2MMDD(new Date()));
    setData(offDays);
  }

  //用在缺勤筛选的首页
  public void setData(AttendanceCharDataBean data, int offDays) {
    //        switch (getStatus()) {
    //            case 0:
    //                tvTotalCountLable.setText("最近" + offDays + "天新增注册共计");
    //                break;
    //            case 1:
    //                tvTotalCountLable.setText("最近" + offDays + "天跟进会员共计");
    //                break;
    //            case 2:
    //                tvTotalCountLable.setText("最近" + offDays + "天新增会员共计");
    //                break;
    //        }
    //        tvTotalCount.setText(data.datas.size());
    // TODO 补全数据
    getDefaultList(offDays);
    doData(data.datas);
    getDataListMax(data.datas);
    initView();
    tvStartDay.setText(DateUtils.Date2MMDD(DateUtils.addDay(new Date(), -offDays)));
    tvEndDay.setText(DateUtils.Date2MMDD(new Date()));
    setData(offDays);
  }

  public int getDataListMax(List<FollowUpDataStatistic.DateCountsBean> list) {
    Ymax = 0;
    for (FollowUpDataStatistic.DateCountsBean bean : list) {
      if (Integer.valueOf(bean.count) > Ymax) Ymax = Integer.valueOf(bean.count);
    }
    return Ymax;
  }

  public void doData(List<FollowUpDataStatistic.DateCountsBean> datasFromServer) {
    for (FollowUpDataStatistic.DateCountsBean dateCountsBean : datas) {
      for (FollowUpDataStatistic.DateCountsBean dateFromServer : datasFromServer) {
        if (dateCountsBean.date.equals(dateFromServer.date)) {
          dateCountsBean.count = dateFromServer.count;
        }
      }
    }
  }

  public void getDefaultList(int offDays) {
    //datasExpand.clear();
    datas.clear();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(System.currentTimeMillis()));
    calendar.add(Calendar.DATE, -offDays);
    for (int i = 0; i < offDays; i++) {
      calendar.add(Calendar.DATE, 1);
      String date = DateUtils.Date2YYYYMMDD(calendar.getTime());
      FollowUpDataStatistic.DateCountsBean bean = new FollowUpDataStatistic.DateCountsBean();
      bean.date = date;
      bean.count = "0";
      datas.add(bean);
    }
  }

  @Override public void onChartGestureStart(MotionEvent me,
      ChartTouchListener.ChartGesture lastPerformedGesture) {
    Timber.e(
        "onChartGestureStart:->:MotionEvent:->" + me + ";;ChartGesture:->" + lastPerformedGesture);
  }

  @Override public void onChartGestureEnd(MotionEvent me,
      ChartTouchListener.ChartGesture lastPerformedGesture) {
    Timber.e(
        "onChartGestureEnd:->:MotionEvent:->" + me + ";;ChartGesture:->" + lastPerformedGesture);
  }

  @Override public void onChartLongPressed(MotionEvent me) {
    Timber.e("onChartLongPressed:->:MotionEvent:->" + me);
  }

  @Override public void onChartDoubleTapped(MotionEvent me) {
    Timber.e("onChartDoubleTapped:->:MotionEvent:->" + me);
  }

  @Override public void onChartSingleTapped(MotionEvent me) {
    Timber.e("onChartSingleTapped:->:MotionEvent:->" + me);
  }

  @Override
  public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    Timber.e("onChartFling:->:velocityX:->" + velocityX + ";;velocityY:->" + velocityY);
  }

  @Override public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

  }

  @Override public void onChartTranslate(MotionEvent me, float dX, float dY) {
    Timber.e("onChartTranslate:->:dX:->" + dX + ";;dY:->" + dY);
    float minX = lineChart.getLowestVisibleX();
    float maxX = lineChart.getHighestVisibleX();
    if (minX == 0) {
      lineChart.highlightValue(minX + 3, 0);
    } else {
      lineChart.highlightValue(maxX - 3, 0);
    }
  }

  @Override public void onValueSelected(Entry e, Highlight h) {
    //Timber.e("onValueSelected:->:Entry:->" + e + ";;Highlight:->" + h);
  }

  @Override public void onNothingSelected() {

  }
}
