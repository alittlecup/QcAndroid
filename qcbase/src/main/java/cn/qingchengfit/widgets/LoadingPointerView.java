package cn.qingchengfit.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import cn.qingchengfit.utils.MeasureUtils;


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
 * Created by Paper on 16/2/15 2016.
 */
public class LoadingPointerView extends View {
    private Paint mainPaint;
    private Path path;

    public LoadingPointerView(Context context) {
        super(context);
        init();
    }

    public LoadingPointerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingPointerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        mainPaint = new Paint();
        mainPaint.setAlpha(0);
        mainPaint.setStyle(Paint.Style.FILL);
//        mainPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(Color.rgb(204, 204, 204));
        path = new Path();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.restore();
        int heigt = getHeight();
        int width = getWidth();

        int bigR =  MeasureUtils.dpToPx(5f,getResources());
        int minR =  MeasureUtils.dpToPx(1.5f,getResources());
        int pointLen = width/2- MeasureUtils.dpToPx(15f,getResources());
        path.moveTo(width / 2 - bigR, heigt / 2);
        path.lineTo(width / 2 + bigR, heigt / 2);
        path.lineTo(width / 2 + minR, heigt / 2 - pointLen);
        path.lineTo(width / 2 - minR, heigt / 2 - pointLen);
        path.close();
        canvas.drawCircle(width / 2, heigt / 2, bigR, mainPaint);
        canvas.drawCircle(width / 2, heigt / 2 - pointLen, minR, mainPaint);
        canvas.drawPath(path, mainPaint);
        canvas.translate(width / 2, heigt / 2);
    }


}
