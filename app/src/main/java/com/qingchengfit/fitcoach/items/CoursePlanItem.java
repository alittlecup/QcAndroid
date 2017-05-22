package com.qingchengfit.fitcoach.items;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.CoursePlan;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CoursePlanItem extends AbstractFlexibleItem<CoursePlanItem.CoursePlanVH> {

    CoursePlan coursePlan;

    public CoursePlanItem(CoursePlan coursePlan) {
        this.coursePlan = coursePlan;
    }

    public String getUrl() {
        if (coursePlan != null) {
            return coursePlan.getUrl();
        } else {
            return "";
        }
    }

    @Override public int getLayoutRes() {
        return R.layout.item_course_plan;
    }

    @Override public CoursePlanVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new CoursePlanVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CoursePlanVH holder, int position, List payloads) {
        holder.name.setText(coursePlan.getName());
        String ct = "";
        if (coursePlan.brand != null) ct = TextUtils.concat(ct, coursePlan.brand.getName(), " | ").toString();
        holder.content.setText(TextUtils.concat(ct, coursePlan.getTagsStr()));
        holder.chosen.setImageResource(R.drawable.ic_arrow_right);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class CoursePlanVH extends FlexibleViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.chosen) ImageView chosen;

        public CoursePlanVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}