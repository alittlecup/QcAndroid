package cn.qingchengfit.saasbase.student.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;


/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/3 2015.
 */
public class CircleView extends Drawable {
    Paint mPaint;
    //    Paint mWhitePain;

    public CircleView(@ColorInt int color) {
        mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setStrokeWidth(4);
        int col;
        try {
            col = color;
        } catch (Exception e) {
            col = Color.rgb(3, 169, 245);
        }
        this.mPaint.setColor(col);
    }

    @Override public void draw(Canvas canvas) {
        Rect rect = getBounds();
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2 - 2, mPaint);
    }

    @Override public void setAlpha(int alpha) {

    }

    @Override public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
