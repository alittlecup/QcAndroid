package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.paper.paperbaselibrary.utils.LogUtil;

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
 * Created by Paper on 15/8/27 2015.
 */
public class HalfScrollView extends ScrollView {

    public HalfScrollView(Context context) {
        super(context);
    }

    public HalfScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (!ViewCompat.canScrollHorizontally(this, 1)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float scrolly = getScrollY();

        LogUtil.e("scroll:" + scrolly + "   " + ViewCompat.canScrollHorizontally(getChildAt(0), 1));
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (scrolly < 200) {
                    smoothScrollTo(0, 0);
                } else
                    smoothScrollTo(0, 400);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (scrolly >= 400)
                    return false;
                break;
        }
        return super.onTouchEvent(ev);
    }

//    private boolean canChildSrcollUp(){
//
//    }

}
