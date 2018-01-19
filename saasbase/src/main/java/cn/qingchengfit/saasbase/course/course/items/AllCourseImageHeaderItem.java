package cn.qingchengfit.saasbase.course.course.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhotos;
import cn.qingchengfit.saasbase.events.CourseImageManageEvent;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AllCourseImageHeaderItem extends AbstractHeaderItem {

  public SchedulePhotos schedulePhotos;
  public int photoSize = 0;
  public String tiltle;
  public String subtitle;

  public AllCourseImageHeaderItem(SchedulePhotos schedulePhotos) {
    this.schedulePhotos = schedulePhotos;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_all_course_image_header;
  }

  @Override public RecyclerView.ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
    return new CourseImageHeaderHolder(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position,
    List payloads) {
    if (holder instanceof CourseImageHeaderHolder) {

      ((CourseImageHeaderHolder) holder).title.setText(
        schedulePhotos.getCourse_name() + " - " + schedulePhotos.getTeacher().username);
      String time =
        DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(schedulePhotos.getStart()))
          + "-"
          + DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedulePhotos.getEnd()));
      if (schedulePhotos.getShop() != null) ;
      ((CourseImageHeaderHolder) holder).subTitle.setText(
        schedulePhotos.getShop().name + "   " + time);
      if (photoSize == 0) {
        ((CourseImageHeaderHolder) holder).noData.setVisibility(View.VISIBLE);
      } else {
        ((CourseImageHeaderHolder) holder).noData.setVisibility(View.GONE);
      }
    }
  }

  @Override public boolean equals(Object o) {
    if (o instanceof AllCourseImageHeaderItem) {
      if (((AllCourseImageHeaderItem) o).schedulePhotos != null && schedulePhotos != null) {
        return schedulePhotos.getId().longValue() == ((AllCourseImageHeaderItem) o).schedulePhotos.getId();
      }
    }
    return false;
  }

  public static class CourseImageHeaderHolder extends FlexibleViewHolder {
    @BindView(R2.id.title) TextView title;
    @BindView(R2.id.sub_title) TextView subTitle;
    @BindView(R2.id.btn_manage) TextView btnManage;
    @BindView(R2.id.no_data) TextView noData;

    public CourseImageHeaderHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      btnManage.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          RxBus.getBus().post(new CourseImageManageEvent(getAdapterPosition()));
        }
      });
    }
  }
}
