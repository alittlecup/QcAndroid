package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ScheduleCompare;
import com.qingchengfit.fitcoach.activity.NotificationActivity;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.DateSegmentLayout;
import com.qingchengfit.fitcoach.component.LoopView;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.ScheduleBean;
import com.wangjie.shadowviewhelper.ShadowProperty;
import com.wangjie.shadowviewhelper.ShadowViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduesFragment extends MainBaseFragment {
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
    @Bind(R.id.web_floatbtn)
    FloatingActionsMenu webFloatbtn;
    @Bind(R.id.spinner_nav)
    Spinner spinnerNav;
    @Bind(R.id.schedule_expend_view)
    LinearLayout scheduleExpendView;
    private FloatingActionButton btn1;
    private FloatingActionButton btn2;
    private FloatingActionButton btn3;
    private ArrayList<ScheduleBean> scheduleBeans;
    private ScheduesAdapter scheduesAdapter;
    private ArrayList<SpinnerBean> mSpinnerDatas;
    private ArrayAdapter<SpinnerBean> spinnerBeanArrayAdapter;
    private int curentGym = 0;
    private QcSchedulesResponse mQcSchedulesResponse;
    /**
     * 处理网络返回
     */
    Observer<QcSchedulesResponse> mHttpCallBack = new Observer<QcSchedulesResponse>() {

        @Override
        public void onCompleted() {
//            openDrawerInterface.hideLoading();//run On ui
        }

        @Override
        public void onError(Throwable e) {
            openDrawerInterface.hideLoading();
        }

        @Override
        public void onNext(QcSchedulesResponse qcSchedulesResponse) {
            mQcSchedulesResponse = qcSchedulesResponse;
            handleResponse(qcSchedulesResponse);
        }
    };
    private Coach coach;


    public ScheduesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedues, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.inflateMenu(R.menu.menu_alert);
        toolbar.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
            return true;
        });
        drawerRadiogroup.setDate(new Date());
        Gson gson = new Gson();
        String id = PreferenceUtils.getPrefString(getActivity(), "coach", "");
        if (TextUtils.isEmpty(id)) {

        }
        coach = gson.fromJson(id, Coach.class);
        HashMap<String, String> params = new HashMap<>();
        params.put("date", DateUtils.getServerDateDay(new Date()));


        mSpinnerDatas = new ArrayList<>();
        mSpinnerDatas.add(new SpinnerBean("", "全部日程", true));
        spinnerBeanArrayAdapter = new ArrayAdapter<SpinnerBean>(getContext(), R.layout.spinner_checkview, mSpinnerDatas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_checkview, parent, false);
                }
                ((TextView) convertView).setText(mSpinnerDatas.get(position).text);
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
                }
                SpinnerBean bean = getItem(position);
                ((TextView) convertView.findViewById(R.id.spinner_tv)).setText(bean.text);
                if (bean.isTitle) {
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.GONE);
                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.VISIBLE);
                } else {
                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.GONE);
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.VISIBLE);
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setImageDrawable(new LoopView(bean.color));
                }
                return convertView;
            }
        };

//        设置下拉列表的风格
        spinnerBeanArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerNav.setAdapter(spinnerBeanArrayAdapter);
        spinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curentGym = mSpinnerDatas.get(position).id;
                handleResponse(mQcSchedulesResponse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scheduleBeans = new ArrayList<>();
        scheduesAdapter = new ScheduesAdapter(scheduleBeans);
        scheduesAdapter.setListener((v, pos) -> {
            String url = scheduesAdapter.datas.get(pos).intent_url;
            if (!TextUtils.isEmpty(url)) {
                openDrawerInterface.goWeb(url);
            }
        });
        scheduleRv.setLayoutManager(new LinearLayoutManager(getContext()));

        scheduleRv.setAdapter(scheduesAdapter);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView materialCalendarView, CalendarDay calendarDay, boolean b) {
                calendarView.setVisibility(View.GONE);
                drawerRadiogroup.setDate(calendarDay.getDate());
            }
        });
        drawerRadiogroup.setOnDateChangeListener(this::goDateSchedule);
        ShadowProperty shadowProperty = new ShadowProperty()
                .setShadowColor(0x77000000)
