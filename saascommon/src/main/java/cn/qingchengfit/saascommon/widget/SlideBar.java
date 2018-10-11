package cn.qingchengfit.saascommon.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SlideBar  extends View {

  // 触摸事件
  // 26个字母
  public String[] DEFAULT_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
      "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
      "W", "X", "Y", "Z", "#"};
  public Paint paint = new Paint();
  Context mContext;
  public String[] letters;
  private int singleHeight;

  public SlideBar(Context context) {
    this(context, null);
  }

  public SlideBar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    paint.setColor(Color.BLACK);
    paint.setTypeface(Typeface.DEFAULT);
    paint.setAntiAlias(true);
    paint.setTextSize(dip2px(mContext, 12));
    letters = DEFAULT_LETTERS;
    Rect rect = new Rect();
    paint.getTextBounds(letters[0], 0, 1, rect);
    letterHeight = rect.height();

  }

  private int letterHeight;

  public int getSingleHeight() {
    return singleHeight;
  }

  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    // 获取焦点改变背景颜色.
    int height = getHeight();// 获取对应高度
    int width = getWidth(); // 获取对应宽度
    singleHeight = Math.min(letterHeight + dip2px(mContext, 10), height / letters.length);
    for (int i = 0; i < letters.length; i++) {
      float xPos = width / 2 - paint.measureText(letters[i]) / 2;
      float yPos = singleHeight * i + singleHeight + (height - singleHeight * letters.length) / 2;
      canvas.drawText(letters[i], xPos, yPos, paint);
    }

  }

  private void setLetters(String[] letters) {
    this.letters = letters;
    invalidate();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthsize = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
    int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式

    int heightsize = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
    int heightmode = MeasureSpec.getMode(heightMeasureSpec);
    switch (widthmode) {
      case MeasureSpec.AT_MOST:
        widthsize = dip2px(mContext, 20) + getPaddingRight() + getPaddingLeft();
        break;
      case MeasureSpec.EXACTLY:
        widthsize = widthsize + getPaddingLeft() + getPaddingRight();
    }
    switch (heightmode) {
      case MeasureSpec.AT_MOST:
        Rect rect = new Rect();
        paint.getTextBounds(letters[0], 0, 1, rect);
        heightsize = (rect.height() + dip2px(mContext, 10)) * letters.length + getPaddingBottom() + getPaddingTop();
        break;
      case MeasureSpec.EXACTLY:
        heightsize = heightsize + getPaddingBottom() + getPaddingTop();
        break;
    }
    setMeasuredDimension(widthsize, heightsize);
  }
  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

}
