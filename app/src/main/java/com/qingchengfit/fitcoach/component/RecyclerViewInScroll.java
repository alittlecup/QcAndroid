package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
 * Created by Paper on 15/9/1 2015.
 */
public class RecyclerViewInScroll extends RecyclerView {
    public RecyclerViewInScroll(Context context) {
        super(context);
    }

    public RecyclerViewInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewInScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent e) {
        //        if (!ViewCompat.canScrollVertically(this, -1)) {
        //            return false;
        //        } else return true;
        return super.onInterceptTouchEvent(e);
    }

    @Override public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
