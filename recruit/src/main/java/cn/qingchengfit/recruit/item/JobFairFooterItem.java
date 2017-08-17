package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
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

  @Override public JobFairFooterVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new JobFairFooterVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, JobFairFooterVH holder, int position, List payloads) {
    holder.tvTotal.setText(holder.tvTotal.getContext().getString(R.string.some_count_job_fair, total));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class JobFairFooterVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_total) TextView tvTotal;

    public JobFairFooterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}