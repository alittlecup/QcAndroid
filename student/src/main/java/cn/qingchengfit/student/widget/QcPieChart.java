package cn.qingchengfit.student.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import java.util.ArrayList;

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
