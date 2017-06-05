package com.qingchengfit.fitcoach.fragment.schedule;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.marcohc.robotocalendar.EventMonthChange;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.bean.RxRefreshList;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.DatePicker;
import com.qingchengfit.fitcoach.component.PagerSlidingTabStrip;
import com.qingchengfit.fitcoach.event.EventInit;
import com.qingchengfit.fitcoach.event.EventScheduleAction;
import com.qingchengfit.fitcoach.fragment.ScheduleListFragment;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.ScheduleBean;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@FragmentWithArgs public class ScheduesFragment extends BaseFragment {
    public static final String TAG = ScheduesFragment.class.getName();
    @Arg @Nullable Long mDateTime;
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
        FragmentArgs.inject(this);
    }

    @Override public String getFragmentName() {
        return ScheduesFragment.class.getName();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedues, container, false);
        unbinder = ButterKnife.bind(this, view);
        PreferenceUtils.setPrefBoolean(getContext(), "is_week_view", false);
        Gson gson = new Gson();
        String id = PreferenceUtils.getPrefString(getActivity(), "coach", "");
        if (TextUtils.isEmpty(id)) {

        }
        coach = gson.fromJson(id, Coach.class);
        //初始化title下拉
        tvMonth.setText(DateUtils.getChineseMonth(new Date()));
        setUpViewPager();

        RxBusAdd(EventMonthChange.class).subscribe(new Action1<EventMonthChange>() {
            @Override public void call(EventMonthChange eventMonthChange) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, eventMonthChange.pos);
                tvMonth.setText(DateUtils.getChineseMonth(calendar.getTime()));
            }
        }, throwable -> {
        });
        RxBusAdd(Date.class).subscribe(new Action1<Date>() {
            @Override public void call(Date date) {
                goDateSchedule(date);
            }
        }, throwable -> {
        });
        if (getContext() != null) {
            RxPermissions.getInstance(getContext())
                .request(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
                .subscribe(new Action1<Boolean>() {
                    @Override public void call(Boolean aBoolean) {
                        if (aBoolean) {
                        } else {
                            //ToastUtils.show("您并未授予日历权限,请到设置(或者安全软件)中开启权限");
                        }
                    }
                }, throwable -> {
                });
        }
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
        RxBusAdd(EventScheduleAction.class).subscribe(new Action1<EventScheduleAction>() {
            @Override public void call(EventScheduleAction eventScheduleAction) {
                StringBuffer sb = new StringBuffer(Configs.Server);
                switch (eventScheduleAction.action) {
                    case 1:
                        sb.append(Configs.SCHEDULE_REST);
                        break;
                    case 2:
                        sb.append(Configs.SCHEDULE_PRIVATE);
                        break;
                    case 3:
                        sb.append(Configs.SCHEDULE_GROUP);
                        break;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mFragmentAdapter.getCurDay());
                calendar.add(Calendar.DATE, scheduleVp.getCurrentItem() - mFragmentAdapter.getCurMidPos());
                sb.append("?").append("date=").append(DateUtils.Date2YYYYMMDD(calendar.getTime()));
                if (eventScheduleAction.mCoachService != null) {
                    if (!eventScheduleAction.mCoachService.has_permission && eventScheduleAction.action != 1) {
                        showAlert(R.string.alert_permission_forbid);
                        return;
                    } else {
                        sb.append("&id=")
                            .append(eventScheduleAction.mCoachService.getId())
                            .append("&model=")
                            .append(eventScheduleAction.mCoachService.getModel());
                    }
                }

                Intent toWeb = new Intent(getActivity(), WebActivity.class);
                toWeb.putExtra("url", sb.toString());
                startActivityForResult(toWeb, 404);
            }
        }, throwable -> {
        });

        webFloatbtn.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override public void onMenuExpanded() {
                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn3.setEnabled(true);
                RxBus.getBus().post(new EventInit(false, 2));
                bgShow.setVisibility(View.VISIBLE);
                //ViewCompat.setAlpha(bgShow,0);
                //ViewCompat.animate(bgShow).alpha(1).setDuration(R.integer.anim_time).start();
            }

            @Override public void onMenuCollapsed() {
                //ViewCompat.animate(bgShow).alpha(0).setDuration(R.integer.anim_time).start();
                bgShow.setVisibility(View.GONE);
            }
        });
        if (getParentFragment() instanceof MainScheduleFragment) {
            if (((MainScheduleFragment) getParentFragment()).getCoachService() != null) {
                curSystemId = ((MainScheduleFragment) getParentFragment()).getCoachService().id;
                curModel = ((MainScheduleFragment) getParentFragment()).getCoachService().model;
            } else {

            }
        }

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
        mFragmentAdapter.setCurCenterDay(new Date());
        scheduleVp.setAdapter(mFragmentAdapter);
        scheduleVp.setOffscreenPageLimit(1);

        scheduleTab.setViewPager(scheduleVp);
        scheduleTab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(scheduleTab.getViewTreeObserver(), this);
                scheduleTab.notifyDataSetChanged();
                goDateSchedule(mDateTime == null ? new Date() : new Date(mDateTime));
            }
        });
        scheduleVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                Calendar c = Calendar.getInstance();
                c.setTime(mFragmentAdapter.getCurDay());
                c.add(Calendar.DATE, position - mFragmentAdapter.getCurMidPos());
                tvMonth.setText(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月");
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.back_today) public void backToady() {
        goDateSchedule(new Date());
    }

    public void onAction(int v) {
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        webFloatbtn.collapse();
        if (getParentFragment() instanceof MainScheduleFragment) {
            CoachService c = ((MainScheduleFragment) getParentFragment()).getCoachService();
            if (c != null) {
                RxBus.getBus().post(new EventScheduleAction(c, v));
            } else {
                new ChooseGymForPermissionFragmentBuilder(v, null).build().show(getFragmentManager(), "");
            }
        }
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
        scheduleVp.setCurrentItem(mFragmentAdapter.getCurMidPos(), false);

        //scheduleTab.selectChild(mFragmentAdapter.getCurMidPos(),0);
    }

    @OnClick({ R.id.tv_month, R.id.icon_down }) public void onCalendarClick() {
        mDatePicker = new DatePicker();
        mDatePicker.show(getFragmentManager(), "");
        if (getActivity() instanceof Main2Activity) {
            Calendar c = Calendar.getInstance();
            c.setTime(mFragmentAdapter.getCurDay());
            c.add(Calendar.DATE, scheduleVp.getCurrentItem() - mFragmentAdapter.getCurMidPos());
            ((Main2Activity) getActivity()).setChooseDate(c.getTime());
        }

        mDatePicker.setListener(new DatePicker.DatePickerChange() {
            @Override public void onMonthChange(int year, int month) {
            }

            @Override public void onDismiss(int year, int month) {
                if (getActivity() instanceof Main2Activity) {
                    goDateSchedule(((Main2Activity) getActivity()).getChooseDate());
                }
            }
        });
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

    @OnClick(R.id.bg_show) public void onClick() {
        webFloatbtn.collapse();
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

        public int getCurMidPos() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            int weekNo = calendar.get(Calendar.DAY_OF_WEEK);
            return (70 + weekNo - 1);
        }

        @Override public Fragment getItem(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            int weekNo = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_MONTH, position - 70 - weekNo + 1);
            return ScheduleListFragment.newInstance(calendar.getTime().getTime(), curSystemId, curModel);
        }

        @Override public int getCount() {
            return 147;
        }

        @Override public CharSequence getPageTitle(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            int weekNo = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_MONTH, position - 70 - weekNo + 1);
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
}
