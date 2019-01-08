package cn.qingchengfit.saasbase.turnovers;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.BarChart;

public class TurBarChart extends BarChart {

  public TurBarChart(Context context) {
    super(context);
  }

  public TurBarChart(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TurBarChart(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override protected void init() {
    super.init();
    //mRenderer = new TurBarChartRenderer(this, mAnimator, mViewPortHandler);
  }
}
