package cn.qingchengfit.saasbase.cards.views.spendrecord;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/4/28 2016.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = false; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;
    private boolean isScrollDown = false;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int last = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            int total = mLinearLayoutManager.getItemCount();
            if (last == (total - 1) && isScrollDown) {
                onLoadMore();
            }
        }
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dx > 0 || dy > 0) {
            isScrollDown = true;
        } else {
            isScrollDown = false;
        }
    }

    //    @Override
    //    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    //        super.onScrolled(recyclerView, dx, dy);
    //
    //        visibleItemCount = recyclerView.getChildCount();
    //        totalItemCount = mLinearLayoutManager.getItemCount();
    //        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
    //
    //        if (loading) {
    //        } else {
    //            if (firstVisibleItem + visibleItemCount >= totalItemCount) {
    //                loading = true;
    //                onLoadMore();
    //                loading = false;
    //            }
    //        }
    //    }

    public abstract void onLoadMore();
}
