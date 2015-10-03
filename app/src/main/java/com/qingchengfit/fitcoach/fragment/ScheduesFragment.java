package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.DateSegmentLayout;
import com.qingchengfit.fitcoach.component.LoopView;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.ScheduleBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduesFragment extends Fragment {
    public static final String TAG = ScheduesFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_radiogroup)
    DateSegmentLayout drawerRadiogroup;
    @Bind(R.id.schedule_calendar)
    RelativeLayout scheduleCalendar;
    @Bind(R.id.schedule_rv)
    RecyclerView scheduleRv;
    @Bind(R.id.schedule_no_img)
    ImageView scheduleNoImg;
    @Bind(R.id.schedule_no_tv)

    TextView scheduleNoTv;
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    @Bind(R.id.shadow_item_container)
    MaterialShadowContainerView shadowItemContainer;

    public ScheduesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedues, container, false);
        ButterKnife.bind(this, view);
        drawerRadiogroup.setDate(new Date());
        Gson gson = new Gson();
        String id = PreferenceUtils.getPrefString(getActivity(), "coach", "");
        if (TextUtils.isEmpty(id)) {

        }
        Coach coach = gson.fromJson(id, Coach.class);
        QcCloudClient.getApi().getApi.qcGetCoachSchedule(Integer.parseInt(coach.id)).subscribe();
        List<ScheduleBean> scheduleBeans = new ArrayList<>();
        scheduleBeans.add(new ScheduleBean());
        scheduleBeans.add(new ScheduleBean());
        scheduleBeans.add(new ScheduleBean());
        ScheduesAdapter scheduesAdapter = new ScheduesAdapter(scheduleBeans);
        scheduleRv.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleRv.setAdapter(scheduesAdapter);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView materialCalendarView, CalendarDay calendarDay, boolean b) {
                shadowItemContainer.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @OnClick(R.id.schedule_calendar_ic)
    public void onCalendarClick() {
        if (shadowItemContainer.getVisibility() == View.VISIBLE) {
            shadowItemContainer.setVisibility(View.GONE);
        } else {
            shadowItemContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static class SchedulesVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_schedule_time)
        TextView itemScheduleTime;
        @Bind(R.id.item_schedule_classname)
        TextView itemScheduleClassname;
        @Bind(R.id.item_schedule_gymname)
        TextView itemScheduleGymname;
        @Bind(R.id.item_schedule_num)
        TextView itemScheduleNum;
        @Bind(R.id.item_schedule_classpic)
        ImageView itemScheduleClasspic;
        @Bind(R.id.item_schedule_status)
        ImageView itemScheduleStatus;

        public SchedulesVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ScheduesAdapter extends RecyclerView.Adapter<SchedulesVH> implements View.OnClickListener {


        private List<ScheduleBean> datas;
        private OnRecycleItemClickListener listener;

        public ScheduesAdapter(List datas) {
            this.datas = datas;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public SchedulesVH onCreateViewHolder(ViewGroup parent, int viewType) {
            SchedulesVH holder = new SchedulesVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedules, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(SchedulesVH holder, int position) {
            ScheduleBean bean = datas.get(position);
            holder.itemScheduleStatus.setImageDrawable(new LoopView("#63d2f3"));
            holder.itemScheduleClassname.setText(bean.cname);
            holder.itemScheduleGymname.setText(bean.name);

        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }

}
