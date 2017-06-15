package cn.qingchengfit.recruit.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.PhotoUtils;
import com.google.android.flexbox.FlexboxLayout;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeWorkExpItem extends AbstractFlexibleItem<ResumeWorkExpItem.ResumeWorkExpVH> {

    WorkExp workExp ;
    Context context;

    public ResumeWorkExpItem(WorkExp workExp, Context context) {
        this.workExp = workExp;
        this.context = context;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_resume_workexp;
    }

    @Override public ResumeWorkExpVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeWorkExpVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeWorkExpVH holder, int position, List payloads) {
        if (workExp.gym != null){
        //健身房信息
            PhotoUtils.small(holder.imgGym,workExp.gym.photo);
            holder.tvGymBrand.setText(workExp.gym.brand_name);

        }
        holder.tvDesc.setText(workExp.description);
        //认证信息，+ 图章
        if (workExp.is_authenticated){
            holder.imgQcComfirm.setVisibility(View.VISIBLE);
            holder.tvTraierScore.setVisibility(View.VISIBLE);
            holder.flTrainerTags.setVisibility(View.VISIBLE);
            // TODO: 2017/6/12 展示标签 高亮的问题
            holder.tvTraierScore.setText(context.getString(R.string.trainer_score,"4.5","4.7"));
        }else {
            holder.imgQcComfirm.setVisibility(View.GONE);
            holder.tvTraierScore.setVisibility(View.GONE);
            holder.flTrainerTags.setVisibility(View.VISIBLE);
        }
        //团课信息展示
        if (!workExp.group_is_hidden){
            holder.layoutGroupMenberInfo.setVisibility(View.VISIBLE);
            holder.tvGroupCount.setText(context.getString(R.string.course_count,workExp.group_course));
            holder.tvGroupMenber.setText(context.getString(R.string.course_menber,workExp.group_user));
        }else {
            holder.layoutGroupMenberInfo.setVisibility(View.GONE);
        }
        if (!workExp.private_is_hidden){
            holder.layoutPrivateMenberInfo.setVisibility(View.VISIBLE);
            holder.tvPrivateCount.setText(context.getString(R.string.course_count,workExp.private_course));
            holder.tvPrivateMenber.setText(context.getString(R.string.course_menber,workExp.private_user));
        }else {
            holder.layoutPrivateMenberInfo.setVisibility(View.GONE);
        }
        if (!workExp.sale_is_hidden){
            holder.layoutSaleInfo.setVisibility(View.VISIBLE);
            holder.tvSale.setText(context.getString(R.string.course_money, CmStringUtils.getMoneyStr(workExp.sale)));
        }else {
            holder.layoutSaleInfo.setVisibility(View.GONE);
        }

    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeWorkExpVH extends FlexibleViewHolder {
        @BindView(R2.id.img_gym) ImageView imgGym;
        @BindView(R2.id.tv_gym_brand) TextView tvGymBrand;
        @BindView(R2.id.tv_gym_position) TextView tvGymPosition;
        @BindView(R2.id.tv_gym_name) TextView tvGymName;
        @BindView(R2.id.img_qc_comfirm) ImageView imgQcComfirm;
        @BindView(R2.id.indicator) View indicator;
        @BindView(R2.id.tv_group_count) TextView tvGroupCount;
        @BindView(R2.id.tv_group_menber) TextView tvGroupMenber;
        @BindView(R2.id.layout_group_menber_info) LinearLayout layoutGroupMenberInfo;
        @BindView(R2.id.tv_private_count) TextView tvPrivateCount;
        @BindView(R2.id.tv_private_menber) TextView tvPrivateMenber;
        @BindView(R2.id.layout_private_menber_info) LinearLayout layoutPrivateMenberInfo;
        @BindView(R2.id.tv_sale) TextView tvSale;
        @BindView(R2.id.layout_sale_info) LinearLayout layoutSaleInfo;
        @BindView(R2.id.tv_traier_score) TextView tvTraierScore;
        @BindView(R2.id.fl_trainer_tags) FlexboxLayout flTrainerTags;
        @BindView(R2.id.tv_desc) TextView tvDesc;

        public ResumeWorkExpVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}