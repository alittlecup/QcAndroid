package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class JobFairHorizonItem extends AbstractFlexibleItem<JobFairHorizonItem.JobFairVH> {

  JobFair jobFair;

  public JobFairHorizonItem(JobFair jobFair) {
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

  @Override public JobFairVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new JobFairVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, JobFairVH holder, int position, List payloads) {
    holder.tvTitle.setText(jobFair.name);
    holder.tvContent.setVisibility(View.GONE);
    PhotoUtils.origin(holder.imgBg, jobFair.banner);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class JobFairVH extends FlexibleViewHolder {
	ImageView imgBg;
	TextView tvTitle;
	TextView tvContent;
	TextView tvStatus;

    public JobFairVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgBg = (ImageView) view.findViewById(R.id.img_bg);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      tvContent = (TextView) view.findViewById(R.id.tv_content);
      tvStatus = (TextView) view.findViewById(R.id.tv_status);
    }
  }
}