package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeEduExpItem extends AbstractFlexibleItem<ResumeEduExpItem.ResumeEduExpVH> {

  Education education;

  public ResumeEduExpItem(Education education) {
    this.education = education;
  }

  public Education getEducation() {
    return education;
  }

  public void setEducation(Education education) {
    this.education = education;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_edu_exp;
  }

  @Override public ResumeEduExpVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeEduExpVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeEduExpVH holder, int position, List payloads) {
    holder.tvUniversityName.setText(education.name);
    holder.tvDegree.setText(education.major + "|" + RecruitBusinessUtils.getDegree(holder.tvDegree.getContext(), education.education));
    holder.tvPeriod.setText(DateUtils.date2YYMM(DateUtils.formatDateFromServer(education.start)) + "至" + DateUtils.date2YYMM(
        DateUtils.formatDateFromServer(education.end)));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeEduExpVH extends FlexibleViewHolder {
	TextView tvUniversityName;
	TextView tvDegree;
	TextView tvPeriod;

    public ResumeEduExpVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvUniversityName = (TextView) view.findViewById(R.id.tv_university_name);
      tvDegree = (TextView) view.findViewById(R.id.tv_degree);
      tvPeriod = (TextView) view.findViewById(R.id.tv_period);
    }
  }
}