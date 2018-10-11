package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class JobFairFooterItem extends AbstractFlexibleItem<JobFairFooterItem.JobFairFooterVH> {

  int total;

  public JobFairFooterItem(int total) {
    this.total = total;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_more_job_fair;
  }

  @Override public JobFairFooterVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new JobFairFooterVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, JobFairFooterVH holder, int position, List payloads) {
    holder.tvTotal.setText(holder.tvTotal.getContext().getString(R.string.some_count_job_fair, total));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class JobFairFooterVH extends FlexibleViewHolder {
	TextView tvTotal;

    public JobFairFooterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTotal = (TextView) view.findViewById(R.id.tv_total);
    }
  }
}