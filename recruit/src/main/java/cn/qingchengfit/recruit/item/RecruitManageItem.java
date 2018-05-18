package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RecruitManageItem extends AbstractFlexibleItem<RecruitManageItem.RecruitManageVH> {

  private int jobs;
  private int gymcount;
  private boolean show;

  public RecruitManageItem(int jobs, int gymcount, boolean show) {
    this.jobs = jobs;
    this.show = show;
    this.gymcount = gymcount;
  }

  public void setJobCounts(int jobCounts, int gymCount) {
    this.jobs = jobCounts;
    this.gymcount = gymCount;
  }

  public int getJobsCount() {
    return jobs;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_recruit_manage;
  }

  @Override
  public RecruitManageVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new RecruitManageVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, RecruitManageVH holder, int position,
      List payloads) {
    holder.imgRedDot.setVisibility(show ? View.VISIBLE : View.GONE);
    if (jobs > 0) {
      holder.tvPublishJobs.setTextColor(
          CompatUtils.getColor(holder.tvPublishJobs.getContext(), R.color.text_grey));
      holder.tvPublishJobs.setText(holder.tvPublishJobs.getContext()
          .getString(R.string.recruit_has_publish_jobs_num, gymcount, jobs));
    } else {
      holder.tvPublishJobs.setTextColor(
          AppUtils.getPrimaryColor(holder.tvPublishJobs.getContext()));
      holder.tvPublishJobs.setText("发布新职位");
    }
  }

  @Override public boolean equals(Object o) {
    return o instanceof RecruitManageItem;
  }

  public class RecruitManageVH extends FlexibleViewHolder {
	ImageView imgRedDot;
	TextView tvPublishJobs;

    public RecruitManageVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgRedDot = (ImageView) view.findViewById(R.id.img_red_dot);
      tvPublishJobs = (TextView) view.findViewById(R.id.tv_publish_jobs);
    }
  }
}