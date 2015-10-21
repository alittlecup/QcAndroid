package com.qingchengfit.fitcoach.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/10/3 2015.
 */
public class LoopView extends Drawable {
    Paint mPaint;
    Paint mWhitePain;

    public LoopView(String color) {
        mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(4);
        int col;
        try {
            col = Color.parseColor(color);
        } catch (Exception e) {
            col = Color.rgb(3, 169, 245);
        }
        this.mPaint.setColor(col);
        mWhitePain = new Paint();
        mWhitePain.setAntiAlias(true);
        mWhitePain.setStyle(Paint.Style.FILL);
        mWhitePain.setColor(Color.WHITE);

    }


    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2 - 2, mPaint);
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2 - 4, mWhitePain);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
