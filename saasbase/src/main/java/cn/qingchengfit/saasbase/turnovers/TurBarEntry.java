package cn.qingchengfit.saasbase.turnovers;

import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.data.BarEntry;

public class TurBarEntry extends BarEntry {
  public TurBarEntry(float x, float y) {
    super(x, y);
  }

  public TurBarEntry(float x, float y, Object data) {
    super(x, y, data);
  }

  public TurBarEntry(float x, float y, Drawable icon) {
    super(x, y, icon);
  }

  public TurBarEntry(float x, float y, Drawable icon, Object data) {
    super(x, y, icon, data);
  }

  public TurBarEntry(float x, float[] vals) {
    super(x, vals);
  }

  public TurBarEntry(float x, float[] vals, Object data) {
    super(x, vals, data);
  }

  public TurBarEntry(float x, float[] vals, int[] colors, Object data) {
    super(x, vals, data);
    this.colors = colors;
  }

  public TurBarEntry(float x, float[] vals, Drawable icon) {
    super(x, vals, icon);
  }

  public TurBarEntry(float x, float[] vals, Drawable icon, Object data) {
    super(x, vals, icon, data);
  }

  private int[] colors;

  public int getYvalsColor(int index) {
    return colors[index];
  }
}
