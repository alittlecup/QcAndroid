package cn.qingchengfit.saasbase.turnovers;

public class TurnoverChartStat implements ITurnoverChartData {
  private float present;
  private String color;
  private String label;

  public TurnoverChartStat(float present, String color, String label) {
    this.present = present;
    this.color = color;
    this.label = label;
  }

  @Override public String getColor() {
    return "#" + color;
  }

  @Override public float getPresent() {
    return present;
  }

  @Override public String getLabel() {
    return label;
  }
}
