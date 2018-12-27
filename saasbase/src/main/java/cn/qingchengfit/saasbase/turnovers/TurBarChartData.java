package cn.qingchengfit.saasbase.turnovers;

import java.util.List;

public class TurBarChartData {
  private String date;
  private List<TurnoversChartStatData> stat;
  private TurnoversChartStatData total;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public List<TurnoversChartStatData> getStat() {
    return stat;
  }

  public void setStat(List<TurnoversChartStatData> stat) {
    this.stat = stat;
  }

  public TurnoversChartStatData getTotal() {
    return total;
  }

  public void setTotal(TurnoversChartStatData total) {
    this.total = total;
  }
}
