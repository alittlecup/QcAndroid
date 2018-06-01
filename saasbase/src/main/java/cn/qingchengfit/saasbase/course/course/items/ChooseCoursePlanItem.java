package cn.qingchengfit.saasbase.course.course.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.course.course.bean.CoursePlan;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseCoursePlanItem extends
  AbstractFlexibleItem<ChooseCoursePlanItem.ChooseCoursePlanVH> {

    public CoursePlan coursePlan;

    public ChooseCoursePlanItem(CoursePlan coursePlan) {
        this.coursePlan = coursePlan;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_course_plan;
    }

    @Override public ChooseCoursePlanVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseCoursePlanVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseCoursePlanVH holder, int position, List payloads) {
        holder.name.setText(coursePlan.getName());
        if (coursePlan.getTags() != null) {
            String tags = "";
            for (int i = 0; i < coursePlan.getTags().size(); i++) {
                tags = tags.concat(coursePlan.getTags().get(i)).concat(i == coursePlan.getTags().size() - 1 ? "" : Configs.SEPARATOR);
            }
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(tags);
        } else {
            holder.content.setVisibility(View.GONE);
        }
        holder.chosen.setImageResource(adapter.isSelected(position) ? R.drawable.ic_green_right : R.drawable.ic_arrow_right);
    }

    public static class ChooseCoursePlanVH extends FlexibleViewHolder {
	TextView name;
	TextView content;
	ImageView chosen;

        public ChooseCoursePlanVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          name = (TextView) view.findViewById(R.id.name);
          content = (TextView) view.findViewById(R.id.content);
          chosen = (ImageView) view.findViewById(R.id.chosen);
        }
    }
}