//                        .setShadowDy(MeasureUtils.dpToPx(0.5f, getResources()))
                .setShadowRadius(MeasureUtils.dpToPx(3f, getResources()));
        ShadowViewHelper.bindShadowHelper(shadowProperty
                , calendarView
        );

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) calendarView.getLayoutParams();
        lp.leftMargin = -shadowProperty.getShadowOffset();
        lp.rightMargin = -shadowProperty.getShadowOffset();
        lp.topMargin = -shadowProperty.getShadowOffset();
        calendarView.setLayoutParams(lp);
//        btn1 = new FloatingActionButton(getActivity());
//        btn1.setIcon(R.drawable.ic_baseinfo_city);
//        btn1.setColorNormal(Color.GREEN);
//        btn1.setTitle("设置休息");
//
//        btn2 = new FloatingActionButton(getActivity());
//
//        btn2.setColorNormal(Color.BLUE);
//        btn2.setTitle("代约团课");
//
//        btn3 = new FloatingActionButton(getActivity());
//        btn3.setIcon(R.drawable.ic_baseinfo_phone);
//        btn3.setColorNormal(Color.CYAN);
//        btn3.setTitle("代约私教");
//
//        btn1.setOnClickListener(v -> openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/systems/?action=rest"));
//        btn2.setOnClickListener(v -> openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/systems/?action=grouplesson"));
//        btn3.setOnClickListener(v -> openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/systems/?action=privatelesson"));
//
//        webFloatbtn.addButton(btn1);
//        webFloatbtn.addButton(btn2);
//        webFloatbtn.addButton(btn3);
        webFloatbtn.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                scheduleExpendView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                scheduleExpendView.setVisibility(View.GONE);
            }
        });
