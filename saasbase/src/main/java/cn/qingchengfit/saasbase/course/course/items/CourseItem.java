package cn.qingchengfit.saasbase.course.course.items;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

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

  public CourseType courseDetail;
  private boolean isEditable;

  public CourseItem(CourseType courseDetail) {
    this.courseDetail = courseDetail;
  }

  public Course getCourse() {
    return new Course.Builder().capacity(courseDetail.capacity)
        .id(courseDetail.id)
        .is_private(courseDetail.is_private)
        .length(courseDetail.length)
        .name(courseDetail.name)
        .photo(courseDetail.photo)
        .build();
  }

  public CourseType getData(){
    return courseDetail;
  }

  public boolean isEditable() {
    return isEditable;
  }

  public void setEditable(boolean editable) {
    isEditable = editable;
  }

  @Override public boolean equals(Object o) {
    if (o instanceof CourseType) {
      return ((CourseType) o).getId().equalsIgnoreCase(courseDetail.getId());
    } else {
      return false;
    }
  }

  @Override public int getLayoutRes() {
    return R.layout.item_course_saas;
  }

  @Override public CourseVh createViewHolder(View view, FlexibleAdapter adapter) {
    return new CourseVh(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CourseVh holder, int position,
      List payloads) {
    boolean isAll = courseDetail.id.equalsIgnoreCase("0");

    holder.courseTypeImg.setVisibility(View.VISIBLE);
    holder.courseLength.setText(
        String.format(Locale.CHINA, "时长%d分钟", courseDetail.getLength() / 60));
    holder.courseName.setText(courseDetail.getName());

    holder.courseTypeImg.setImageResource(
        courseDetail.is_private() ? R.drawable.vd_course_private_conner
            : R.drawable.vd_course_group_conner);

    Glide.with(adapter.getRecyclerView().getContext())
        .load(courseDetail.getPhoto())
        .placeholder(R.drawable.img_loadingimage)
        .into(holder.courseImg);

    if (isEditable) {
      holder.del.setVisibility(View.VISIBLE);
    } else {
      holder.del.setVisibility(GONE);
    }

    if (adapter.isSelected(position)) {
      holder.imgCourseSelectRight.setImageResource(R.drawable.ic_green_right);
    } else {
      holder.imgCourseSelectRight.setImageResource(R.drawable.vd_triangle_right_grey);
    }

    holder.courseLength.setVisibility(isAll ? GONE : View.VISIBLE);
    holder.imgCourseSelectRight.setVisibility(isAll ? GONE : View.VISIBLE);
    holder.courseTypeImg.setVisibility(isAll ? GONE : View.VISIBLE);

    if (isAll) {
      holder.courseImg.setBackgroundColor(
          ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
    }
  }

  public static class CourseVh extends FlexibleViewHolder {

	ImageView courseImg;
	ImageView courseTypeImg;
	TextView courseName;
	TextView courseLength;
	ImageView del;
	ImageView imgCourseSelectRight;

    public CourseVh(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      courseImg = (ImageView) view.findViewById(R.id.course_img);
      courseTypeImg = (ImageView) view.findViewById(R.id.course_type_img);
      courseName = (TextView) view.findViewById(R.id.course_name);
      courseLength = (TextView) view.findViewById(R.id.course_length);
      del = (ImageView) view.findViewById(R.id.del);
      imgCourseSelectRight = (ImageView) view.findViewById(R.id.img_course_status_right);

      del.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (mAdapter.getItem(getFlexibleAdapterPosition()) instanceof CourseItem) {
            boolean isPrivate = ((CourseItem) mAdapter.getItem(
                getFlexibleAdapterPosition())).courseDetail.is_private();
            //RxBus.getBus().post(new DelCourseEvent(isPrivate, getFlexibleAdapterPosition())); todo 删除课程广播
          }
        }
      });
    }
  }
}
