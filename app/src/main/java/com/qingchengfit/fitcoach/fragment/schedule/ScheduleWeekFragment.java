package com.qingchengfit.fitcoach.fragment.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.marcohc.robotocalendar.EventMonthChange;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.component.DatePicker;
import com.qingchengfit.fitcoach.event.EventInit;
import com.qingchengfit.fitcoach.event.EventScheduleAction;
import com.qingchengfit.fitcoach.event.EventScheduleService;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import java.util.Calendar;
import java.util.Date;
import rx.functions.Action1;

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
 * Created by Paper on 16/11/21.
 */
public class ScheduleWeekFragment extends BaseFragment {

    @BindView(R.id.bg_show) View bgShow;
    @BindView(R.id.web_floatbtn) FloatingActionsMenu webFloatbtn;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tv_month) TextView tvMonth;
    @BindView(R.id.btn_back_today) FrameLayout btnBackToday;
    private CoachService mCoachService;
    private DatePicker dataPicker;
    private ScheduleWeekAdapter adapter;
    private FloatingActionButton btn3;
    private FloatingActionButton btn2;
    private FloatingActionButton btn1;
    private Date mClickDate;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_week, container, false);
        unbinder = ButterKnife.bind(this, view);
        PreferenceUtils.setPrefBoolean(getContext(), "is_week_view", true);
        if (getParentFragment() instanceof MainScheduleFragment) {
            mCoachService = ((MainScheduleFragment) getParentFragment()).getCoachService();
        }
        adapter = new ScheduleWeekAdapter(getChildFragmentManager());
        tvMonth.setText(DateUtils.getChineseMonth(new Date()));
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(250 + adapter.getPostion());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                Date d = DateUtils.formatDateFromYYYYMMDD(DateUtils.getWeek(position - 250 + adapter.getPostion()).first);
                tvMonth.setText(DateUtils.getChineseMonth(d));
            }

            @Override public void onPageScrollStateChanged(int state) {

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
        btn1.setOnClickListener(v1 -> onAction(1, new Date()));
        btn2.setOnClickListener(v1 -> onAction(3, new Date()));
        btn3.setOnClickListener(v1 -> onAction(2, new Date()));
        webFloatbtn.addButton(btn1);
        webFloatbtn.addButton(btn2);
        webFloatbtn.addButton(btn3);
        webFloatbtn.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override public void onMenuExpanded() {
                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn3.setEnabled(true);
                RxBus.getBus().post(new EventInit(false,2));
                //获取当前fragment
                Fragment f = (Fragment) adapter.instantiateItem(viewpager,viewpager.getCurrentItem());
                if (f instanceof ScheduleOneWeekFragment){
                    ((ScheduleOneWeekFragment) f).setPause(true);
                }
                bgShow.setVisibility(View.VISIBLE);

                mClickDate = null;
            }

            @Override public void onMenuCollapsed() {
                bgShow.setVisibility(View.GONE);
                Fragment f = (Fragment) adapter.instantiateItem(viewpager,viewpager.getCurrentItem());
                if (f instanceof ScheduleOneWeekFragment){
                    ((ScheduleOneWeekFragment) f).setPause(false);
                }
                btnBackToday.setVisibility(View.VISIBLE);
                btnBackToday.postInvalidateDelayed(500);
            }
        });

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
                sb.append("?").append("date=").append(DateUtils.Date2YYYYMMDD(mClickDate == null ? new Date() : mClickDate));
                if (eventScheduleAction.mCoachService != null) {
                    if (!eventScheduleAction.mCoachService.has_permission) {
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
        },throwable -> {});

        RxBusAdd(EventMonthChange.class).subscribe(new Action1<EventMonthChange>() {
            @Override public void call(EventMonthChange eventMonthChange) {
                viewpager.setCurrentItem(eventMonthChange.pos);
            }
        });
        RxBusAdd(EventScheduleService.class).subscribe(new Action1<EventScheduleService>() {
            @Override public void call(EventScheduleService eventScheduleService) {
                mCoachService = eventScheduleService.mCoachService;
            }
        });
        return view;
    }

    @OnClick(R.id.bg_show) public void onClick() {
        webFloatbtn.collapse();
    }

    @OnClick(R.id.btn_back_today) public void backTody() {
        viewpager.setCurrentItem(250, true);
    }

    public void onAction(int v, Date date) {
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        webFloatbtn.collapse();
        mClickDate = date;
        if (getParentFragment() instanceof MainScheduleFragment) {
            //new ChooseGymForPermissionFragmentBuilder(v, ((MainScheduleFragment) getParentFragment()).getCoachService()).build()
            //    .show(getFragmentManager(), "");
            CoachService c = ((MainScheduleFragment) getParentFragment()).getCoachService();
            if (c != null){
                RxBus.getBus().post(new EventScheduleAction(c, v));
            }else
                new ChooseGymForPermissionFragmentBuilder(v, null).build()
                    .show(getFragmentManager(), "");
        }
    }

    @Override public String getFragmentName() {
        return ScheduleWeekFragment.class.getName();
    }

    @OnClick({ R.id.tv_month, R.id.day_view }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_month:
                dataPicker = new DatePicker();
                dataPicker.show(getFragmentManager(), "");
                if (getActivity() instanceof Main2Activity) {
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.DAY_OF_WEEK, 1);
                    today.add(Calendar.WEEK_OF_YEAR, viewpager.getCurrentItem() - 250);
                    ((Main2Activity) getActivity()).setChooseDate(today.getTime());
                }

                dataPicker.setListener(new DatePicker.DatePickerChange() {
                    @Override public void onMonthChange(int year, int month) {
                        tvMonth.setText(year + "年" + month + "月");
                    }

                    @Override public void onDismiss(int year, int month) {
                        if (getActivity() instanceof Main2Activity) {

                            Calendar today = Calendar.getInstance();
                            today.set(Calendar.DAY_OF_WEEK, 1);
                            today.set(Calendar.HOUR, 0);
                            today.set(Calendar.MINUTE, 0);
                            today.set(Calendar.SECOND, 0);
                            today.set(Calendar.MILLISECOND, 0);
                            Date d = ((Main2Activity) getActivity()).getChooseDate();
                            Calendar chooseday = Calendar.getInstance();
                            chooseday.setTime(d);
                            chooseday.set(Calendar.DAY_OF_WEEK, 1);
                            chooseday.set(Calendar.HOUR, 0);
                            chooseday.set(Calendar.MINUTE, 0);
                            chooseday.set(Calendar.SECOND, 0);
                            chooseday.set(Calendar.MILLISECOND, 0);
                            chooseday.setTime(d);
                            adapter.setPostion(DateUtils.interval(today.getTime(), chooseday.getTime()) / 7);
                            viewpager.setCurrentItem(250);
                            adapter.notifyDataSetChanged();
                            tvMonth.setText(DateUtils.getChineseMonth(chooseday.getTime()));
                        }
                    }
                });
                break;
            case R.id.day_view:
                getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_fade_in, R.anim.slide_fade_out)
                    .replace(R.id.schedule_frag, new ScheduesFragmentBuilder(new Date().getTime()).build())
                    .commitAllowingStateLoss();
                break;
        }
    }

    public class ScheduleWeekAdapter extends FragmentStatePagerAdapter {

        private int p = 0;

        public int getPostion() {
            return p;
        }

        public void setPostion(int postion) {
            this.p = postion;
        }

        public ScheduleWeekAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return ScheduleOneWeekFragment.newInstance(position - 250 + p, mCoachService);
        }

        @Override public int getCount() {
            return 500;
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
