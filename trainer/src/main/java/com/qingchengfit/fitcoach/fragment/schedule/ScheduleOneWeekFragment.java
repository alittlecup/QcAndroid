package com.qingchengfit.fitcoach.fragment.schedule;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CalenderPopWindow;
import com.qingchengfit.fitcoach.event.EventScheduleService;
import com.qingchengfit.fitcoach.event.EventScheduleView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/11/23.
 */
public class ScheduleOneWeekFragment extends BaseFragment {

    @BindView(R.id.weekView) WeekView weekView;
    List<WeekViewEvent> mEvents = new ArrayList<WeekViewEvent>();
    QcSchedulesResponse mQcSchedulesResponse;
    PopupWindow mPopupWindow;
    private boolean calledNetwork = false;
    private CalenderPopWindow mCalenderPopWindow;
    private CoachService mCoachService;
    private Calendar startTime;
    private Calendar endTime;
    private WeekViewEvent event;

    public static ScheduleOneWeekFragment newInstance(int pos, CoachService coachService) {
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putParcelable("service", coachService);
        ScheduleOneWeekFragment fragment = new ScheduleOneWeekFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoachService = getArguments().getParcelable("service");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_week, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPopupWindow = new PopupWindow(getContext());
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        if (hour > 3) {
            weekView.goToHour(hour - 3);
        } else {
            weekView.goToHour(hour);
        }
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override public String interpretDate(Calendar calendar) {
                String ret = getResources().getStringArray(R.array.week_simple_sunday_first)[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                return ret;
            }

            @Override public String interpretTime(int i) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, 0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return dateFormat.format(calendar.getTime());
            }
        });
        weekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {
                HashMap<String, Object> tags = weekViewEvent.getTag();
                //同一时段多于3个事件
                boolean isLess = tags.get("less") != null;
                if (isLess) {
                    RxBus.getBus()
                        .post(new EventScheduleView.Builder().isWeekView(false).mDate(weekViewEvent.getStartTime().getTime()).build());
                } else {
                    String url = (String) tags.get("url");
                    if (!TextUtils.isEmpty(url)) {
                        Intent it = new Intent(getActivity(), WebActivity.class);
                        it.putExtra("url", url);
                        getParentFragment().startActivityForResult(it, 404);
                    }
                }
            }
        });
        weekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {

            @Override public void onEmptyViewClicked(Calendar calendar, float v, float v1) {
                //if (mCalenderPopWindow == null) {
                mCalenderPopWindow = new CalenderPopWindow.Builder(getContext()).rest(v2 -> onAction(1, calendar.getTime()))
                    .group(v2 -> onAction(3, calendar.getTime()))
                    .privat(v2 -> onAction(2, calendar.getTime()))
                    .build();
                mCalenderPopWindow.setDismiss(new PopupWindow.OnDismissListener() {
                    @Override public void onDismiss() {
                        weekView.cancelClick();
                    }
                });
                //}
                mCalenderPopWindow.show(weekView, (int) v, (int) v1);
                weekView.clickOneRect(calendar);
                //GuideWindow window = new GuideWindow(getContext(),"xxxx",GuideWindow.DOWN);
                //window.show(weekView);
            }
        });
        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                if (!calledNetwork) {
                    refresh();
                    calledNetwork = true;
                }
                return mEvents;
            }
        });

        RxBusAdd(EventScheduleService.class)
          .onBackpressureLatest()
          .subscribe(new Action1<EventScheduleService>() {
            @Override public void call(EventScheduleService eventScheduleService) {
                if (CmStringUtils.isEmpty(eventScheduleService.mCoachService.getId())){
                    mCoachService = null;
                }else
                    mCoachService = eventScheduleService.mCoachService;
                inflateData();
            }
        });
        weekView.setPostion(getArguments().getInt("pos"));
        return view;
    }

    public void setPause(boolean pause) {
        if (weekView != null) {
            weekView.setPause(pause);
            weekView.notifyDatasetChanged();
        }
    }

    @Override protected void onVisible() {
        super.onVisible();
        if (weekView != null) {
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            if (hour > 3) {
                weekView.goToHour(hour - 3);
            } else {
                weekView.goToHour(hour);
            }
        }
    }

    public void refresh() {
        HashMap<String, String> params = new HashMap<>();
        Pair<String, String> dates = DateUtils.getWeek(getArguments().getInt("pos"));
        params.put("from_date", dates.first);
        params.put("to_date", dates.second);

        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoachScheduleV1(App.coachid, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcSchedulesResponse>() {
                @Override public void call(QcSchedulesResponse qcSchedulesResponse) {
                    if (ResponseConstant.checkSuccess(qcSchedulesResponse)) {
                        mQcSchedulesResponse = qcSchedulesResponse;
                        inflateData();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    @Override public void onResume() {
        super.onResume();
        refresh();
    }

    public void onAction(int v, Date date) {
        if (getParentFragment() instanceof ScheduleWeekFragment) {
            ((ScheduleWeekFragment) getParentFragment()).onAction(v, date);
        }
        //StringBuffer sb = new StringBuffer(Configs.Server);
        //switch (v) {
        //    case 1:
        //        sb.append("mobile/coaches/" + App.coachid + "/systems/?action=rest");
        //        break;
        //    case 2:
        //        sb.append("mobile/coaches/" + App.coachid + "/systems/?action=privatelesson");
        //        break;
        //    case 3:
        //        sb.append("mobile/coaches/" + App.coachid + "/systems/?action=grouplesson");
        //        break;
        //}
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        //sb.append("&").append("date=").append(DateUtils.Date2YYYYMMDD(calendar.getTime()));
        //Intent toWeb = new Intent(getActivity(), WebActivity.class);
        //toWeb.putExtra("url", sb.toString());
        //startActivityForResult(toWeb, 404);
        //webFloatbtn.collapse();
    }

    void inflateData() {
        mEvents.clear();
        for (int i = 0; i < mQcSchedulesResponse.data.services.size(); i++) {
            QcSchedulesResponse.Service scheduleService = mQcSchedulesResponse.data.services.get(i);
            if (mCoachService == null || mCoachService.id.equalsIgnoreCase(scheduleService.system.id)) {
                for (int j = 0; j < scheduleService.rests.size(); j++) {
                    QcSchedulesResponse.Rest rest = scheduleService.rests.get(j);
                    startTime = Calendar.getInstance();
                    startTime.setTime(DateUtils.formatDateFromServer(rest.start));
                    endTime = Calendar.getInstance();
                    endTime.setTime(DateUtils.formatDateFromServer(rest.end));
                    event = new WeekViewEvent(startTime.getTime().getTime(), "休\n息", null, startTime, endTime, false);
                    event.setColor(
                        endTime.getTime().getTime() < new Date().getTime() ? ContextCompat.getColor(getContext(), R.color.warm_grey)
                            : ContextCompat.getColor(getContext(), R.color.rest_color));
                    HashMap<String, Object> tag = new HashMap<>();
                    tag.put("url", rest.url);
                    tag.put("rest", true);
                    event.setTag(tag);
                    mEvents.add(event);
                }
                for (int k = 0; k < scheduleService.schedules.size(); k++) {
                    QcScheduleBean schedule = scheduleService.schedules.get(k);
                    startTime = Calendar.getInstance();
                    startTime.setTime(DateUtils.formatDateFromServer(schedule.start));
                    endTime = Calendar.getInstance();
                    endTime.setTime(DateUtils.formatDateFromServer(schedule.end));
                    int count = 0;

                    event = new WeekViewEvent(startTime.getTime().getTime(), schedule.course.name, schedule.count + "", startTime, endTime,
                        false);
                    event.setColor(
                        endTime.getTime().getTime() < new Date().getTime() ? ContextCompat.getColor(getContext(), R.color.warm_grey)
                            : ContextCompat.getColor(getContext(),
                                schedule.course.is_private ? R.color.private_color : R.color.group_color));
                    HashMap<String, Object> tag = new HashMap<>();
                    tag.put("url", schedule.url);
                    if (schedule.orders != null && schedule.orders.size() > 0) {
                        tag.put("name", schedule.orders.get(0).username);
                    }
                    event.setTag(tag);
                    mEvents.add(event);
                }
            }
        }
        weekView.notifyDatasetChanged();
    }

    @Override public String getFragmentName() {
        return ScheduleOneWeekFragment.class.getName();
    }
}
