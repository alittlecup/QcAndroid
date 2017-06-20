package cn.qingchengfit.recruit.item;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
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

  @Override public ResumeBaseInfoVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new ResumeBaseInfoVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeBaseInfoVH holder, int position, List payloads) {
    PhotoUtils.smallCircle(holder.imgAvatar, resumeHome.avatar);
    holder.tvUsername.setText(resumeHome.username);
    holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(holder.tvUsername.getContext(),
        resumeHome.gender == 0 ? R.drawable.vd_gender_male : R.drawable.vd_gender_female), null, null, null);
    holder.tvAge.setText(DateUtils.getAge(DateUtils.formatDateFromServer(resumeHome.birthday)) + "岁");
    holder.tvDegree.setText(RecruitBusinessUtils.getDegree(holder.tvDegree.getContext(), resumeHome.max_education));
    holder.tvHeight.setText(resumeHome.height == 0 ? "--" : resumeHome.height + "cm");
    holder.tvWeight.setText(resumeHome.weight == 0 ? "--" : resumeHome.weight + "kg");
    holder.tvWorkYear.setText(resumeHome.work_year == 0 ? "应届" : resumeHome.work_year + "年");
    holder.tvSignWord.setText(resumeHome.brief_description);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeBaseInfoVH extends FlexibleViewHolder {
    @BindView(R2.id.img_avatar) ImageView imgAvatar;
    @BindView(R2.id.tv_username) CompatTextView tvUsername;
    @BindView(R2.id.tv_sign_word) TextView tvSignWord;
    @BindView(R2.id.tv_work_year) CompatTextView tvWorkYear;
    @BindView(R2.id.tv_age) CompatTextView tvAge;
    @BindView(R2.id.tv_degree) CompatTextView tvDegree;
    @BindView(R2.id.tv_height) CompatTextView tvHeight;
    @BindView(R2.id.tv_weight) CompatTextView tvWeight;

    public ResumeBaseInfoVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}