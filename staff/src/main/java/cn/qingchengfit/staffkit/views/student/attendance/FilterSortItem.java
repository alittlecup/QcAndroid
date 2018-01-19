package cn.qingchengfit.staffkit.views.student.attendance;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/3/7.
 */

public class FilterSortItem extends AbstractFlexibleItem<FilterSortItem.FilterSortVH> {

    private OnSortDataListener sortDataListener;

    private FilterSortBean data;

    public FilterSortItem(FilterSortBean data, OnSortDataListener sortDataListener) {
        this.data = data;
        this.sortDataListener = sortDataListener;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.layout_filter_sort;
    }

    @Override public FilterSortVH createViewHolder(View view, FlexibleAdapter adapter) {
        final FilterSortVH holder = new FilterSortVH(view, adapter);

        holder.llHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                int position = (int) view.getTag();
                sortDataListener.onHighToLow(position);
            }
        });

        holder.llLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                int position = (int) view.getTag();
                sortDataListener.onLowToHigh(position);
            }
        });
        return holder;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, FilterSortVH holder, int position, List payloads) {
        holder.absenceTitle.setText(data.name);
        if (data.isHighToLow || data.isLowToHigh) {
            holder.absenceTitle.setTextColor(holder.green);
        } else {
            holder.absenceTitle.setTextColor(holder.grey);
        }
        holder.llHighToLow.setTag(position);
        holder.llLowToHigh.setTag(position);

        resetViewState(holder);

        if (data.isHighToLow) {
            holder.llHighToLow.setBackgroundDrawable(holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort));
            holder.llLowToHigh.setBackgroundDrawable(holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort_grey));

            holder.textH2L.setTextColor(holder.green);
            holder.textL2H.setTextColor(holder.grey);

            holder.imageH2L.setImageResource(R.drawable.ic_arrow_downward_green);
            holder.imageL2H.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }

        if (data.isLowToHigh) {
            holder.llHighToLow.setBackgroundDrawable(holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort_grey));
            holder.llLowToHigh.setBackgroundDrawable(holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort));

            holder.textH2L.setTextColor(holder.grey);
            holder.textL2H.setTextColor(holder.green);

            holder.imageH2L.setImageResource(R.drawable.ic_arrow_downward_black);
            holder.imageL2H.setImageResource(R.drawable.ic_arrow_uplist_green);
        }
    }

    private void resetViewState(FilterSortVH holder) {
        holder.llHighToLow.setBackgroundDrawable(holder.bgCornorGrey);
        holder.llLowToHigh.setBackgroundDrawable(holder.bgCornorGrey);

        holder.textH2L.setTextColor(holder.grey);
        holder.textL2H.setTextColor(holder.grey);

        holder.imageH2L.setImageResource(R.drawable.ic_arrow_downward_black);
        holder.imageL2H.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
    }

    public interface OnSortDataListener {
        void onHighToLow(int position);

        void onLowToHigh(int position);
    }

    class FilterSortVH extends FlexibleViewHolder {

        @BindView(R.id.absence_high_to_low) LinearLayout llHighToLow;
        @BindView(R.id.absence_low_to_high) LinearLayout llLowToHigh;
        @BindView(R.id.text_absence_high_to_low) TextView textH2L;
        @BindView(R.id.text_absence_low_to_high) TextView textL2H;
        @BindView(R.id.image_absence_high_to_low) ImageView imageH2L;
        @BindView(R.id.image_absence_low_to_high) ImageView imageL2H;
        @BindView(R.id.absence_sort_title) TextView absenceTitle;
        @BindColor(R.color.qc_allotsale_green) int green;
        @BindColor(R.color.qc_text_grey) int grey;
        @BindDrawable(R.drawable.bg_cornor_sort_grey) Drawable bgCornorGrey;

        public FilterSortVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
