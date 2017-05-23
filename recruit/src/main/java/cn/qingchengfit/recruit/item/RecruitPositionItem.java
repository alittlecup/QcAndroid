package cn.qingchengfit.recruit.item;

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
import cn.qingchengfit.recruit.model.Job;
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

    @Override public int getLayoutRes() {
        return R.layout.item_recruit_position;
    }

    @Override public RecruitPositionVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new RecruitPositionVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, RecruitPositionVH holder, int position, List payloads) {
        holder.tvPositionName.setText(job.name);
        if (job.gym != null){
            holder.tvGymInfo.setText(job.gym.getDistrictStr()+" · "+job.gym.getName());
            PhotoUtils.small(holder.imgGym,job.gym.getPhoto());
        } else holder.tvGymInfo.setText("");
        holder.tvSalary.setText((int)job.min_salary/1000 +"-"+(int)job.max_salary/1000+"K");
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
        public RecruitPositionVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}