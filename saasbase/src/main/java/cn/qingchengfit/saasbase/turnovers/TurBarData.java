package cn.qingchengfit.saasbase.turnovers;

public class TurBarData implements ITurnoverBarChartData {
  private float[] floatYs;
  private float x;
  private Object data;

  public TurBarData(float x, float[] floatYs,Object data) {
    this.x = x;
    this.floatYs = floatYs;
    this.data=data;
  }

  @Override public float getX() {
    return x;
  }

  @Override public float[] getY() {
    return floatYs;
  }

  @Override public Object getData() {
    return data;
  }
}
