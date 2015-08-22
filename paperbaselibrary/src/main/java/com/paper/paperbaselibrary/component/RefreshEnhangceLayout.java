package com.paper.paperbaselibrary.component;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;

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
 * Created by Paper on 15/8/20 2015.
 */

public class RefreshEnhangceLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private View mChildrenView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    private ViewDragHelper mDragHelper;

    /**
     * @param context
     */
    public RefreshEnhangceLayout(Context context) {
        this(context, null);
    }

    public RefreshEnhangceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

//        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.listview_footer, null,
//                false);
        mListViewFooter = new View(context);
        mDragHelper = ViewDragHelper.create(this, 1, new DragCallBack());


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 初始化ListView对象
//        if (mListView == null) {
//            getListView();
//        }
        if (getChildCount() > 0) {
            mChildrenView = getChildAt(0);
        }
    }

    /**
     * 获取ListView对象
     */
//    private void getListView() {
//        int childs = getChildCount();
//        if (childs > 0) {
//            View childView = getChildAt(0);
//            if (childView instanceof ListView) {
//                mListView = (ListView) childView;
//                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
//                mListView.setOnScrollListener(this);
//                Log.d(VIEW_LOG_TAG, "### 找到listview");
//            }
//        }
//    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if ((action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        if (mDragHelper.shouldInterceptTouchEvent(ev)) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        final int action = event.getAction();
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                 按下
//                mYDown = (int) event.getRawY();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                 移动
//                mLastY = (int) event.getRawY();
//                break;
//
//            case MotionEvent.ACTION_UP:
//                 抬起
//                    loadData();
//                break;
//            default:
//                break;
//        }
        if (canLoad()) {
            mDragHelper.processTouchEvent(event);
            return true;
        } else
            return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading;
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

//        if (mListView != null && mListView.getAdapter() != null) {
//            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
//        }
        return !ViewCompat.canScrollVertically(mChildrenView, 1);
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            //
            mOnLoadListener.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            this.addView(mListViewFooter, new LayoutParams(LayoutParams.MATCH_PARENT, 100));
        } else {
            this.removeView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            loadData();
        }
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnLoadListener {
        public void onLoad();
    }

    private class DragCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return -500;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
//            final int topBound = getPaddingTop();
//            final int bottomBound = getHeight() - RefreshEnhangceLayout.this.getHeight();
//
//            final int newTop = Math.min(Math.max(top, topBound), bottomBound);

            return top;
        }
    }


}
