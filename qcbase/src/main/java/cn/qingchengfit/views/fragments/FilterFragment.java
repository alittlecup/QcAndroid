package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/3/7.
 */

public class FilterFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R2.id.recycle_popwindow_common) RecyclerView rlPopWindowCommon;

    private CommonFlexAdapter commonFlexAdapter;
    private OnSelectListener onSelectListener;
    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private int maxHeight;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_pop_common, null, false);
        unbinder = ButterKnife.bind(this, view);
        rlPopWindowCommon.setLayoutManager(new LinearLayoutManager(getContext()));
        rlPopWindowCommon.setAdapter(commonFlexAdapter);
        commonFlexAdapter.setMode(SelectableAdapter.MODE_SINGLE);
        commonFlexAdapter.addListener(this);
        return view;
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isVisible()) {
        }
    }

    @Override protected void onInVisible() {
        super.onInVisible();
    }

    public void setItemList(List<? extends AbstractFlexibleItem> items) {
        if (items.size() > 0) {
            this.items.clear();
            this.items.addAll(items);
            if(commonFlexAdapter == null) {
                commonFlexAdapter = new CommonFlexAdapter(this.items, this);
            }
            commonFlexAdapter.notifyDataSetChanged();
        }
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public AbstractFlexibleItem getItemAtPosition(int position){
        return items.get(position);
    }

    public int getItemCount(){
        return commonFlexAdapter.getItemCount();
    }

    public void setFilterAnimation(final ViewGroup filterLayout, boolean isShow) {
        final ViewGroup.LayoutParams params = filterLayout.getLayoutParams();
        params.height = 0;
        filterLayout.setLayoutParams(params);
        final int startHeight;
        final int endHeight;
        if (isShow) {
            startHeight = 0;
            int height = commonFlexAdapter.getItemCount() * MeasureUtils.dpToPx(40f, getResources());
            if (maxHeight > 0) {
                endHeight = height > maxHeight ? maxHeight : height;
            }else{
                endHeight = height;
            }
        } else {
            int height = commonFlexAdapter.getItemCount() * MeasureUtils.dpToPx(40f, getResources());
            if (maxHeight > 0) {
                startHeight = height > maxHeight ? maxHeight : height;
            }else{
                startHeight = height;
            }
            endHeight = 0;
        }

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(startHeight, endHeight);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (startHeight < endHeight) {
                    params.height = (int) (endHeight * fraction);
                } else {
                    params.height = startHeight - (int) (startHeight * fraction);
                }
                filterLayout.setLayoutParams(params);
            }
        });
        valueAnimator.start();
    }

    public float getViewHeight(){
        return commonFlexAdapter.getItemCount() * MeasureUtils.dpToPx(40f, getResources());
    }

    @Override public String getFragmentName() {
        return FilterFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        int tmpPos =
            commonFlexAdapter.getSelectedItemCount() > 0 ? commonFlexAdapter.getSelectedPositions()
                .get(0) : 0;
        commonFlexAdapter.toggleSelection(position);
        commonFlexAdapter.notifyItemChanged(tmpPos);
        commonFlexAdapter.notifyItemChanged(position);
        if (onSelectListener != null) {
            onSelectListener.onSelectItem(position);
        }
        return true;
    }

    public void refresh() {
        if (commonFlexAdapter != null) {
            commonFlexAdapter.notifyDataSetChanged();
        }
    }

    public interface OnSelectListener {
        void onSelectItem(int position);
    }
}
