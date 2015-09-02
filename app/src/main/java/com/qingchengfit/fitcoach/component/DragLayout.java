package com.qingchengfit.fitcoach.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

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
 * Created by Paper on 15/9/1 2015.
 */
public class DragLayout extends LinearLayout {
    private ViewDragHelper dragHelper;
    private View mDragView, contentView;
    private int dragRange;
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            contentView.layout(0, top + mDragView.getHeight(), getWidth(), top + mDragView.getHeight() + dragRange);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int topBound = getHeight() - dragRange - mDragView.getHeight();
            int bottomBound = getHeight() - mDragView.getHeight();
            final int newHeight = Math.min(Math.max(topBound, top), bottomBound);
            return newHeight;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 500;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
//            if (yvel > 0) {
//                smoothToBottom();
//            }else if (yvel < 0) {
//                smoothToTop();
//            }
        }
    };

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        mDragView = findViewById(R.id.halfscroll_first);
        contentView = findViewById(R.id.myhome_viewpager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        dragRange = contentView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mDragView.layout(0, getHeight() - mDragView.getHeight(), getWidth(), getHeight());
        contentView.layout(0, getHeight(), getWidth(), getHeight() + dragRange);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            dragHelper.cancel();
            return false;
        }
        return dragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    private void smoothToTop() {
        if (dragHelper.smoothSlideViewTo(mDragView, getPaddingLeft(), getHeight() - dragRange - mDragView.getHeight())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void smoothToBottom() {
        if (dragHelper.smoothSlideViewTo(mDragView, getPaddingLeft(), getHeight() - mDragView.getHeight())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}
