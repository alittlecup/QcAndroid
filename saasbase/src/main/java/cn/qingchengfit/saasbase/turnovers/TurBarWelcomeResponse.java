package cn.qingchengfit.saasbase.turnovers;

import cn.qingchengfit.saascommon.model.IChartData;
import java.util.List;

public class TurBarWelcomeResponse implements IChartData<List<TurBarChartData>> {
  public float count;
  public List<TurBarChartData> date_counts;

  @Override public List<TurBarChartData> getData() {
    return date_counts;
  }
}
