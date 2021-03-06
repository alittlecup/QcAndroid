package cn.qingchengfit.recruit.item;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.QcTagGroup;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeWorkExpItem extends AbstractFlexibleItem<ResumeWorkExpItem.ResumeWorkExpVH> {

  WorkExp workExp;
  Context context;
  boolean showAll;

  public ResumeWorkExpItem(WorkExp workExp, Context context) {
    this.workExp = workExp;
    this.context = context;
  }

  public WorkExp getWorkExp() {
    return workExp;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_resume_workexp;
  }

  @Override public ResumeWorkExpVH createViewHolder(View view, FlexibleAdapter adapter) {
    ResumeWorkExpVH vh = new ResumeWorkExpVH(view, adapter);
    return vh;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeWorkExpVH holder, int position, List payloads) {
    if (workExp.gym != null) {
      //健身房信息
      if (workExp.gym.brand != null) {
        PhotoUtils.small(holder.imgGym, workExp.gym.brand.photo);
        holder.tvGymBrand.setText(workExp.gym.brand.getName());
        holder.tvGymPosition.setText("职位：" + workExp.position);
        holder.tvGymName.setText(TextUtils.isEmpty(workExp.gym.getCityName()) ? ""
            : (workExp.gym.getCityName() + " · ") + workExp.gym.name);
      }
    }
    holder.tvDuring.setText(DateUtils.getYYMMfromServer(workExp.start) + "至" + DateUtils.getYYMMfromServer(workExp.end));
    if (TextUtils.isEmpty(workExp.description)) {
      holder.tvDesc.setVisibility(View.GONE);
      holder.short_divider.setVisibility(View.GONE);
    } else {
      holder.tvDesc.setVisibility(View.VISIBLE);
      holder.short_divider.setVisibility(View.VISIBLE);
      holder.tvDesc.setText(workExp.description);
    }

    //认证信息，+ 图章
    if (workExp.is_authenticated) {
      holder.imgQcComfirm.setVisibility(View.VISIBLE);
      holder.tvTraierScore.setVisibility(View.VISIBLE);
      holder.flTrainerTags.setVisibility(View.VISIBLE);
      holder.layoutSyncFromQc.setVisibility(View.VISIBLE);
      holder.layoutScore.setVisibility(View.VISIBLE);
      holder.tvTraierScore.setText(CmStringUtils.getFloatDot1(workExp.coach_score));
      holder.tvCourseScore.setText(CmStringUtils.getFloatDot1(workExp.course_score));
      if (workExp.impression != null && workExp.impression.size() > 0) {
        holder.flTrainerTags.removeAllViews();
        int max = workExp.getImpressList().size() > 4 ? 5 : workExp.getImpressList().size();
        if (max < 5) {
          holder.flTrainerTags.setTags(workExp.getImpressList());
        } else {
          holder.flTrainerTags.setTags(workExp.getImpressList().subList(0, 5));
        }
        holder.tvShowAll.setVisibility(workExp.impression.size() > 5 ? View.VISIBLE : View.GONE);
      } else {
        // TODO: 2017/6/14 无印象怎么处理
        holder.tvShowAll.setVisibility(View.GONE);
      }
    } else {
      holder.layoutScore.setVisibility(View.GONE);
      holder.imgQcComfirm.setVisibility(View.GONE);
      holder.layoutSyncFromQc.setVisibility(View.GONE);
      holder.flTrainerTags.setVisibility(View.GONE);
      holder.tvShowAll.setVisibility(View.GONE);
    }
    //团课信息展示
    if (!workExp.group_is_hidden) {
      holder.layoutGroupMenberInfo.setVisibility(View.VISIBLE);
      holder.tvGroupCount.setText(context.getString(R.string.course_count, workExp.group_course));
      holder.tvGroupMenber.setText(context.getString(R.string.course_menber, workExp.group_user));
    } else {
      holder.layoutGroupMenberInfo.setVisibility(View.GONE);
    }
    if (!workExp.private_is_hidden) {
      holder.layoutPrivateMenberInfo.setVisibility(View.VISIBLE);
      holder.tvPrivateCount.setText(context.getString(R.string.course_count, workExp.private_course));
      holder.tvPrivateMenber.setText(context.getString(R.string.course_menber, workExp.private_user));
    } else {
      holder.layoutPrivateMenberInfo.setVisibility(View.GONE);
    }
    if (!workExp.sale_is_hidden) {
      holder.layoutSaleInfo.setVisibility(View.VISIBLE);
      holder.tvSale.setText(context.getString(R.string.course_money, CmStringUtils.getMoneyStr(workExp.sale)));
    } else {
      holder.layoutSaleInfo.setVisibility(View.GONE);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ResumeWorkExpVH extends FlexibleViewHolder {
	ImageView imgGym;
	View short_divider;
	TextView tvGymBrand;
	TextView tvGymPosition;
	TextView tvGymName;
	ImageView imgQcComfirm;
	View indicator;
	TextView tvGroupCount;
	TextView tvGroupMenber;
	LinearLayout layoutGroupMenberInfo;
	TextView tvPrivateCount;
	TextView tvPrivateMenber;
	LinearLayout layoutPrivateMenberInfo;
	LinearLayout layoutSyncFromQc;
	LinearLayout layoutScore;
	TextView tvSale;
	LinearLayout layoutSaleInfo;
	TextView tvTraierScore;
	TextView tvCourseScore;
	QcTagGroup flTrainerTags;
	TextView tvDesc;
	TextView tvDuring;
	CompatTextView tvShowAll;

    public ResumeWorkExpVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      imgGym = (ImageView) view.findViewById(R.id.img_gym);
      short_divider = (View) view.findViewById(R.id.short_divider);
      tvGymBrand = (TextView) view.findViewById(R.id.tv_gym_brand);
      tvGymPosition = (TextView) view.findViewById(R.id.tv_gym_position);
      tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
      imgQcComfirm = (ImageView) view.findViewById(R.id.img_qc_comfirm);
      indicator = (View) view.findViewById(R.id.indicator);
      tvGroupCount = (TextView) view.findViewById(R.id.tv_group_count);
      tvGroupMenber = (TextView) view.findViewById(R.id.tv_group_menber);
      layoutGroupMenberInfo = (LinearLayout) view.findViewById(R.id.layout_group_menber_info);
      tvPrivateCount = (TextView) view.findViewById(R.id.tv_private_count);
      tvPrivateMenber = (TextView) view.findViewById(R.id.tv_private_menber);
      layoutPrivateMenberInfo = (LinearLayout) view.findViewById(R.id.layout_private_menber_info);
      layoutSyncFromQc = (LinearLayout) view.findViewById(R.id.layout_sync_from_qc);
      layoutScore = (LinearLayout) view.findViewById(R.id.layout_score);
      tvSale = (TextView) view.findViewById(R.id.tv_sale);
      layoutSaleInfo = (LinearLayout) view.findViewById(R.id.layout_sale_info);
      tvTraierScore = (TextView) view.findViewById(R.id.tv_trainer_score);
      tvCourseScore = (TextView) view.findViewById(R.id.tv_course_score);
      flTrainerTags = (QcTagGroup) view.findViewById(R.id.fl_trainer_tags);
      tvDesc = (TextView) view.findViewById(R.id.tv_desc);
      tvDuring = (TextView) view.findViewById(R.id.tv_during);
      tvShowAll = (CompatTextView) view.findViewById(R.id.tv_show_all);

      tvShowAll.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          IFlexible item = adapter.getItem(getAdapterPosition());
          if (item instanceof ResumeWorkExpItem) {
            if (((ResumeWorkExpItem) item).showAll) {
              flTrainerTags.removeAllViews();
              flTrainerTags.setTags(
                  ((ResumeWorkExpItem) item).workExp.getImpressList().subList(0, 5));
              tvShowAll.setText("查看全部");
              ((ResumeWorkExpItem) item).showAll = false;
            } else {
              flTrainerTags.removeAllViews();
              flTrainerTags.setTags(((ResumeWorkExpItem) item).workExp.getImpressList());
              tvShowAll.setText("收起");
              ((ResumeWorkExpItem) item).showAll = true;
            }
          }
        }
      });
    }
  }
}