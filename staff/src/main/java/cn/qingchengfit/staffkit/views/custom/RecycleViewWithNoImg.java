package cn.qingchengfit.staffkit.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.MeasureUtils;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YDRecycleViewWithNoImg
 * <p/>
 * <p/>
 * Created by Paper on 16/3/2 2016.
 */
public class RecycleViewWithNoImg extends RelativeLayout implements CustomSwipeRefreshLayout.CanChildScrollUpCallback {

    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView textView;
    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private String hint;
    private LinearLayout nodata;
    private int noDataImgRes;
    private int colorRes;
    private float padbottom;
    private Animation rotate;
    private ImageView imgLoading;
    private LinearLayout loadingLayout;
    private float mNodataTop;
    private float padTop;

    public RecycleViewWithNoImg(Context context) {
        super(context);
        inflate(context, R.layout.layout_srf_no_img, this);
        onFinishInflate();
    }

    public RecycleViewWithNoImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecycleViewWithNoImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_srf_no_img, this, true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RecycleNoImg);
        hint = ta.getString(R.styleable.RecycleNoImg_rn_hint);
        noDataImgRes = ta.getResourceId(R.styleable.RecycleNoImg_rn_drawable, R.drawable.schedules_no_data);
        colorRes = ta.getResourceId(R.styleable.RecycleNoImg_rn_color, R.color.colorPrimary);
        padbottom = ta.getDimension(R.styleable.RecycleNoImg_rn_bottom_padding, MeasureUtils.dpToPx(0f, getResources()));
        padTop = ta.getDimension(R.styleable.RecycleNoImg_rn_top_padding, MeasureUtils.dpToPx(0f, getResources()));
        mNodataTop = ta.getDimension(R.styleable.RecycleNoImg_rn_no_data_top, MeasureUtils.dpToPx(100f, getResources()));
        ta.recycle();
    }

    public void setNoData(boolean no) {
        setFresh(false);
        rotate.cancel();
        loadingLayout.setVisibility(GONE);
        ViewCompat.setTranslationY(nodata, mNodataTop);
        if (no) {
            imageView.setImageResource(noDataImgRes);
            nodata.setVisibility(VISIBLE);
        } else {
            nodata.setVisibility(GONE);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration de) {
        recyclerView.addItemDecoration(de);
    }

    public void addScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.addOnScrollListener(listener);
    }

    public void scrollToPosition(int pos) {
        recyclerView.scrollToPosition(pos);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        recyclerView = findViewById(R.id.no_recycleview);
        imageView = findViewById(R.id.img);
        textView = findViewById(R.id.hint);
        nodata = findViewById(R.id.nodata);
        loadingLayout = findViewById(R.id.loading_layout);
        recyclerView.setPadding(recyclerView.getPaddingLeft(), (int) padTop, recyclerView.getPaddingRight(), (int) padbottom);
        swipeRefreshLayout = findViewById(R.id.swipe);
        textView.setText(hint);
        swipeRefreshLayout.setColorSchemeResources(colorRes);
        imgLoading = findViewById(R.id.img_recycler_loading);
        rotate = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
        imgLoading.startAnimation(rotate);
    }

    public void setNoDataImgRes(@DrawableRes int dataImgRes) {
        noDataImgRes = dataImgRes;
        imageView.setImageResource(dataImgRes);
    }

    public void setNodataHint(String hint) {
        textView.setText(hint);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setFresh(boolean isFresh) {
        swipeRefreshLayout.setRefreshing(isFresh);
    }

    public RecyclerView.Adapter getAdapter() {
        return recyclerView.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void stopLoading() {
        rotate.cancel();
        loadingLayout.setVisibility(GONE);
        setFresh(false);
    }

    public void setRefreshble(boolean can) {
        swipeRefreshLayout.setEnabled(can);
    }

    @Override public boolean canSwipeRefreshChildScrollUp() {
        return recyclerView.canScrollVertically(-1);
    }
}
