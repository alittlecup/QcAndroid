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
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RecruitManageItem extends AbstractFlexibleItem<RecruitManageItem.RecruitManageVH> {

  private int jobs;
  private boolean show;

  public RecruitManageItem(int jobs, boolean show) {
    this.jobs = jobs;
    this.show = show;
  }

  public void setJobCounts(int jobCounts) {
    this.jobs = jobCounts;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_recruit_manage;
  }

  @Override
  public RecruitManageVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new RecruitManageVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, RecruitManageVH holder, int position,
      List payloads) {
    holder.imgRedDot.setVisibility(show ? View.VISIBLE : View.GONE);
    holder.tvPublishJobs.setText(
        holder.tvPublishJobs.getContext().getString(R.string.recruit_has_publish_jobs_num, jobs));
  }

  @Override public boolean equals(Object o) {
    return o instanceof RecruitManageItem;
  }

  public class RecruitManageVH extends FlexibleViewHolder {
    @BindView(R2.id.img_red_dot) ImageView imgRedDot;
    @BindView(R2.id.tv_publish_jobs) TextView tvPublishJobs;

    public RecruitManageVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}