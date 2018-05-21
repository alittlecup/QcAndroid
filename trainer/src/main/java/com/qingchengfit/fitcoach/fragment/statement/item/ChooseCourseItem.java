package com.qingchengfit.fitcoach.fragment.statement.item;

import android.view.View;
import android.widget.TextView;


import com.qingchengfit.fitcoach.R;
import cn.qingchengfit.bean.CourseDetail;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseCourseItem extends AbstractFlexibleItem<ChooseCourseItem.ChooseStudentVH> {

    public CourseDetail mCourse;

    public ChooseCourseItem(CourseDetail course) {
        mCourse = course;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_student;
    }

    @Override public ChooseStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStudentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        holder.itemText.setText(mCourse.getName());
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChooseStudentVH extends FlexibleViewHolder {
	TextView itemText;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemText = (TextView) view.findViewById(R.id.item_text);
        }
    }
}