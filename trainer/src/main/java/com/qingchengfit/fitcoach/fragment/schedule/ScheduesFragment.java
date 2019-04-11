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
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.bean.RxRefreshList;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.marcohc.robotocalendar.EventMonthChange;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.component.DatePicker;
import com.qingchengfit.fitcoach.event.EventInit;
import com.qingchengfit.fitcoach.event.EventScheduleAction;
import com.qingchengfit.fitcoach.event.EventScheduleService;
import com.qingchengfit.fitcoach.fragment.ScheduleListFragment;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@FragmentWithArgs public class ScheduesFragment extends BaseFragment {
  public static final String TAG = ScheduesFragment.class.getName();
  @Arg @Nullable Long mDateTime;
  PagerSlidingTabStrip scheduleTab;
  ViewPager scheduleVp;
  TextView monthView;
  TextView tvMonth;
  FloatingActionsMenu webFloatbtn;
  View bgShow;
  private FloatingActionButton btn1;
  private FloatingActionButton btn2;
  private FloatingActionButton btn3;
  private String curSystemId;

  private DatePicker mDatePicker;
  private FragmentAdapter mFragmentAdapter;
  private Observable<String> mObservableReresh;
  private String curModel;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  public ScheduesFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Override public String getFragmentName() {
    return ScheduesFragment.class.getName();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_schedues, container, false);
    scheduleTab = (PagerSlidingTabStrip) view.findViewById(R.id.schedule_tab);
    scheduleVp = (ViewPager) view.findViewById(R.id.schedule_vp);
    monthView = (TextView) view.findViewById(R.id.month_view);
    tvMonth = (TextView) view.findViewById(R.id.tv_month);
    webFloatbtn = (FloatingActionsMenu) view.findViewById(R.id.web_floatbtn);
    bgShow = (View) view.findViewById(R.id.bg_show);

    view.findViewById(R.id.back_today).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        backToady();
      }
    });
    view.findViewById(R.id.tv_month).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCalendarClick();
      }
    });
    view.findViewById(R.id.icon_down).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCalendarClick();
      }
    });
    view.findViewById(R.id.month_view).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMonth();
      }
    });
    view.findViewById(R.id.bg_show).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ScheduesFragment.this.onClick();
      }
    });

    PreferenceUtils.setPrefBoolean(getContext(), "is_week_view", false);

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
      new RxPermissions(getActivity()).request(Manifest.permission.READ_CALENDAR,
          Manifest.permission.WRITE_CALENDAR).subscribe(aBoolean -> {
        if (aBoolean) {
        } else {
          //ToastUtils.show("您并未授予日历权限,请到设置(或者安全软件)中开启权限");
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
        if (eventScheduleAction.from != 0) return;
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
          if (!eventScheduleAction.mCoachService.has_permission
              && eventScheduleAction.action != 1) {
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

    webFloatbtn.setOnFloatingActionsMenuUpdateListener(
        new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
          @Override public void onMenuExpanded() {
            btn1.setEnabled(true);
            btn2.setEnabled(true);
            btn3.setEnabled(true);
            RxBus.getBus().post(new EventInit(false, 2));
            bgShow.setVisibility(View.VISIBLE);
          }

          @Override public void onMenuCollapsed() {
            bgShow.setVisibility(View.GONE);
          }
        });
    if (getParentFragment() instanceof MainScheduleFragment) {
      if (((MainScheduleFragment) getParentFragment()).getCoachService() != null) {
        curSystemId = ((MainScheduleFragment) getParentFragment()).getCoachService().id;
        curModel = ((MainScheduleFragment) getParentFragment()).getCoachService().model;
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
    RxBusAdd(EventScheduleService.class).subscribe(new Action1<EventScheduleService>() {
      @Override public void call(EventScheduleService eventScheduleService) {
        if (eventScheduleService.mCoachService != null) {
          curSystemId = eventScheduleService.mCoachService.id;
          curModel = eventScheduleService.mCoachService.model;
        } else {
          curSystemId = null;
          curModel = "";
        }
      }
    });
    return view;
  }

  private void setUpViewPager() {
    mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
    mFragmentAdapter.setCurCenterDay(new Date());
    scheduleVp.setAdapter(mFragmentAdapter);
    scheduleVp.setOffscreenPageLimit(1);

    scheduleTab.setViewPager(scheduleVp);
    scheduleTab.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            CompatUtils.removeGlobalLayout(scheduleTab.getViewTreeObserver(), this);
            scheduleTab.notifyDataSetChanged();
            goDateSchedule(mDateTime == null ? new Date() : new Date(mDateTime));
          }
        });
    scheduleVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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

  @Override public void onResume() {
    super.onResume();
    if (loginStatus.isLogined() && !TextUtils.isEmpty(gymWrapper.getGymId())) {
      goDateSchedule(mDateTime == null ? new Date() : new Date(mDateTime));
    }
  }

  public void backToady() {
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
        RxBus.getBus().post(new EventScheduleAction(c, v, 0));
      } else {
        new ChooseGymForPermissionFragmentBuilder(v, null, 0).build()
            .show(getFragmentManager(), "");
      }
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode < 0 && requestCode == 404) {
      mFragmentAdapter.notifyDataSetChanged();
    } else if (resultCode > 0 && requestCode == 501) {
      curModel = data.getStringExtra("model");
      curSystemId = data.getStringExtra("id");
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
  }

  public void onCalendarClick() {
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
    RxBus.getBus().unregister(RxRefreshList.class.getName(), mObservableReresh);

    super.onDestroyView();
  }

  public void onMonth() {
    getFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_fade_in, R.anim.slide_fade_out)
        .replace(R.id.schedule_frag, new ScheduleWeekFragment())
        .commitAllowingStateLoss();
  }

  public void onClick() {
    webFloatbtn.collapse();
  }

  /***
   * 单个日程
   */

  public static class SchedulesVH extends RecyclerView.ViewHolder {
    TextView itemScheduleTime;
    TextView itemScheduleClassname;
    TextView itemScheduleGymname;
    TextView itemScheduleNum;
    ImageView itemScheduleClasspic;
    ImageView itemScheduleStatus;
    View indicator;

    public SchedulesVH(View view) {
      super(view);
      itemScheduleTime = (TextView) view.findViewById(R.id.item_schedule_time);
      itemScheduleClassname = (TextView) view.findViewById(R.id.item_schedule_classname);
      itemScheduleGymname = (TextView) view.findViewById(R.id.item_schedule_gymname);
      itemScheduleNum = (TextView) view.findViewById(R.id.item_schedule_num);
      itemScheduleClasspic = (ImageView) view.findViewById(R.id.item_schedule_classpic);
      itemScheduleStatus = (ImageView) view.findViewById(R.id.item_schedule_status);
      indicator = (View) view.findViewById(R.id.indicator);
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
