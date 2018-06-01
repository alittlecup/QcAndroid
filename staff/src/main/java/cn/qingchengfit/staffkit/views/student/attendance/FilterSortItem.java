package cn.qingchengfit.staffkit.views.student.attendance;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

  @Override public void bindViewHolder(FlexibleAdapter adapter, FilterSortVH holder, int position,
      List payloads) {
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
      holder.llHighToLow.setBackgroundDrawable(
          holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort));
      holder.llLowToHigh.setBackgroundDrawable(
          holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort_grey));

      holder.textH2L.setTextColor(holder.green);
      holder.textL2H.setTextColor(holder.grey);

      holder.imageH2L.setImageResource(R.drawable.ic_arrow_downward_green);
      holder.imageL2H.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
    }

    if (data.isLowToHigh) {
      holder.llHighToLow.setBackgroundDrawable(
          holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort_grey));
      holder.llLowToHigh.setBackgroundDrawable(
          holder.itemView.getResources().getDrawable(R.drawable.bg_cornor_sort));

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

    LinearLayout llHighToLow;
    LinearLayout llLowToHigh;
    TextView textH2L;
    TextView textL2H;
    ImageView imageH2L;
    ImageView imageL2H;
    TextView absenceTitle;
    int green;
    int grey;
    Drawable bgCornorGrey;

    public FilterSortVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      green = view.getResources().getColor(R.color.qc_allotsale_green);
      grey = view.getResources().getColor(R.color.qc_text_grey);
      bgCornorGrey = view.getResources().getDrawable(R.drawable.bg_cornor_sort_grey);
      llHighToLow = (LinearLayout) view.findViewById(R.id.absence_high_to_low);
      llLowToHigh = (LinearLayout) view.findViewById(R.id.absence_low_to_high);
      textH2L = (TextView) view.findViewById(R.id.text_absence_high_to_low);
      textL2H = (TextView) view.findViewById(R.id.text_absence_low_to_high);
      imageH2L = (ImageView) view.findViewById(R.id.image_absence_high_to_low);
      imageL2H = (ImageView) view.findViewById(R.id.image_absence_low_to_high);
      absenceTitle = (TextView) view.findViewById(R.id.absence_sort_title);
    }
  }
}
