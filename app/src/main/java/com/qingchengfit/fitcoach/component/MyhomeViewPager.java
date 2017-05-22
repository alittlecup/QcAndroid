package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

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
 * Created by Paper on 15/9/2 2015.
 */
public class MyhomeViewPager extends ViewPager {
    //boolean canSwipe = false;
    //private View scrollView;
    //private float touchY;
    //private float touchX;
    //private int senseY = 10;
    //
    //public MyhomeViewPager(Context context) {
    //    super(context);
    //    init();
    //}
    //
    //public MyhomeViewPager(Context context, AttributeSet attrs) {
    //    super(context, attrs);
    //    init();
    //}
    //
    //public void init() {
    //
    //}
    //
    //@Override
    //protected void onFinishInflate() {
    //    super.onFinishInflate();
    //
    //}
    //
    //@Override
    //public boolean onInterceptTouchEvent(MotionEvent ev) {
    //    View v = ((ViewGroup) getChildAt(getCurrentItem())).getChildAt(0);
    //    final int action = MotionEventCompat.getActionMasked(ev);
    //    switch (action) {
    //        case MotionEvent.ACTION_DOWN:
    //            touchY = ev.getY();
    //            touchX = ev.getX();
    //            canSwipe = true;
    //            super.onTouchEvent(ev);
    //            break;
    //        case MotionEvent.ACTION_MOVE:
    //            float y = touchY;
    //            float x = touchX;
    //            touchX = ev.getX();
    //            touchY = ev.getY();
    //            if (Math.abs(ev.getX() - x) > 20)
    //                return true;
    //            else return false;
    //        case MotionEvent.ACTION_UP:
    //
    //            break;
    //        default:
    //            break;
    //    }
    //    return super.onInterceptTouchEvent(ev);
    //}
    //
    //@Override
    //public boolean onTouchEvent(MotionEvent ev) {
    //    final int action = MotionEventCompat.getActionMasked(ev);
    //    View v = ((ViewGroup) getChildAt(getCurrentItem())).getChildAt(0);
    //    switch (action) {
    //        case MotionEvent.ACTION_DOWN:
    //            touchY = ev.getY();
    //            touchX = ev.getX();
    //            canSwipe = true;
    //            super.onTouchEvent(ev);
    //            break;
    //        case MotionEvent.ACTION_MOVE:
    //
    //
    //
    //            return super.onTouchEvent(ev);
    //        case MotionEvent.ACTION_UP:
    //
    //            break;
    //        default:
    //            break;
    //    }
    //    return super.onTouchEvent(ev);
    //}
    //
    //public boolean canScrollup() {
    //    FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) getAdapter();
    //    scrollView = ((ViewGroup) adapter.getItem(getCurrentItem()).getView()).getChildAt(0);
    //        return !ViewCompat.canScrollVertically(scrollView, -1);
    //}
    //
    //public boolean canScrollDown() {
    //    scrollView = ((ViewGroup) getChildAt(getCurrentItem())).getChildAt(0);
    //    return ViewCompat.canScrollVertically(scrollView, 1);
    //}

    public MyhomeViewPager(Context context) {
        super(context);
    }

    public MyhomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
