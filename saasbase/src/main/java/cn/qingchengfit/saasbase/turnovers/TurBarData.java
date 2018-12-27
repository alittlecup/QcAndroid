package cn.qingchengfit.saasbase.turnovers;

public class TurBarData implements ITurnoverBarChartData {
  private float[] floatYs;
  private float x;

  public TurBarData(float x, float[] floatYs) {
    this.x = x;
    this.floatYs = floatYs;
  }

  @Override public float getX() {
    return x;
  }

  @Override public float[] getY() {
    return floatYs;
  }
}
