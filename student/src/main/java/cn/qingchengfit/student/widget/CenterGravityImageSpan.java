package cn.qingchengfit.student.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

public class CenterGravityImageSpan extends ImageSpan {

  public CenterGravityImageSpan(Drawable drawable) {
    super(drawable);
  }
  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top,
      int y, int bottom, @NonNull Paint paint) {

    Drawable b = getDrawable();
    Paint.FontMetricsInt fm = paint.getFontMetricsInt();
    int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;
    canvas.save();
    canvas.translate(x, transY);
    b.draw(canvas);
    canvas.restore();
  }
}
