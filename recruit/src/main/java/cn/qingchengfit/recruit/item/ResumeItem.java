package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeItem extends AbstractFlexibleItem<ResumeItem.ResumeVH> {

  private Resume resume;

  public ResumeItem(Resume resume) {
    this.resume = resume;
  }

  public Resume getResume() {
    return resume;
  }

  public void setResume(Resume resume) {
    this.resume = resume;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume;
  }

  @Override public ResumeVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ResumeVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeVH holder, int position,
      List payloads) {
    int defautAvatar =
        resume.gender == 0 ? R.drawable.default_student_male : R.drawable.default_student_female;
    PhotoUtils.smallCircle(holder.imgAvatar, resume.avatar, defautAvatar, defautAvatar);
    holder.tvName.setText(resume.username);
    holder.imgGender.setImageResource(
        resume.gender == 0 ? R.drawable.vd_gender_male : R.drawable.vd_gender_female);
    holder.tvRequirement.setText(resume.getBaseInfoStr(holder.tvRequirement.getContext()));
    holder.tvExcept.setText(resume.getExpStr());
    if (adapter instanceof CommonFlexAdapter && ((CommonFlexAdapter) adapter).getStatus() == -1) {
      holder.cbResume.setVisibility(View.VISIBLE);
      holder.cbResume.setChecked(adapter.isSelected(position));
    } else {
      holder.cbResume.setVisibility(View.GONE);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeVH extends FlexibleViewHolder {
	ImageView imgAvatar;
	TextView tvName;
	ImageView imgGender;
	TextView tvRequirement;
	TextView tvExcept;
	CheckBox cbResume;

    public ResumeVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
      tvName = (TextView) view.findViewById(R.id.tv_name);
      imgGender = (ImageView) view.findViewById(R.id.img_gender);
      tvRequirement = (TextView) view.findViewById(R.id.tv_requirement);
      tvExcept = (TextView) view.findViewById(R.id.tv_except);
      cbResume = (CheckBox) view.findViewById(R.id.cb_resume);
    }
  }
}