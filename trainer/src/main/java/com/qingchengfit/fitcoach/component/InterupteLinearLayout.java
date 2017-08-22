package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

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
 * Created by Paper on 16/1/15 2016.
 */
public class InterupteLinearLayout extends LinearLayout {

    private boolean canChildClick = false;

    public InterupteLinearLayout(Context context) {
        super(context);
    }

    public InterupteLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterupteLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCanChildClick(boolean canChildClick) {
        this.canChildClick = canChildClick;
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {

        return !canChildClick;
    }
}
