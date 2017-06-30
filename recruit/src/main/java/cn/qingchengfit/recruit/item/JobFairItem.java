package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.model.JobFair;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class JobFairItem extends AbstractFlexibleItem<JobFairItem.JobFairVH> {

  JobFair jobFair;

  public JobFairItem(JobFair jobFair) {
    this.jobFair = jobFair;
  }

  public JobFair getJobFair() {
    return jobFair;
  }

  public void setJobFair(JobFair jobFair) {
    this.jobFair = jobFair;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_jobfairs;
  }

  @Override public JobFairVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new JobFairVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, JobFairVH holder, int position, List payloads) {
    holder.tvTitle.setText("赛普稀里哗啦呼呼呼呼呼呼");
    holder.imgBg.setBackgroundResource(R.color.toolbar);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class JobFairVH extends FlexibleViewHolder {
    @BindView(R2.id.img_bg) ImageView imgBg;
    @BindView(R2.id.tv_title) TextView tvTitle;

    public JobFairVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}