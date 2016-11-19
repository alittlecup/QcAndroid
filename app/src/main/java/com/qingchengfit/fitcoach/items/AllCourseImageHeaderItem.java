package com.qingchengfit.fitcoach.items;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.SchedulePhotos;
import com.qingchengfit.fitcoach.event.CourseImageManageEvent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;

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




    public AllCourseImageHeaderItem(SchedulePhotos schedulePhotos) {
        this.schedulePhotos = schedulePhotos;
    }

    public SchedulePhotos schedulePhotos;
    public int photoSize = 0;
    public String tiltle;
    public String subtitle;


    @Override
    public int getLayoutRes() {
        return R.layout.item_all_course_image_header;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new CourseImageHeaderHolder(inflater.inflate(R.layout.item_all_course_image_header, parent, false), adapter);

    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RecyclerView.ViewHolder holder, int position, List payloads) {
        if (holder instanceof CourseImageHeaderHolder) {

            ((CourseImageHeaderHolder) holder).title.setText(schedulePhotos.getCourse_name() + " - " + schedulePhotos.getTeacher().name);
            String time = DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(schedulePhotos.getStart())) + "-" + DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedulePhotos.getEnd()));
            if (schedulePhotos.getShop() != null);
                ((CourseImageHeaderHolder) holder).subTitle.setText(schedulePhotos.getShop().name+"   "+time);
            if ( photoSize== 0){
                ((CourseImageHeaderHolder) holder).noData.setVisibility(View.VISIBLE);
            }else ((CourseImageHeaderHolder) holder).noData.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public static class CourseImageHeaderHolder extends FlexibleViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.sub_title)
        TextView subTitle;
        @BindView(R.id.btn_manage)
        TextView btnManage;
        @BindView(R.id.no_data)
        TextView noData;

        public CourseImageHeaderHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            btnManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.getBus().post(new CourseImageManageEvent(getAdapterPosition()));
                }
            });
        }



    }

}
