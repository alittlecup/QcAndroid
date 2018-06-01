package com.qingchengfit.fitcoach.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import cn.qingchengfit.bean.CourseDetail;
import com.qingchengfit.fitcoach.event.DelCourseEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;
import java.util.Locale;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/2.
 */
public class CourseItem extends AbstractFlexibleItem<CourseItem.CourseVh> {

    public CourseDetail courseDetail;
    private boolean isEditable;
    public CourseItem(CourseDetail courseDetail) {
        this.courseDetail = courseDetail;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_course_saas;
    }

    @Override public CourseVh createViewHolder(View view, FlexibleAdapter adapter) {
        return new CourseVh(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CourseVh holder, int position, List payloads) {
        holder.courseLength.setText(String.format(Locale.CHINA, "时长%d分钟", courseDetail.getLength() / 60));
        holder.courseName.setText(courseDetail.getName());
        holder.courseTypeImg.setImageResource(
            courseDetail.is_private() ? R.drawable.ic_course_private_conner : R.drawable.ic_course_group_conner);
        Glide.with(adapter.getRecyclerView().getContext())
            .load(courseDetail.getPhoto())
            .placeholder(R.drawable.img_loadingimage)
            .into(holder.courseImg);
        if (isEditable) {
            holder.del.setVisibility(View.VISIBLE);
        } else {
            holder.del.setVisibility(View.GONE);
        }
    }

    public static class CourseVh extends FlexibleViewHolder {

	ImageView courseImg;
	ImageView courseTypeImg;
	TextView courseName;
	TextView courseLength;
	ImageView del;

        public CourseVh(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
          courseImg = (ImageView) view.findViewById(R.id.course_img);
          courseTypeImg = (ImageView) view.findViewById(R.id.course_type_img);
          courseName = (TextView) view.findViewById(R.id.course_name);
          courseLength = (TextView) view.findViewById(R.id.course_length);
          del = (ImageView) view.findViewById(R.id.del);

          del.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (mAdapter.getItem(getFlexibleAdapterPosition()) instanceof CourseItem) {
                        boolean isPrivate = ((CourseItem) mAdapter.getItem(getFlexibleAdapterPosition())).courseDetail.is_private();
                        RxBus.getBus().post(new DelCourseEvent(isPrivate, getFlexibleAdapterPosition()));
                    }
                }
            });
        }
    }
}
