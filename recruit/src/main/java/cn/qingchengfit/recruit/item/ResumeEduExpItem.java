package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeEduExpItem extends AbstractFlexibleItem<ResumeEduExpItem.ResumeEduExpVH> {


    @Override public int getLayoutRes() {
        return R.layout.item_edu_exp;
    }

    @Override public ResumeEduExpVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeEduExpVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeEduExpVH holder, int position, List payloads) {

    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeEduExpVH extends FlexibleViewHolder {
        @BindView(R2.id.tv_university_name) TextView tvUniversityName;
        @BindView(R2.id.tv_degree) TextView tvDegree;
        @BindView(R2.id.tv_period) TextView tvPeriod;
        public ResumeEduExpVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}