package com.qingchengfit.fitcoach.fragment.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    @BindView(R.id.weekView)
    WeekView weekView;
    private Calendar startTime;
    private Calendar endTime;
    private WeekViewEvent event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_week, container, false);
        ButterKnife.bind(this, view);

        weekView.notifyDatasetChanged();
        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

                




                startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 5);
                startTime.set(Calendar.MINUTE, 30);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 2);
                endTime.set(Calendar.MONTH, newMonth-1);
                event = new WeekViewEvent(2, "哈哈哈", startTime, endTime);
                event.setColor(getResources().getColor(R.color.green));
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 2);
                startTime.set(Calendar.MINUTE, 30);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 1);
                endTime.set(Calendar.MONTH, newMonth-1);
                event = new WeekViewEvent(3, "测试", startTime, endTime);
                event.setColor(getResources().getColor(R.color.green));
                events.add(event);


                return events;
            }
        });
        return view;
    }

    @Override
    public String getFragmentName() {
        return ScheduleWeekFragment.class.getName();
    }
}
