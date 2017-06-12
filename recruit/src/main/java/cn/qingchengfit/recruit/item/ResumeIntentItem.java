package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.support.widgets.CompatTextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeIntentItem extends AbstractFlexibleItem<ResumeIntentItem.ResumeIntentVH> {



    @Override public int getLayoutRes() {
        return R.layout.item_resume_work_intent;
    }

    @Override public ResumeIntentVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeIntentVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeIntentVH holder, int position, List payloads) {

    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeIntentVH extends FlexibleViewHolder {
        @BindView(R2.id.tv_position) TextView tvPosition;
        @BindView(R2.id.tv_city) TextView tvCity;
        @BindView(R2.id.tv_salary) TextView tvSalary;
        @BindView(R2.id.layout_job_intent) LinearLayout layoutJobIntent;
        @BindView(R2.id.tv_current_status) CompatTextView tvCurrentStatus;
        public ResumeIntentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}