package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeWorkExpItem extends AbstractFlexibleItem<ResumeWorkExpItem.ResumeWorkExpVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_resume_workexp;
    }

    @Override public ResumeWorkExpVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeWorkExpVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeWorkExpVH holder, int position, List payloads) {
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

        @BindView(R2.id.group_course) public ResumeWorkExpVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}