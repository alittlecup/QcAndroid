//package com.qingchengfit.fitcoach.component;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.os.Build;
//import android.support.v4.view.ViewCompat;
//import android.util.AttributeSet;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.animation.Interpolator;
//import android.widget.FrameLayout;
//import android.widget.Scroller;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2016/12/12.
// */
//
//public class CustomVpScrollView extends FrameLayout implements GestureDetector.OnGestureListener{
//
//    private static final boolean USE_CACHE = false;
//    private GestureDetector mGestureDetector;
//    private Scroller mScroller;
//    private static final Interpolator sInterpolator = new Interpolator() {
//        @Override
//        public float getInterpolation(float t) {
//            t -= 1.0f;
//            return t * t * t * t * t + 1.0f;
//        }
//    };
//    private boolean mIsScrollStarted;
//    private boolean mScrollingCacheEnabled;
//
//    public CustomVpScrollView(Context context) {
//        super(context);
//        initView();
//    }
//
//
//
//    public CustomVpScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initView();
//    }
//
//    public CustomVpScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView();
//    }
//
//    @TargetApi(21)
//    public CustomVpScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView();
//    }
//    private void initView() {
//        mGestureDetector = new GestureDetector(getContext(),this);
//        mScroller = new Scroller(getContext(),sInterpolator);
//
//    }
//
//
//
//    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        if (widthMode == MeasureSpec.UNSPECIFIED) {
//            return;
//        }
//
//        if (getChildCount() > 0) {
//            final View child = getChildAt(0);
//            final int widthPadding;
//            final int heightPadding;
//            final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
//            final int targetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
//            if (targetSdkVersion >= Build.VERSION_CODES.M) {
//                widthPadding = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin;
//                heightPadding = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin;
//            } else {
//                widthPadding = getPaddingLeft() + getPaddingRight();
//                heightPadding = getPaddingTop() + getPaddingBottom();
//            }
//
//            int desiredWidth = getMeasuredWidth() - widthPadding;
//            if (child.getMeasuredWidth() < desiredWidth) {
//                final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
//                    desiredWidth, MeasureSpec.EXACTLY);
//                final int childHeightMeasureSpec = getChildMeasureSpec(
//                    heightMeasureSpec, heightPadding, lp.height);
//                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//            }
//        }
//    }
//
//    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return mGestureDetector.onTouchEvent(ev);
//
//    }
//
//    @Override public boolean onDown(MotionEvent e) {
//        return false;
//    }
//
//    @Override public void onShowPress(MotionEvent e) {
//
//    }
//
//    @Override public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }
//
//    @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        return false;
//    }
//
//    @Override public void onLongPress(MotionEvent e) {
//
//    }
//
//    @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        return false;
//    }
//
//
//    /**
//     * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
//     *
//     * @param x the number of pixels to scroll by on the X axis
//     * @param y the number of pixels to scroll by on the Y axis
//     */
//    void smoothScrollTo(int x, int y) {
//        smoothScrollTo(x, y, 0);
//    }
//
//    /**
//     * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
//     *
//     * @param x the number of pixels to scroll by on the X axis
//     * @param y the number of pixels to scroll by on the Y axis
//     * @param velocity the velocity associated with a fling, if applicable. (0 otherwise)
//     */
//    void smoothScrollTo(int x, int y, int velocity) {
//        if (getChildCount() == 0) {
//            // Nothing to do.
//            setScrollingCacheEnabled(false);
//            return;
//        }
//
//        int sx;
//        boolean wasScrolling = (mScroller != null) && !mScroller.isFinished();
//        if (wasScrolling) {
//            // We're in the middle of a previously initiated scrolling. Check to see
//            // whether that scrolling has actually started (if we always call getStartX
//            // we can get a stale value from the scroller if it hadn't yet had its first
//            // computeScrollOffset call) to decide what is the current scrolling position.
//            sx = mIsScrollStarted ? mScroller.getCurrX() : mScroller.getStartX();
//            // And abort the current scrolling.
//            mScroller.abortAnimation();
//            setScrollingCacheEnabled(false);
//        } else {
//            sx = getScrollX();
//        }
//        int sy = getScrollY();
//        int dx = x - sx;
//        int dy = y - sy;
//        if (dx == 0 && dy == 0) {
//            completeScroll(false);
//            populate();
//            setScrollState(SCROLL_STATE_IDLE);
//            return;
//        }
//
//        setScrollingCacheEnabled(true);
//        setScrollState(SCROLL_STATE_SETTLING);
//
//        final int width = getClientWidth();
//        final int halfWidth = width / 2;
//        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
//        final float distance = halfWidth + halfWidth
//            * distanceInfluenceForSnapDuration(distanceRatio);
//
//        int duration;
//        velocity = Math.abs(velocity);
//        if (velocity > 0) {
//            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
//        } else {
//            final float pageWidth = width * mAdapter.getPageWidth(mCurItem);
//            final float pageDelta = (float) Math.abs(dx) / (pageWidth + mPageMargin);
//            duration = (int) ((pageDelta + 1) * 100);
//        }
//        duration = Math.min(duration, MAX_SETTLE_DURATION);
//
//        // Reset the "scroll started" flag. It will be flipped to true in all places
//        // where we call computeScrollOffset().
//        mIsScrollStarted = false;
//        mScroller.startScroll(sx, sy, dx, dy, duration);
//        ViewCompat.postInvalidateOnAnimation(this);
//    }
//
//    @Override
//    public void computeScroll() {
//        mIsScrollStarted = true;
//        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
//            int oldX = getScrollX();
//            int oldY = getScrollY();
//            int x = mScroller.getCurrX();
//            int y = mScroller.getCurrY();
//
//            if (oldX != x || oldY != y) {
//                scrollTo(x, y);
//                if (!pageScrolled(x)) {
//                    mScroller.abortAnimation();
//                    scrollTo(0, y);
//                }
//            }
//
//            // Keep on drawing until the animation has finished.
//            ViewCompat.postInvalidateOnAnimation(this);
//            return;
//        }
//
//        // Done with scroll, clean up state.
//        completeScroll(true);
//    }
//
//    private void setScrollingCacheEnabled(boolean enabled) {
//        if (mScrollingCacheEnabled != enabled) {
//            mScrollingCacheEnabled = enabled;
//            if (USE_CACHE) {
//                final int size = getChildCount();
//                for (int i = 0; i < size; ++i) {
//                    final View child = getChildAt(i);
//                    if (child.getVisibility() != GONE) {
//                        child.setDrawingCacheEnabled(enabled);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override protected void onDetachedFromWindow() {
//        if ((mScroller != null) && !mScroller.isFinished()) {
//            mScroller.abortAnimation();
//        }
//        super.onDetachedFromWindow();
//    }
//}
