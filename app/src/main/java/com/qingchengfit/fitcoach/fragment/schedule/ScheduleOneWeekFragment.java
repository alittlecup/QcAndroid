package com.qingchengfit.fitcoach.fragment.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.utils.DateUtils;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static ScheduleOneWeekFragment newInstance(int pos,CoachService coachService) {
        Bundle args = new Bundle();
        args.putInt("pos",pos);
        args.putParcelable("service",coachService);
        ScheduleOneWeekFragment fragment = new ScheduleOneWeekFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CoachService mCoachService;


    @BindView(R.id.weekView) WeekView weekView;
    private Calendar startTime;
    private Calendar endTime;
    private WeekViewEvent event;
    List<WeekViewEvent> mEvents = new ArrayList<WeekViewEvent>();

    QcSchedulesResponse mQcSchedulesResponse;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mCoachService = getArguments().getParcelable("service");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_week, container, false);
        unbinder = ButterKnife.bind(this, view);
        HashMap<String,String> params = new HashMap<>();
        Pair<String,String> dates = DateUtils.getWeek(getArguments().getInt("pos"));
        params.put("start",dates.first);
        params.put("end",dates.second);

        RxRegiste(QcCloudClient.getApi().getApi.qcGetCoachScheduleV1(App.coachid, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcSchedulesResponse>() {
                @Override public void call(QcSchedulesResponse qcSchedulesResponse) {
                    if (ResponseConstant.checkSuccess(qcSchedulesResponse)){
                        mQcSchedulesResponse = qcSchedulesResponse;
                        inflateData();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override public String interpretDate(Calendar calendar) {
                String ret = getResources().getStringArray(R.array.week_simple_sunday_first)[calendar.get(Calendar.DAY_OF_WEEK)-1];
                return ret;
            }

            @Override public String interpretTime(int i) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, 0);
                SimpleDateFormat dateFormat =new SimpleDateFormat("HH:mm", Locale.getDefault());
                return dateFormat.format(calendar.getTime());
            }
        });
        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return mEvents;
            }
        });
        weekView.setPostion(getArguments().getInt("pos"));
        return view;
    }

    void inflateData(){
        mEvents.clear();
        for (int i = 0; i < mQcSchedulesResponse.data.services.size(); i++) {
            QcSchedulesResponse.Service scheduleService = mQcSchedulesResponse.data.services.get(i);
            if (mCoachService == null || mCoachService.id == scheduleService.system.id){
                for (int j = 0; j < scheduleService.rests.size(); j++) {
                    QcSchedulesResponse.Rest rest = scheduleService.rests.get(j);
                    startTime = Calendar.getInstance();
                    startTime.setTime(DateUtils.formatDateFromServer(rest.start));
                    endTime =Calendar.getInstance();
                    endTime.setTime(DateUtils.formatDateFromServer(rest.end));
                    event = new WeekViewEvent(startTime.getTime().getTime(),"休息",startTime,endTime);
                    mEvents.add(event);
                }
                for (int k = 0; k < scheduleService.schedules.size(); k++) {
                    QcScheduleBean schedule = scheduleService.schedules.get(k);
                    startTime = Calendar.getInstance();
                    startTime.setTime(DateUtils.formatDateFromServer(schedule.start));
                    endTime =Calendar.getInstance();
                    endTime.setTime(DateUtils.formatDateFromServer(schedule.end));
                    event = new WeekViewEvent(startTime.getTime().getTime(),schedule.course.name,startTime,endTime);
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
