package com.marcohc.robotocalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.component.DatePicker;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
 * Created by Paper on 16/11/25.
 */
public class CalendarFragment extends BaseFragment {

	RobotoCalendarView calendarView;

    public static CalendarFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("p", pos);
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = (RobotoCalendarView) view.findViewById(R.id.calendar);

        Calendar mCurCalendar = Calendar.getInstance(Locale.getDefault());
        Calendar today = Calendar.getInstance(Locale.getDefault());
        mCurCalendar.add(Calendar.MONTH, getArguments().getInt("p"));
        calendarView.initializeCalendar(mCurCalendar);
        if (mCurCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && mCurCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            calendarView.markDayAsSelectedDay(new Date());
        }
        calendarView.setRobotoCalendarListener(new RobotoCalendarView.RobotoCalendarListener() {
            @Override public void onDateSelected(Date date) {
                //RxBus.getBus().post(date);
                if (getActivity() instanceof Main2Activity) {
                    ((Main2Activity) getActivity()).setChooseDate(date);
                }
                if (getParentFragment() instanceof DatePicker) {
                    ((DatePicker) getParentFragment()).dismiss();
                }
            }

            @Override public void onRightButtonClick() {

            }

            @Override public void onLeftButtonClick() {

            }
        });
        HashMap<String, String> params = new HashMap<>();
        params.put("from_date", DateUtils.getStartDayOfMonth(mCurCalendar.getTime()));
        params.put("to_date", DateUtils.getEndDayOfMonth(mCurCalendar.getTime()));
        TrainerRepository.getStaticTrainerAllApi().qcGetScheduleGlance(App.coachid, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(qcScheduleGlanceResponse -> {
                getActivity().runOnUiThread(() -> {
                    for (String day : qcScheduleGlanceResponse.data.dates) {
                        markDay(day);
                    }
                    if (mCurCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && mCurCalendar.get(Calendar.YEAR) == today.get(
                        Calendar.YEAR)) {
                        calendarView.markDayAsSelectedDay(new Date());
                    }
                });
            }, throwable -> {
            }, () -> {
            });
        return view;
    }

    public void markDay(String day) {
        Date makeD = DateUtils.formatDateFromYYYYMMDD(day);
        if ((makeD.getTime() + 3600000) < DateUtils.getToadayMidnight()) {
            calendarView.markSecondUnderlineWithStyle(RobotoCalendarView.GREY_COLOR, makeD);
        } else {
            calendarView.markSecondUnderlineWithStyle(RobotoCalendarView.RED_COLOR, makeD);
        }
    }

    @Override public String getFragmentName() {
        return CalendarFragment.class.getName();
    }
}
