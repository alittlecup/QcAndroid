package cn.qingchengfit.recruit.item;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeBaseInfoItem extends AbstractFlexibleItem<ResumeBaseInfoItem.ResumeBaseInfoVH> {

  ResumeHome resumeHome;

  public ResumeBaseInfoItem(ResumeHome resumeHome) {
    this.resumeHome = resumeHome;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_base_info;
  }

  @Override public ResumeBaseInfoVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeBaseInfoVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeBaseInfoVH holder, int position, List payloads) {
    PhotoUtils.smallCircle(holder.imgAvatar, resumeHome.avatar);
    holder.tvUsername.setText(resumeHome.username);
    holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(null, null,
        ContextCompat.getDrawable(holder.tvUsername.getContext(),
            resumeHome.gender == 0 ? R.drawable.vd_gender_male : R.drawable.vd_gender_female),
        null);
    holder.tvAge.setText(DateUtils.getAge(DateUtils.formatDateFromServer(resumeHome.birthday)) + "岁");
    holder.tvDegree.setText(RecruitBusinessUtils.getDegree(holder.tvDegree.getContext(), resumeHome.max_education));
    holder.tvHeight.setText(resumeHome.height == 0 ? "--" : (int) resumeHome.height + "cm");
    holder.tvWeight.setText(resumeHome.weight == 0 ? "--" : (int) resumeHome.weight + "kg");
    holder.tvWorkYear.setText(resumeHome.work_year == 0 ? "应届" : resumeHome.work_year + "年");
    holder.tvSignWord.setText(resumeHome.brief_description.trim());
    holder.tvSignWord.setHint("一句话介绍");
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeBaseInfoVH extends FlexibleViewHolder {
	ImageView imgAvatar;
	CompatTextView tvUsername;
	TextView tvSignWord;
	CompatTextView tvWorkYear;
	CompatTextView tvAge;
	CompatTextView tvDegree;
	CompatTextView tvHeight;
	CompatTextView tvWeight;

    public ResumeBaseInfoVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
      tvUsername = (CompatTextView) view.findViewById(R.id.tv_username);
      tvSignWord = (TextView) view.findViewById(R.id.tv_sign_word);
      tvWorkYear = (CompatTextView) view.findViewById(R.id.tv_work_year);
      tvAge = (CompatTextView) view.findViewById(R.id.tv_age);
      tvDegree = (CompatTextView) view.findViewById(R.id.tv_degree);
      tvHeight = (CompatTextView) view.findViewById(R.id.tv_height);
      tvWeight = (CompatTextView) view.findViewById(R.id.tv_weight);
    }
  }
}