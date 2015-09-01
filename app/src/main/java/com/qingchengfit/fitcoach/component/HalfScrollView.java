package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.qingchengfit.fitcoach.R;

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
    private RelativeLayout tab;
    private int firstheight;
    private boolean isSecondView;

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
        tab = (RelativeLayout) findViewById(R.id.myhome_tab_layout);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        firstheight = tab.getTop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (!ViewCompat.canScrollVertically(this, 1) && isSecondView) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float scrolly = getScrollY();

//        LogUtil.e("scroll:" + scrolly + "   " + ViewCompat.canScrollHorizontally(getChildAt(0), 1));
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (isSecondView && scrolly < firstheight - 200) {
                    smoothScrollTo(0, 0);
                    isSecondView = false;
                } else if (!isSecondView && scrolly > 200) {
                    smoothScrollTo(0, firstheight);
                    isSecondView = true;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (scrolly >= firstheight - 200)
                    return true;
                break;
        }
        return super.onTouchEvent(ev);
    }


//    private boolean canChildSrcollUp(){
//
//    }

}
