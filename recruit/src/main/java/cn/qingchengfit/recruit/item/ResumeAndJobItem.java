package cn.qingchengfit.recruit.item;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.network.response.JobListIndex;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

import static android.view.View.GONE;

public class ResumeAndJobItem extends AbstractFlexibleItem<ResumeAndJobItem.ResumeAndJobVH> {

  JobListIndex jobListIndex;
  private boolean isLogin;

  public ResumeAndJobItem(boolean isLogin) {
    this.isLogin = isLogin;
  }

  public JobListIndex getJobListIndex() {
    return jobListIndex;
  }

  public void setJobListIndex(JobListIndex jobListIndex) {
    this.jobListIndex = jobListIndex;
  }

  public void setLogin(boolean login) {
    isLogin = login;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_and_jobfair;
  }

  @Override public ResumeAndJobVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ResumeAndJobVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeAndJobVH holder, int position,
      List payloads) {
    if (jobListIndex != null) {
      PhotoUtils.smallCircle(holder.imgMyResume, jobListIndex.avatar,
          R.drawable.vd_default_myprofile, R.drawable.vd_default_myprofile);
      PhotoUtils.smallCircle(holder.imgMyJobFair, jobListIndex.fair_banner,
          R.drawable.vd_default_jobfair, R.drawable.vd_default_jobfair);
      if (!isLogin) {
        holder.tvResumeCompletedTip.setVisibility(GONE);
        holder.tvResumeCompleted.setText("登陆查看");
        holder.tvResumeCompleted.setTextColor(
            ContextCompat.getColor(holder.itemView.getContext(), R.color.text_warm));
      } else {
        holder.tvResumeCompletedTip.setVisibility(View.VISIBLE);
        holder.tvResumeCompleted.setText(jobListIndex.completion + "%");
        holder.tvResumeCompleted.setTextColor(CompatUtils.getColor(holder.tvResumeCompleted.getContext(),
            jobListIndex.completion.floatValue() >= RecruitConstants.RESUME_COMPLETED ? R.color.text_grey : R.color.red));
      }
      if (!isLogin) {
        holder.tvJobFair.setText("登陆查看");
      } else {
        holder.tvJobFair.setText(jobListIndex.fair_count + "场进行中");
      }
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeAndJobVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_resume_completed) TextView tvResumeCompleted;
    @BindView(R2.id.layout_my_resume) RelativeLayout layoutMyResume;
    @BindView(R2.id.tv_job_fair) TextView tvJobFair;
    @BindView(R2.id.layout_my_jobfair) RelativeLayout layoutMyJobfair;
    @BindView(R2.id.img_my_resume) ImageView imgMyResume;
    @BindView(R2.id.img_my_job_fair) ImageView imgMyJobFair;
    @BindView(R2.id.tv_resume_completed_tip) TextView tvResumeCompletedTip;

    public ResumeAndJobVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      layoutMyJobfair.setOnClickListener(this);
      layoutMyResume.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (view.getId() == R.id.layout_my_resume || view.getId() == R.id.layout_my_jobfair) {
        RxBus.getBus().post(new EventClickViewPosition.Builder().id(view.getId()).build());
        return;
      }
      super.onClick(view);
    }
  }
}