package cn.qingchengfit.views.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by fb on 2017/3/6.
 */

public class FilterCommonLinearItem extends AbstractFlexibleItem<FilterCommonLinearItem.FilterCommonLinearVH> {

    private String data;
    private boolean isShowState = true;

    public FilterCommonLinearItem(String data) {
        this.data = data;
    }

    public FilterCommonLinearItem(String data, boolean isShowState){
        this.data = data;
        this.isShowState = isShowState;
    }

    @Override public int getLayoutRes() {
        return R.layout.layout_linear_pop;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public String getData() {
        return data;
    }

    @Override public FilterCommonLinearVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        FilterCommonLinearVH holder = new FilterCommonLinearVH(inflater.inflate(getLayoutRes(), null, false), adapter);
        return holder;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, FilterCommonLinearVH holder, int position, List payloads) {
        holder.textPopFilterCommon.setText(data);

        if (!adapter.isSelected(position)) {
            holder.imageViewFilterCommon.setVisibility(View.INVISIBLE);
            holder.textPopFilterCommon.setTextColor(holder.itemView.getResources().getColor(R.color.qc_text_black));
        } else {
            holder.imageViewFilterCommon.setVisibility(View.VISIBLE);
            holder.textPopFilterCommon.setTextColor(holder.itemView.getResources().getColor(R.color.qc_allotsale_green));
        }
        if (!isShowState){
            holder.imageViewFilterCommon.setVisibility(View.INVISIBLE);
        }
    }

    class FilterCommonLinearVH extends FlexibleViewHolder {

        @BindView(R2.id.tv_filter_pop_linear_common) TextView textPopFilterCommon;
        @BindView(R2.id.image_filter_pop_linear_confirm) ImageView imageViewFilterCommon;

        public FilterCommonLinearVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        @Override public void onClick(View view) {
            super.onClick(view);
        }
    }
}
