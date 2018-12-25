package cn.qingchengfit.saascommon.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.PieChart;

public class QcPieChart extends PieChart {
  public QcPieChart(Context context) {
    this(context, null);
  }

  public QcPieChart(Context context, AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public QcPieChart(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initStyle();
  }


  private void initStyle() {
    setUsePercentValues(true);
    getDescription().setEnabled(false);
    setDrawCenterText(true);
    setDragDecelerationEnabled(false);
    setDrawHoleEnabled(true);
    setHoleColor(Color.WHITE);
    setTransparentCircleAlpha(0);
    setRotationEnabled(false);
    getLegend().setEnabled(false);
    setNoDataText("");
  }

}
