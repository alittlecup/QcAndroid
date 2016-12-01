package com.qingchengfit.fitcoach.fragment.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.marcohc.robotocalendar.EventMonthChange;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.bean.RxRefreshList;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.DatePicker;
import com.qingchengfit.fitcoach.component.PagerSlidingTabStrip;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.ScheduleListFragment;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystem;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.ScheduleBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ScheduesFragment extends BaseFragment {
    public static final String TAG = ScheduesFragment.class.getName();
    @BindView(R.id.schedule_tab) PagerSlidingTabStrip scheduleTab;
    @BindView(R.id.schedule_vp) ViewPager scheduleVp;
    @BindView(R.id.month_view) TextView monthView;
    @BindView(R.id.tv_month) TextView tvMonth;
    @BindView(R.id.web_floatbtn) FloatingActionsMenu webFloatbtn;
    @BindView(R.id.bg_show) View bgShow;
    //@BindView(R.id.schedule_expend_view) LinearLayout scheduleExpendView;
    private FloatingActionButton btn1;
    private FloatingActionButton btn2;
    private FloatingActionButton btn3;
    private ArrayList<ScheduleBean> scheduleBeans;
    private ArrayAdapter<SpinnerBean> spinnerBeanArrayAdapter;
    private long curSystemId = 0;
    private int curPostion = 0;

    private QcSchedulesResponse mQcSchedulesResponse;
    private Date mCurDate = new Date();
    private DatePicker mDatePicker;
    private Coach coach;
    private ArrayList<SpinnerBean> spinnerBeans;
    private List<Integer> mSystemsId = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;
    private Observable<NewPushMsg> mObservable;
    private Observable<String> mObservableReresh;
    private String curModel;
    private String mTitle;
    private Unbinder unbinder;


    public ScheduesFragment() {
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedues, container, false);
        unbinder = ButterKnife.bind(this, view);
        Gson gson = new Gson();
        String id = PreferenceUtils.getPrefString(getActivity(), "coach", "");
        if (TextUtils.isEmpty(id)) {

        }
        coach = gson.fromJson(id, Coach.class);
        //初始化title下拉

        setUpViewPager();

        RxBusAdd(EventMonthChange.class).subscribe(new Action1<EventMonthChange>() {
            @Override public void call(EventMonthChange eventMonthChange) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, eventMonthChange.pos);
                tvMonth.setText(DateUtils.getChineseMonth(calendar.getTime()));
            }
        });
        RxBusAdd(Date.class).subscribe(new Action1<Date>() {
            @Override public void call(Date date) {
                goDateSchedule(date);
            }
        });

        btn1 = new FloatingActionButton(getActivity());
        btn1.setIcon(R.drawable.ic_action_rest);
        btn1.setColorNormal(ContextCompat.getColor(getContext(), R.color.rest_color));
        btn1.setTitle("设置休息");
        //
        btn2 = new FloatingActionButton(getActivity());
        btn2.setIcon(R.drawable.ic_action_group);
        btn2.setColorNormal(ContextCompat.getColor(getContext(), R.color.group_color));
        btn2.setTitle("代约团课");

        btn3 = new FloatingActionButton(getActivity());
        btn3.setIcon(R.drawable.ic_action_private);
        btn3.setColorNormal(ContextCompat.getColor(getContext(), R.color.private_color));
        btn3.setTitle("代约私教");
        btn1.setOnClickListener(v1 -> onAction(1));
        btn2.setOnClickListener(v1 -> onAction(3));
        btn3.setOnClickListener(v1 -> onAction(2));
        webFloatbtn.addButton(btn1);
        webFloatbtn.addButton(btn2);
        webFloatbtn.addButton(btn3);
        //firstGuide.setOnTouchListener(new View.OnTouchListener() {
        //    @Override
        //    public boolean onTouch(View v, MotionEvent event) {
        //        return true;
        //    }
        //});
        //if (!PreferenceUtils.getPrefBoolean(App.AppContex, App.coachid + "first_guide", false)) {
        //    firstGuide.setVisibility(View.VISIBLE);
        //}
        //
        webFloatbtn.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override public void onMenuExpanded() {
                bgShow.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(bgShow,0);
                ViewCompat.animate(bgShow).alpha(1).setDuration(R.integer.anim_time).start();
            }

            @Override public void onMenuCollapsed() {
                ViewCompat.animate(bgShow).alpha(0).setDuration(R.integer.anim_time).start();
                bgShow.setVisibility(View.GONE);
            }
        });
        if (getParentFragment() instanceof MainScheduleFragment){
            if (((MainScheduleFragment) getParentFragment()).getCoachService() != null){
                curSystemId = ((MainScheduleFragment) getParentFragment()).getCoachService().id;
                curModel = ((MainScheduleFragment) getParentFragment()).getCoachService().model;
            }
        }

        //scheduleFloatbg.setOnClickListener(v -> {
        //    webFloatbtn.collapse();
        //});
        //mObservable = RxBus.getBus().register(NewPushMsg.class);
        //mObservable.subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<NewPushMsg>() {
        //    @Override public void onCompleted() {
        //
        //    }
        //
        //    @Override public void onError(Throwable e) {
        //
        //    }
        //
        //    @Override public void onNext(NewPushMsg newPushMsg) {
        //        //queryNotify();
        //    }
        //});

        mObservableReresh = RxBus.getBus().register(RxBus.BUS_REFRESH);
        mObservableReresh.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(String s) {
                mFragmentAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void setUpViewPager() {
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        scheduleVp.setAdapter(mFragmentAdapter);
        scheduleVp.setOffscreenPageLimit(1);
        scheduleVp.setCurrentItem(30, false);
        scheduleTab.setViewPager(scheduleVp);
    }

    public void setUpNaviSpinner() {

        spinnerBeans = new ArrayList<>();
        spinnerBeans.clear();
        spinnerBeans.add(new SpinnerBean("", "全部日程", true));

        String systemStr = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "systems", "");
        if (!TextUtils.isEmpty(systemStr)) {
            QcCoachSystemResponse qcCoachSystemResponse = new Gson().fromJson(systemStr, QcCoachSystemResponse.class);
            List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
            mSystemsId.clear();
            spinnerBeans.clear();
            spinnerBeans.add(new SpinnerBean("", "全部日程", true));
            for (int i = 0; i < systems.size(); i++) {
                QcCoachSystem system = systems.get(i);
                spinnerBeans.add(new SpinnerBean(system.color, system.name, system.id, ""));
                mSystemsId.add(system.id);
            }
        } else {

        }
        //获取用户拥有的系统
        //QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid)
        //    .subscribeOn(Schedulers.io())
        //    .observeOn(AndroidSchedulers.mainThread())
        //    .subscribe(new Subscriber<QcCoachSystemResponse>() {
        //        @Override public void onCompleted() {
        //
        //        }
        //
        //        @Override public void onError(Throwable e) {
        //
        //        }
        //
        //        @Override public void onNext(QcCoachSystemResponse qcCoachSystemResponse) {
        //            if (qcCoachSystemResponse.status == ResponseResult.SUCCESS) {
        //                if (qcCoachSystemResponse.date == null || qcCoachSystemResponse.date.systems == null ||
        //                    qcCoachSystemResponse.date.systems.size() == 0) {
        //                } else {
        //                    List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
        //                    mSystemsId.clear();
        //                    spinnerBeans.clear();
        //                    spinnerBeans.add(new SpinnerBean("", "全部日程", true));
        //                    for (int i = 0; i < systems.size(); i++) {
        //                        QcCoachSystem system = systems.get(i);
        //                        spinnerBeans.add(new SpinnerBean(system.color, system.name, system.id, ""));
        //                        mSystemsId.add(system.id);
        //                        if (spinnerBeanArrayAdapter != null) {
        //                            spinnerBeanArrayAdapter.notifyDataSetChanged();
        //                        }
        //                    }
        //
        //                    PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems", new Gson().toJson(qcCoachSystemResponse));
        //                }
        //            } else if (qcCoachSystemResponse.error_code.equalsIgnoreCase(ResponseResult.error_no_login)) {
        //
        //            }
        //        }
        //    });

        //spinnerBeanArrayAdapter = new ArrayAdapter<SpinnerBean>(getContext(), R.layout.spinner_checkview, spinnerBeans) {
        //    @Override public View getView(int position, View convertView, ViewGroup parent) {
        //        if (convertView == null) {
        //            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_checkview, parent, false);
        //        }
        //        ((TextView) convertView).setText(spinnerBeans.get(position).text);
        //        return convertView;
        //    }
        //
        //    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //        if (convertView == null) {
        //            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        //        }
        //        SpinnerBean bean = getItem(position);
        //        ((TextView) convertView.findViewById(R.id.spinner_tv)).setText(bean.text);
        //        if (bean.isTitle) {
        //            ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.GONE);
        //            ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.VISIBLE);
        //        } else {
        //            ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.GONE);
        //            ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.VISIBLE);
        //            ((ImageView) convertView.findViewById(R.id.spinner_icon)).setImageDrawable(new LoopView(bean.color));
        //        }
        //        return convertView;
        //    }
        //};
        //spinnerBeanArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
    }

    public void onAction(int v) {
        StringBuffer sb = new StringBuffer(Configs.Server);
        switch (v) {
            case 1:
                sb.append("mobile/coaches/" + App.coachid + "/systems/?action=rest");
                break;
            case 2:
                sb.append("mobile/coaches/" + App.coachid + "/systems/?action=privatelesson");
                break;
            case 3:
                sb.append("mobile/coaches/" + App.coachid + "/systems/?action=grouplesson");
                break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mFragmentAdapter.getCurDay());
        calendar.add(Calendar.DAY_OF_MONTH, scheduleVp.getCurrentItem() - 30);
        sb.append("&").append("date=").append(DateUtils.Date2YYYYMMDD(calendar.getTime()));
        Intent toWeb = new Intent(getActivity(), WebActivity.class);
        toWeb.putExtra("url", sb.toString());
        startActivityForResult(toWeb, 404);
        //webFloatbtn.collapse();
    }

    @Override public void onStart() {
        super.onStart();

    }



    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0 && requestCode == 404) {
            mFragmentAdapter.notifyDataSetChanged();
        } else if (resultCode > 0 && requestCode == 501) {
            curModel = data.getStringExtra("model");
            curSystemId = Integer.parseInt(data.getStringExtra("id"));
            mFragmentAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取某日日程
     */
    private void goDateSchedule(Date date) {
        mFragmentAdapter.setCurCenterDay(date);
        mFragmentAdapter.notifyDataSetChanged();
        scheduleTab.notifyDataSetChanged();
        scheduleVp.setCurrentItem(30, false);
    }

    @OnClick({ R.id.tv_month, R.id.icon_down }) public void onCalendarClick() {
        //scheduleCalendar.setClickable(false);
        mDatePicker = new DatePicker();
        mDatePicker.show(getFragmentManager(), "");
        mDatePicker.setListener(new DatePicker.DatePickerChange() {
            @Override public void onMonthChange(int year, int month) {
                tvMonth.setText(year+"年"+month+"月");
            }
        });
        //updateCalendar();
    }

    public void updateCalendar() {

    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(NewPushMsg.class.getName(), mObservable);
        RxBus.getBus().unregister(RxRefreshList.class.getName(), mObservableReresh);
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.month_view) public void onMonth() {
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_fade_in, R.anim.slide_fade_out)
            .replace(R.id.schedule_frag, new ScheduleWeekFragment())
            .commitAllowingStateLoss();
    }

    /***
     * 单个日程
     */

    public static class SchedulesVH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_schedule_time) TextView itemScheduleTime;
        @BindView(R.id.item_schedule_classname) TextView itemScheduleClassname;
        @BindView(R.id.item_schedule_gymname) TextView itemScheduleGymname;
        @BindView(R.id.item_schedule_num) TextView itemScheduleNum;
        @BindView(R.id.item_schedule_classpic) ImageView itemScheduleClasspic;
        @BindView(R.id.item_schedule_status) ImageView itemScheduleStatus;
        @BindView(R.id.indicator) View indicator;

        public SchedulesVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {

        private String[] weekDays = new String[] {
            "日", "一", "二", "三", "四", "五", "六"
        };
        private Date curDate = new Date();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setCurCenterDay(Date day) {
            curDate = day;
        }

        public Date getCurDay() {
            return curDate;
        }

        @Override public Fragment getItem(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_MONTH, position - 30);
            return ScheduleListFragment.newInstance(calendar.getTime().getTime(), curSystemId, curModel);
        }

        @Override public int getCount() {
            return 60;
        }

        @Override public CharSequence getPageTitle(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            calendar.add(Calendar.DAY_OF_MONTH, position - 30);
            StringBuffer sb = new StringBuffer();
            sb.append(weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
            sb.append("\n");
            //sb.append(calendar.get(Calendar.MONTH) + 1);
            //sb.append(".");
            sb.append(calendar.get(Calendar.DAY_OF_MONTH));
            return sb.toString();
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    //class ScheduesAdapter extends RecyclerView.Adapter<SchedulesVH> implements View.OnClickListener {
    //    private List<ScheduleBean> datas;
    //    private OnRecycleItemClickListener listener;
    //
    //    public ScheduesAdapter(List datas) {
    //        this.datas = datas;
    //    }
    //
    //    public OnRecycleItemClickListener getListener() {
    //        return listener;
    //    }
    //
    //    public void setListener(OnRecycleItemClickListener listener) {
    //        this.listener = listener;
    //    }
    //
    //    @Override public SchedulesVH onCreateViewHolder(ViewGroup parent, int viewType) {
    //
    //        SchedulesVH holder = new SchedulesVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedules, parent, false));
    //        holder.itemView.setOnClickListener(this);
    //        return holder;
    //    }
    //
    //    @Override public void onBindViewHolder(SchedulesVH holder, int position) {
    //        holder.itemView.setTag(position);
    //        ScheduleBean bean = datas.get(position);
    //        if (bean.type == 0) {
    //            holder.itemScheduleTime.setText(DateUtils.getTimeHHMM(new Date(bean.time)));
    //            StringBuffer sb = new StringBuffer();
    //            sb.append(DateUtils.getTimeHHMM(new Date(bean.time)));
    //            sb.append("-");
    //            sb.append(DateUtils.getTimeHHMM(new Date(bean.timeEnd)));
    //            sb.append(" 休息");
    //            holder.itemScheduleClassname.setText(sb.toString());
    //            holder.itemScheduleGymname.setText(bean.gymname);
    //            holder.itemScheduleNum.setVisibility(View.GONE);
    //            holder.itemScheduleClasspic.setVisibility(View.GONE);
    //            holder.itemScheduleStatus.setVisibility(View.GONE);
    //            holder.indicator.setVisibility(View.GONE);
    //        } else if (bean.type == 1) {
    //            holder.indicator.setVisibility(View.VISIBLE);
    //            holder.itemScheduleTime.setText(DateUtils.getTimeHHMM(new Date(bean.time)));
    //            holder.itemScheduleClassname.setText(bean.title);
    //            holder.itemScheduleGymname.setText(bean.gymname);
    //            Glide.with(App.AppContex).load(bean.pic_url).into(holder.itemScheduleClasspic);
    //            holder.itemScheduleNum.setVisibility(View.VISIBLE);
    //            holder.itemScheduleClasspic.setVisibility(View.VISIBLE);
    //            holder.itemScheduleNum.setText(bean.count + "人已预约");
    //        }
    //
    //        if (bean.timeEnd < new Date().getTime()) {
    //            holder.itemScheduleClassname.setTextColor(ContextCompat.getColor(getContext(), R.color.text_grey));
    //            holder.itemScheduleTime.setTextColor(ContextCompat.getColor(getContext(), R.color.text_grey));
    //            holder.itemScheduleStatus.setVisibility(View.GONE);
    //            holder.indicator.setBackgroundResource(R.color.warm_grey);
    //        } else {
    //            holder.itemScheduleStatus.setVisibility(bean.type == 1 ? View.VISIBLE : View.GONE);
    //            holder.itemScheduleClassname.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
    //            holder.itemScheduleTime.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
    //            holder.itemScheduleStatus.setImageResource(R.drawable.ic_schedule_orderd);
    //            holder.indicator.setBackgroundResource(bean.count > 0 ? R.color.primary : R.color.grey);
    //        }
    //    }
    //
    //    @Override public int getItemCount() {
    //        return datas.size();
    //    }
    //
    //    @Override public void onClick(View v) {
    //        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    //    }
    //}
}
