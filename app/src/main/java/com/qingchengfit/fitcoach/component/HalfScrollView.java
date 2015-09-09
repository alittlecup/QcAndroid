package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Scroller;

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
    private TabLayout tab;
    private int firstheight;
    private boolean isSecondView;
    private View mChinlrenView;
    private ViewDragHelper mDragHelper;
    private ViewGroup firstView;
    private float lastX, lastY;
    private HalfViewListener listener;
    private Scroller mScroller;
    private int scrollpos;
    public HalfScrollView(Context context) {
        super(context);
        init();
    }

    public HalfScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public HalfScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
        mScroller = new Scroller(getContext());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tab = (TabLayout) findViewById(R.id.myhome_tab);
        firstView = (ViewGroup) findViewById(R.id.halfscroll_first);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        scrollpos = getScrollY();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final int action = MotionEventCompat.getActionMasked(ev);
//        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
//            mDragHelper.cancel();
//            return false;
//        }
//        return mDragHelper.shouldInterceptTouchEvent(ev);
//    return true;
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        mDragHelper.processTouchEvent(ev);
//        return true;
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        firstheight = tab.getTop();
        scrollTo(0, scrollpos);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
//        this.onInterceptTouchEvent(ev);
//        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (!isSecondView)
            return true;
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                lastX = ev.getX();
                super.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float y = lastY;
                lastY = ev.getY();
                if (ev.getY() - y > 5 && canChildUp())
                    return true;
                else return false;
//                if (isSecondView && Math.abs(ev.getX() - lastX) > 10) {
//                    lastX = ev.getX();
//                    return false;
//                }
//
//                if (ev.getY() - lastY > 20 && isSecondView && !canChildVScroll(-1)) {
//                    lastY = ev.getY();
//                    return true;
//                } else if (ev.getY() - lastY > 20 && isSecondView && canChildVScroll(-1)) {
//                    lastY = ev.getY();
//                    return false;
//                } else if (ev.getY() - lastY < 20 && isSecondView && (canChildVScroll(1) || (canChildVScroll(-1)))) {
//                    lastY = ev.getY();
//                    return false;
//                }
//                lastY = ev.getY();
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean canChildUp() {
        ViewGroup vg = (ViewGroup) getChildAt(0);
        ViewGroup v = (ViewGroup) vg.getChildAt(vg.getChildCount() - 1);
        MyhomeViewPager viewPager = (MyhomeViewPager) v.getChildAt(v.getChildCount() - 1);
        ViewGroup vg2 = (ViewGroup) viewPager.getChildAt(0);
        return viewPager.canScrollup();
//        if (direct > 0)
//            return ViewCompat.canScrollVertically(vg2.getChildAt(0), direct);
//        else
//            return ViewCompat.canScrollVertically(vg2.getChildAt(0), direct) && viewPager.getCurrentItem() == 0;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float scrolly = getScrollY();
        float y = ev.getY();
//        LogUtil.e("scroll:" + scrolly + "   " + ViewCompat.canScrollHorizontally(getChildAt(0), 1));
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
//                if (!isSecondView)
//                    return super.onTouchEvent(ev);
//
//                if (canChildUp() && isSecondView)
//                    return true;
//                else return false;
                break;
            case MotionEvent.ACTION_UP:

//                if (isSecondView && scrolly < firstheight - 200) {
//                    smoothScrollTo(0, 0);
//                    ViewCompat.animate(this).rotationYBy(-firstheight).start();
//                    isSecondView = false;
//                } else if (!isSecondView && scrolly > 200) {
//                    smoothScrollTo(0, firstheight);
//                    ViewCompat.animate(this).rotationYBy(firstheight).start();
//                    isSecondView = true;
//                }
                return super.onTouchEvent(ev);

            case MotionEvent.ACTION_MOVE:
//                if (y - lastY > 20 && !canChildVScroll(-1) && isSecondView) {
//                    lastY = y;
//                    return super.onTouchEvent(ev);
//                }else if (!isSecondView){
//                    return super.onTouchEvent(ev);
//                }
//                else {
//                    lastY = y;
//                    return false;
//                }

                return super.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mScroller.isFinished()) {
            if (ViewCompat.canScrollVertically(this, 1)) {
                isSecondView = false;
            } else isSecondView = true;
        }
//        if (t < firstheight) {
//            isSecondView = false;
//        } else isSecondView = true;
        if (listener != null)
            listener.onScroll(t);
    }

    public HalfViewListener getListener() {
        return listener;
    }

    public void setListener(HalfViewListener listener) {
        this.listener = listener;
    }

    public interface HalfViewListener {
        public void onScroll(int i);

    }

    class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == firstView;
        }

//        @Override
//        public int getViewVerticalDragRange(View child) {
//            return 200;
//        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }
    }

//

//    private boolean canChildSrcollUp(){
//
//    }

}
