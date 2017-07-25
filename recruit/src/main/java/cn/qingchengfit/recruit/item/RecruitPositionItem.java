package cn.qingchengfit.recruit.item;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RecruitPositionItem extends AbstractFlexibleItem<RecruitPositionItem.RecruitPositionVH> {

  Job job;

  public RecruitPositionItem(Job job) {
    this.job = job;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_recruit_position;
  }

  @Override public RecruitPositionVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new RecruitPositionVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RecruitPositionVH holder, int position, List payloads) {
    holder.tvPositionName.setText(job.name);
    if (job.gym != null) {
      holder.tvGymInfo.setText(job.gym.getCityDistrict()
          + (TextUtils.isEmpty(job.gym.getCityDistrict()) ? "" : " · ")
          + job.gym.getBrand_name()
          + job.gym.name);
      PhotoUtils.small(holder.imgGym, job.gym.photo);
    } else {
      holder.tvGymInfo.setText("");
    }
    holder.tvSalary.setText(RecruitBusinessUtils.getSalary(job.min_salary, job.max_salary, "面议"));
    holder.tvGender.setText(RecruitBusinessUtils.getGender(job.gender, "性别"));
    holder.tvHeight.setText(RecruitBusinessUtils.getHeight(job.min_height, job.max_height, "身高"));
    holder.tvAge.setText(RecruitBusinessUtils.getAge(job.min_age, job.max_age, "年龄"));
    holder.tvWorkYear.setText(
        RecruitBusinessUtils.getWorkYear(job.min_work_year, job.max_work_year, "经验"));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class RecruitPositionVH extends FlexibleViewHolder {
    @BindView(R2.id.img_gym) ImageView imgGym;
    @BindView(R2.id.tv_position_name) TextView tvPositionName;
    @BindView(R2.id.tv_salary) TextView tvSalary;
    @BindView(R2.id.tv_gym_info) TextView tvGymInfo;
    @BindView(R2.id.layout_limit) LinearLayout layoutLimit;
    @BindView(R2.id.tv_work_year) TextView tvWorkYear;
    @BindView(R2.id.tv_gender) TextView tvGender;
    @BindView(R2.id.tv_age) TextView tvAge;
    @BindView(R2.id.tv_height) TextView tvHeight;
    @Nullable @BindView(R2.id.cb_position) CheckBox checkBox;
    @Nullable @BindView(R2.id.tv_has_undo) TextView tv_has_todo;
    @Nullable @BindView(R2.id.img_right) ImageView imgRight;

    public RecruitPositionVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}