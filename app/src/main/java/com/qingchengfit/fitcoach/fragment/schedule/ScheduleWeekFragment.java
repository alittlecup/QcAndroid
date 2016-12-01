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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.DateUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.marcohc.robotocalendar.EventMonthChange;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.component.DatePicker;
import com.qingchengfit.fitcoach.event.EventScheduleService;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import java.util.Calendar;
import java.util.Date;
import rx.android.schedulers.AndroidSchedulers;
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
    private CoachService mCoachService;
    private DatePicker dataPicker;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_week, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getParentFragment() instanceof MainScheduleFragment){
            mCoachService = ((MainScheduleFragment) getParentFragment()).getCoachService();
        }

        viewpager.setAdapter(new ScheduleWeekAdapter(getChildFragmentManager()));
        viewpager.setCurrentItem(1000);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                Date d = DateUtils.formatDateFromYYYYMMDD(DateUtils.getWeek(position-1000).first);
                tvMonth.setText(DateUtils.getChineseMonth(d));
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });

        FloatingActionButton btn1 = new FloatingActionButton(getActivity());
        btn1.setIcon(R.drawable.ic_action_rest);
        btn1.setColorNormal(ContextCompat.getColor(getContext(), R.color.rest_color));
        btn1.setTitle("设置休息");
        //
        FloatingActionButton btn2 = new FloatingActionButton(getActivity());
        btn2.setIcon(R.drawable.ic_action_group);
        btn2.setColorNormal(ContextCompat.getColor(getContext(), R.color.group_color));
        btn2.setTitle("代约团课");

        FloatingActionButton btn3 = new FloatingActionButton(getActivity());
        btn3.setIcon(R.drawable.ic_action_private);
        btn3.setColorNormal(ContextCompat.getColor(getContext(), R.color.private_color));
        btn3.setTitle("代约私教");
        btn1.setOnClickListener(v1 -> onAction(1,new Date()));
        btn2.setOnClickListener(v1 -> onAction(3,new Date()));
        btn3.setOnClickListener(v1 -> onAction(2,new Date()));
        webFloatbtn.addButton(btn1);
        webFloatbtn.addButton(btn2);
        webFloatbtn.addButton(btn3);
        webFloatbtn.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override public void onMenuExpanded() {
                bgShow.setVisibility(View.VISIBLE);
                //ViewCompat.setAlpha(bgShow,0);
                //ViewCompat.animate(bgShow).alpha(255).setDuration(R.integer.anim_time).start();
            }

            @Override public void onMenuCollapsed() {
                //ViewCompat.animate(bgShow).alpha(0).setDuration(R.integer.anim_time).start();
                bgShow.setVisibility(View.GONE);
            }
        });


        RxBusAdd(Date.class)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Date>() {
                @Override public void call(Date date) {
                    int num = DateUtils.dayNumFromToday(date);
                    int todayCount = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    viewpager.setCurrentItem((num-todayCount+1)/7);
                }
            });
        RxBusAdd(EventMonthChange.class).subscribe(new Action1<EventMonthChange>() {
            @Override public void call(EventMonthChange eventMonthChange) {
                viewpager.setCurrentItem(eventMonthChange.pos);
            }
        });
        RxBusAdd(EventScheduleService.class)
            .subscribe(new Action1<EventScheduleService>() {
                @Override public void call(EventScheduleService eventScheduleService) {
                    mCoachService = eventScheduleService.mCoachService;
                }
            });
        return view;
    }

    @OnClick(R.id.bg_show) public void onClick() {
        webFloatbtn.collapse();
    }


    public void onAction(int v, Date date) {
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
        calendar.setTime(date);
        sb.append("&").append("date=").append(DateUtils.Date2YYYYMMDD(calendar.getTime()));
        Intent toWeb = new Intent(getActivity(), WebActivity.class);
        toWeb.putExtra("url", sb.toString());
        startActivityForResult(toWeb, 404);
        //webFloatbtn.collapse();
    }


    @Override public String getFragmentName() {
        return ScheduleWeekFragment.class.getName();
    }

    @OnClick({ R.id.tv_month, R.id.day_view }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_month:
                dataPicker = new DatePicker();
                dataPicker.show(getFragmentManager(),"");
                dataPicker.setListener(new DatePicker.DatePickerChange() {
                    @Override public void onMonthChange(int year, int month) {
                        tvMonth.setText(year+"年"+month+"月");
                    }
                });
                break;
            case R.id.day_view:
                getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_fade_in,R.anim.slide_fade_out)
                    .replace(R.id.schedule_frag,new ScheduesFragment())
                    .commitAllowingStateLoss();
                break;
        }
    }

    public class ScheduleWeekAdapter extends FragmentStatePagerAdapter {

        public ScheduleWeekAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return ScheduleOneWeekFragment.newInstance(position - 1000, mCoachService);
        }

        @Override public int getCount() {
            return Integer.MAX_VALUE;
        }
    }
}
