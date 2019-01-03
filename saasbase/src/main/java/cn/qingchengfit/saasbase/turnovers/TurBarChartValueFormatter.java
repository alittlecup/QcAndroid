package cn.qingchengfit.saasbase.turnovers;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.util.List;

public class TurBarChartValueFormatter implements IAxisValueFormatter {
  private List<String> values;

  public TurBarChartValueFormatter(List<String> values) {
    this.values = values;
  }

  @Override public String getFormattedValue(float value, AxisBase axis) {
    if (value >= values.size()) return "";
    if (value < 0) return "";
    int position = (int) value;
    return values.get(position).substring(5);
  }
}
