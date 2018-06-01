package com.qingchengfit.fitcoach.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.DateUtils;
import com.qingchengfit.fitcoach.R;
import cn.qingchengfit.bean.SchedulePhotos;
import com.qingchengfit.fitcoach.event.CourseImageManageEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

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
 * Created by Paper on 16/7/27.
 */
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

    @Override public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position, List payloads) {
        if (holder instanceof CourseImageHeaderHolder) {

            ((CourseImageHeaderHolder) holder).title.setText(schedulePhotos.getCourse_name() + " - " + schedulePhotos.getTeacher().name);
            String time =
                DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(schedulePhotos.getStart())) + "-" + DateUtils.getTimeHHMM(
                    DateUtils.formatDateFromServer(schedulePhotos.getEnd()));
            if (schedulePhotos.getShop() != null) ;
            ((CourseImageHeaderHolder) holder).subTitle.setText(schedulePhotos.getShop().name + "   " + time);
            if (photoSize == 0) {
                ((CourseImageHeaderHolder) holder).noData.setVisibility(View.VISIBLE);
            } else {
                ((CourseImageHeaderHolder) holder).noData.setVisibility(View.GONE);
            }
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class CourseImageHeaderHolder extends FlexibleViewHolder {
	TextView title;
	TextView subTitle;
	TextView btnManage;
	TextView noData;

        public CourseImageHeaderHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          title = (TextView) view.findViewById(R.id.title);
          subTitle = (TextView) view.findViewById(R.id.sub_title);
          btnManage = (TextView) view.findViewById(R.id.btn_manage);
          noData = (TextView) view.findViewById(R.id.no_data);

          btnManage.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    RxBus.getBus().post(new CourseImageManageEvent(getAdapterPosition()));
                }
            });
        }
    }
}