//        openDrawerInterface.showLoading();
        QcCloudClient.getApi().getApi.qcGetCoachSchedule(Integer.parseInt(coach.id), params).subscribeOn(Schedulers.newThread()).subscribe(mHttpCallBack);
        return view;
    }

    @OnClick({R.id.schedule_rest_btn, R.id.schedule_group_btn, R.id.schedule_private_btn})
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.schedule_rest_btn:
                openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/systems/?action=rest");
                break;
            case R.id.schedule_private_btn:
                openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/systems/?action=privatelesson");
                break;
            case R.id.schedule_group_btn:
                openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/systems/?action=grouplesson");
                break;
        }
    }

    /**
     * 获取某日日程
     *
     * @param date
     */

    private void goDateSchedule(Date date) {
        HashMap<String, String> params = new HashMap<>();
        params.put("date", DateUtils.getServerDateDay(date));
//        openDrawerInterface.showLoading();
        QcCloudClient.getApi().getApi.qcGetCoachSchedule(Integer.parseInt(coach.id), params).subscribeOn(Schedulers.newThread()).subscribe(mHttpCallBack);
    }

    private void handleResponse(QcSchedulesResponse qcSchedulesResponse) {
        if (qcSchedulesResponse == null)
            return;
        List<QcSchedulesResponse.System> systems = qcSchedulesResponse.data.systems;
        scheduleBeans.clear();
        mSpinnerDatas.clear();
        mSpinnerDatas.add(new SpinnerBean("", "全部日程", true));
        for (int i = 0; i < systems.size(); i++) {
            QcSchedulesResponse.System system = systems.get(i);

            List<QcSchedulesResponse.Rest> rests = system.rests;
            List<QcScheduleBean> schedules = system.schedules;
            String syscolor = system.system.color;
            SpinnerBean spinnerbean = new SpinnerBean(syscolor, system.system.name, system.system.id);
            mSpinnerDatas.add(spinnerbean);

            if (curentGym != 0 && curentGym != system.system.id)
                continue;
            for (int j = 0; j < rests.size(); j++) {
                QcSchedulesResponse.Rest rest = rests.get(j);
                ScheduleBean bean = new ScheduleBean();
                bean.type = 0;
                bean.color = syscolor;
                bean.time = DateUtils.formatDateFromServer(rest.start).getTime();
                bean.timeEnd = DateUtils.formatDateFromServer(rest.end).getTime();
                bean.gymname = system.system.name;
                bean.intent_url = rest.url;
                scheduleBeans.add(bean);

            }
            for (int k = 0; k < schedules.size(); k++) {
                QcScheduleBean schedule = schedules.get(k);
                ScheduleBean bean = new ScheduleBean();
                bean.type = 1;
                bean.gymname = system.system.cname;
                bean.color = syscolor;
                bean.time = DateUtils.formatDateFromServer(schedule.start).getTime();
                bean.timeEnd = DateUtils.formatDateFromServer(schedule.end).getTime();
                bean.count = schedule.count;
                bean.pic_url = schedule.course.photo;
                bean.title = schedule.course.name;
                bean.intent_url = schedule.url;
                scheduleBeans.add(bean);
            }

        }
        Collections.sort(scheduleBeans, new ScheduleCompare());
        getActivity().runOnUiThread(() -> {
            scheduesAdapter.notifyDataSetChanged();
            spinnerBeanArrayAdapter.notifyDataSetChanged();
            if (scheduleBeans.size() > 0) {
                scheduleNoImg.setVisibility(View.GONE);
                scheduleNoTv.setVisibility(View.GONE);
                scheduleRv.setVisibility(View.VISIBLE);

            } else {
                scheduleRv.setVisibility(View.GONE);
                scheduleNoImg.setVisibility(View.VISIBLE);
                scheduleNoTv.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.schedule_calendar)
    public void onCalendarClick() {
        if (calendarView.getVisibility() == View.VISIBLE) {
            calendarView.setVisibility(View.GONE);
        } else {
            calendarView.setVisibility(View.VISIBLE);
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
            holder.itemView.setTag(position);
            ScheduleBean bean = datas.get(position);
            if (bean.type == 0) {
                holder.itemScheduleTime.setText(DateUtils.getTimeHHMM(new Date(bean.time)));
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(new Date(bean.time)));
                sb.append("-");
                sb.append(DateUtils.getTimeHHMM(new Date(bean.timeEnd)));
                sb.append(" 休息");
                holder.itemScheduleClassname.setText(sb.toString());
                holder.itemScheduleGymname.setText(bean.gymname);
                holder.itemScheduleNum.setVisibility(View.GONE);
                holder.itemScheduleClasspic.setVisibility(View.GONE);

            } else if (bean.type == 1) {
                holder.itemScheduleTime.setText(DateUtils.getTimeHHMM(new Date(bean.time)));
                holder.itemScheduleClassname.setText(bean.title);
                holder.itemScheduleGymname.setText(bean.gymname);
                Glide.with(App.AppContex).load(bean.pic_url).into(holder.itemScheduleClasspic);
                holder.itemScheduleNum.setVisibility(View.VISIBLE);
                holder.itemScheduleClasspic.setVisibility(View.VISIBLE);

                holder.itemScheduleNum.setText(bean.count + "人已预约");
            }

            if (bean.timeEnd < new Date().getTime()) {
                holder.itemScheduleClassname.setTextColor(getContext().getResources().getColor(R.color.text_grey));
                holder.itemScheduleTime.setTextColor(getContext().getResources().getColor(R.color.text_grey));
                holder.itemScheduleStatus.setImageResource(R.drawable.ic_schedule_hook);
            } else {
                holder.itemScheduleClassname.setTextColor(getContext().getResources().getColor(R.color.text_black));
                holder.itemScheduleTime.setTextColor(getContext().getResources().getColor(R.color.text_black));
                holder.itemScheduleStatus.setImageDrawable(new LoopView(bean.color));
            }

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
