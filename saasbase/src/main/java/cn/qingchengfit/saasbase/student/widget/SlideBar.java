package cn.qingchengfit.saasbase.student.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by huangbaole on 2017/10/30.
 */

public class SlideBar extends View {
    // 26个字母
    public static String[] DEFAULT = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint paint = new Paint();
    protected Context mContext;
    public SlideBar(Context context) {
        super(context);
        mContext=context;
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }
    // 触摸事件


    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / DEFAULT.length;// 获取每一个字母的高度

        for (int i = 0; i < DEFAULT.length; i++) {
            paint.setColor(Color.GRAY);
            paint.setColor(Color.BLACK);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            paint.setTextSize(dip2px(mContext, 12));
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(DEFAULT[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(DEFAULT[i], xPos, yPos, paint);
        }

    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
