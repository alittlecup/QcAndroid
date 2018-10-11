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


import cn.qingchengfit.recruit.R;

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

  @Override public RecruitPositionVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new RecruitPositionVH(view, adapter);
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
	ImageView imgGym;
	TextView tvPositionName;
	TextView tvSalary;
	TextView tvGymInfo;
	LinearLayout layoutLimit;
	TextView tvWorkYear;
	TextView tvGender;
	TextView tvAge;
	TextView tvHeight;
	CheckBox checkBox;
	TextView tv_has_todo;
	ImageView imgRight;

    public RecruitPositionVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgGym = (ImageView) view.findViewById(R.id.img_gym);
      tvPositionName = (TextView) view.findViewById(R.id.tv_position_name);
      tvSalary = (TextView) view.findViewById(R.id.tv_salary);
      tvGymInfo = (TextView) view.findViewById(R.id.tv_gym_info);
      layoutLimit = (LinearLayout) view.findViewById(R.id.layout_limit);
      tvWorkYear = (TextView) view.findViewById(R.id.tv_work_year);
      tvGender = (TextView) view.findViewById(R.id.tv_gender);
      tvAge = (TextView) view.findViewById(R.id.tv_age);
      tvHeight = (TextView) view.findViewById(R.id.tv_height);
      checkBox = (CheckBox) view.findViewById(R.id.cb_position);
      tv_has_todo = (TextView) view.findViewById(R.id.tv_has_undo);
      imgRight = (ImageView) view.findViewById(R.id.img_right);
    }
  }
}