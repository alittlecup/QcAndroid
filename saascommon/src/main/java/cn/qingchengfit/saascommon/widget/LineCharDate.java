package cn.qingchengfit.saascommon.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.utils.DateUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.Date;

/**
 * Created by huangbaole on 2017/11/6.
 */

public class LineCharDate extends LinearLayout {
  Context mContext;
  private LineChart lineChart;
  private TextView leftTextView;
  private TextView rightTextView;

  private String markViewUnit;

  public LineCharDate(Context context) {
    super(context);
    initView(context);
  }

  public LineCharDate(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initView(context);
  }

  public LineCharDate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context);
  }

  private void initView(Context context) {
    mContext = context;
    inflate(mContext, R.layout.layout_linechar_date, this);
    lineChart = findViewById(R.id.lineChart);
    leftTextView = findViewById(R.id.start_day);
    rightTextView = findViewById(R.id.end_day);
    initLineChar(lineChart);
  }

  private void initLineChar(LineChart chart) {
    chart.setOnChartGestureListener(new LineCharDate.SimpleOnChartGestureListener() {
      @Override public void onChartTranslate(MotionEvent me, float dX, float dY) {
        float minX = chart.getLowestVisibleX();
        float maxX = chart.getHighestVisibleX();
        if (minX == 0) {
          chart.highlightValue(minX + 3, 0);
        } else {
          chart.highlightValue(maxX - 3, 0);
        }
      }

      @Override public void onChartSingleTapped(MotionEvent me) {
        super.onChartSingleTapped(me);
        if (lineCharListener != null) {
          Entry entry = chart.getEntryByTouchPoint(me.getX(), me.getY());
          lineCharListener.onChartClick((int) entry.getX(), (int) entry.getY());
        }
      }
    });
    chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
      @Override public void onValueSelected(Entry entry, Highlight h) {
        if (lineCharListener != null) {
          lineCharListener.onChartTranslate((int) entry.getX(), (int) entry.getY());
        }
      }

      @Override public void onNothingSelected() {

      }
    });
    chart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
    chart.setDrawGridBackground(false);
    chart.setDrawBorders(false);  //是否在折线图上添加边框

    // no description text
    chart.getDescription().setEnabled(false);
    // 如果没有数据的时候，会显示这个，类似listview的emtpyview
    chart.setNoDataText("暂无数据");
    chart.setNoDataTextColor(Color.parseColor("#6eb8f1"));

    // enable touch gestures
    chart.setTouchEnabled(true);
    // enable scaling and dragging
    chart.setDragEnabled(true);
    chart.setScaleEnabled(false);
    // if disabled, scaling can be done on x- and y-axis separately
    chart.setPinchZoom(false);
    chart.setDragDecelerationEnabled(false);
    chart.setRenderer(
        new LinerChartHightRender(chart, chart.getAnimator(), chart.getViewPortHandler()));
    initMarkerView();

    initXAxis(chart.getXAxis());

    initYAxis(chart.getAxisLeft());

    chart.getAxisRight().setEnabled(false);
    chart.animateX(500);

    Legend l = chart.getLegend();

    l.setForm(Legend.LegendForm.NONE);
  }

  private void initMarkerView() {
    MyMarkerView markerView = new MyMarkerView(getContext(), R.layout.custom_marker_view);
    markerView.setChartView(lineChart);
    lineChart.setMarker(markerView);
  }

  public String getMarkViewUnit() {
    return markViewUnit;
  }

  public void setMarkViewUnit(String markViewUnit) {
    this.markViewUnit = markViewUnit;
    ((MyMarkerView) lineChart.getMarker()).setStringUnit(markViewUnit);
  }

  public void hideMarkDate() {
    ((MyMarkerView) lineChart.getMarker()).hideDate();
  }

  public void setMarkViewTintColor(@ColorInt int color) {
    ((MyMarkerView) lineChart.getMarker()).setDrawableTintColor(color);
  }

  public void setVisibleXRange(int xRange) {
    // 设置X轴最大的显示范围
    lineChart.setVisibleXRangeMaximum(xRange);
    // 设置X轴最小的显示范围
    lineChart.setVisibleXRangeMinimum(xRange);
  }

  private void initYAxis(YAxis mAxisLeft) {
    mAxisLeft.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
    mAxisLeft.setAxisMaximum(5);//Y轴最大高度
    mAxisLeft.setSpaceTop(10);
    mAxisLeft.setStartAtZero(true);//设置Y轴坐标是否从0开始

    mAxisLeft.setDrawLimitLinesBehindData(false);
    mAxisLeft.setLabelCount(4, true);
    mAxisLeft.setTextColor(Color.parseColor("#999999"));
    mAxisLeft.setAxisLineColor(Color.TRANSPARENT);
    mAxisLeft.setGridColor(Color.parseColor("#dddddd"));
  }

  public void setYAxisMaximum(float yAxisMaximum) {
    lineChart.getAxisLeft().setAxisMaximum(yAxisMaximum);
  }

  private void initXAxis(XAxis mXAxis) {
    mXAxis.removeAllLimitLines();
    mXAxis.enableGridDashedLine(10f, 10f, 0f);
    mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    mXAxis.setGridColor(Color.TRANSPARENT);// 去掉网格中竖线的显示
    mXAxis.setLabelCount(0, true);
    mXAxis.setDrawLabels(false);
    mXAxis.setTextColor(Color.parseColor("#999999"));
    mXAxis.setXOffset(20);
    mXAxis.setAxisLineColor(Color.parseColor("#dddddd"));
    mXAxis.setAvoidFirstLastClipping(true);
  }

  public void hideBottomDate() {
    leftTextView.setVisibility(GONE);
    rightTextView.setVisibility(GONE);
  }

  public void setData(LineData data) {
    if (data == null) return;
    LineDataSet set1 = (LineDataSet) (data.getDataSetByIndex(0));
    int size = set1.getValues().size();
    setLeftBottomText(DateUtils.Date2MMDD(DateUtils.addDay(new Date(), -size)));
    setRightBottomText(DateUtils.Date2MMDD(new Date()));
    lineChart.setData(data);
    lineChart.invalidate();
    setYAxisMaximum(getAxisLeftLefeMax(data));

    moveViewToX(size - 4);
    highlightValue(size + 2, 0);

    setVisibleXRange(6);
    setMarkViewTintColor(set1.getColor());
  }

  public void addData(LineData data) {
    if (data == null) return;
    if (lineChart.isEmpty()) {
      setData(data);
    } else {

      lineChart.setData(data);
      lineChart.invalidate();

      setYAxisMaximum(getAxisLeftLefeMax(data));

      moveViewToX(19);
      highlightValue(23, 0);

      setVisibleXRange(6);
    }
  }

  public void addData(LineData data, Integer pos) {
    if (data != null) {
      addData(data);
    }
    if (pos > 3) {
      moveViewToX(pos - 3);
      highlightValue(pos, 0);
    }
  }

  public void setLeftBottomText(String msg) {
    leftTextView.setText(msg);
  }

  public void setRightBottomText(String msg) {
    rightTextView.setText(msg);
  }

  public void moveViewToX(float xValue) {
    lineChart.moveViewToX(xValue);
  }

  private float highlightValuePos = -1;

  public void highlightValue(float x, int dataSetIndex) {
    highlightValuePos = x;
    lineChart.highlightValue(x, 0);
  }

  private float getAxisLeftLefeMax(LineData data) {
    float Ymax = 0;
    LineDataSet dataSet = (LineDataSet) data.getDataSetByIndex(0);
    for (Entry item : dataSet.getValues()) {
      if (item.getY() > Ymax) Ymax = item.getY();
    }
    return Ymax == 0 ? 5 : (Ymax / 2f) * 3f;
  }

  public void setLineCharListener(onLineCharListener lineCharListener) {
    this.lineCharListener = lineCharListener;
  }

  private onLineCharListener lineCharListener;

  public interface onLineCharListener {
    void onChartTranslate(int valuePos, int count);

    void onChartClick(int clickPos, int count);
  }

  public class SimpleOnChartGestureListener implements OnChartGestureListener {

    @Override public void onChartGestureStart(MotionEvent me,
        ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override public void onChartGestureEnd(MotionEvent me,
        ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override public void onChartLongPressed(MotionEvent me) {

    }

    @Override public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
  }
}
