package cn.qingchengfit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/1/3.
 */

public class ConnerTag extends View {

    public float mRaduim = 50;
    Paint bgPaint ;
    Paint textPaint;
    int bgColor ;
    String mText ;

    public ConnerTag(Context context) {
        super(context);
    }

    public ConnerTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public ConnerTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) public ConnerTag(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs,defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyle) {
        isInEditMode();
        final TypedArray a = getContext().obtainStyledAttributes(
            attrs, R.styleable.ConnerTag, defStyle, 0);
        mRaduim = a.getDimension(R.styleable.ConnerTag_radius,8f);
        bgColor = a.getColor(R.styleable.ConnerTag_bg_color,Color.RED);
        mText = a.getString(R.styleable.ConnerTag_text);
        a.recycle();
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        bgPaint.setColor(bgColor);
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    //三角形加斜字
    private void drawBg(Canvas canvas) {
        int w = getWidth(), h = getHeight();
        canvas.drawArc(new RectF(w-mRaduim*2,0,w,mRaduim*2),0,-90,false,bgPaint);
        Path path = new Path();
        path.moveTo(2,0);
        path.lineTo(w-mRaduim,0);
        path.lineTo(w,mRaduim);
        path.lineTo(w,h-2);
        path.close();
        bgPaint.setColor(bgColor);
        canvas.drawPath(path,bgPaint);
        Path path1 = new Path();
        path1.moveTo(0,0);
        path1.lineTo(2,0);
        path1.lineTo(w,h-2);
        path1.lineTo(w,h);
        path1.close();
        bgPaint.setColor(Color.WHITE);
        canvas.drawPath(path1,bgPaint);
        canvas.save();
        textPaint.setTextSize(w/5);
        float textlenght = textPaint.measureText(mText);
        canvas.translate(w/2,0);
        canvas.rotate(45);
        canvas.drawText(mText, (int)(0.7*w/2-textlenght/2)  , h/5, textPaint);
        canvas.restore();
    }

}
