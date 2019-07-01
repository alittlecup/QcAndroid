package cn.qingchengfit.saasbase.course.detail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import cn.qingchengfit.saasbase.R;
import com.bigkoo.pickerview.lib.DensityUtil;

public class CountImageView extends AppCompatImageView {
  public CountImageView(Context context) {
    super(context);
    init();
  }

  public CountImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CountImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private Paint textPaint;
  private Paint bottomArea;
  private int bottomMargin = 0;

  private void init() {
    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setTextSize(DensityUtil.dip2px(getContext(), 8));
    textPaint.setColor(Color.WHITE);
    bottomArea = new Paint(Paint.ANTI_ALIAS_FLAG);
    bottomArea.setColor(getResources().getColor(R.color.colorPrimary));
    bottomMargin = DensityUtil.dip2px(getContext(), 3);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (!TextUtils.isEmpty(text)) {
      int width = getWidth();
      int height = getHeight();
      canvas.drawArc(0, 0, width, height, 30, 120, false, bottomArea);
      int textWidth = textBounds.width();
      canvas.drawText(text, (width - textWidth) / 2, height -bottomMargin, textPaint);
    }
  }

  private String text;
  private Rect textBounds = new Rect();

  public void setText(String text) {
    if (text != null && text.length() > 0) {
      this.text = text;
      textPaint.getTextBounds(text, 0, text.length(), textBounds);
    }
  }
}
