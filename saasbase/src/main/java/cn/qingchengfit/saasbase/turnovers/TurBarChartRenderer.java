package cn.qingchengfit.saasbase.turnovers;

import android.graphics.Canvas;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class TurBarChartRenderer extends BarChartRenderer {
  public TurBarChartRenderer(BarDataProvider chart, ChartAnimator animator,
      ViewPortHandler viewPortHandler) {
    super(chart, animator, viewPortHandler);
  }

  @Override protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
    super.drawDataSet(c, dataSet, index);


  }
}