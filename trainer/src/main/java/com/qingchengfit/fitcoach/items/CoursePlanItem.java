package com.qingchengfit.fitcoach.items;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.qingchengfit.fitcoach.R;
import cn.qingchengfit.bean.CoursePlan;
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

    @Override public CoursePlanVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new CoursePlanVH(view, adapter);
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
	TextView name;
	TextView content;
	ImageView chosen;

        public CoursePlanVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          name = (TextView) view.findViewById(R.id.name);
          content = (TextView) view.findViewById(R.id.content);
          chosen = (ImageView) view.findViewById(R.id.chosen);
        }
    }
}