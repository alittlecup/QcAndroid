package com.qingchengfit.fitcoach.component;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/10/17 2015.
 */
public class DatePicker extends Dialog {


    private final RobotoCalendarView robotoCalendarView;
    private Calendar mCurCalendar;
    private int mMonthOffset = 0;
    public DatePicker(Context context) {
        super(context, R.style.ChoosePicDialogStyle);
        View view = getLayoutInflater().inflate(R.layout.dialog_datepicker, null);
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.calendarView);
        mCurCalendar = Calendar.getInstance(Locale.getDefault());

        robotoCalendarView.markDayAsSelectedDay(new Date());


    }

    public void setDayClickListener(RobotoCalendarView.RobotoCalendarListener listener) {
        robotoCalendarView.setRobotoCalendarListener(listener);
    }

    public Calendar getmCurCalendar() {
        return mCurCalendar;
    }


    @Override
    public void show() {
        mCurCalendar.setTime(new Date());
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        super.show();
        updateCalendar();
        robotoCalendarView.markDayAsSelectedDay(new Date());
    }

    public void addMonth() {

        mCurCalendar.add(Calendar.MONTH, 1);
        updateCalendar();

    }

    public void minlusMonth() {
        mCurCalendar.add(Calendar.MONTH, -1);
        updateCalendar();

    }

    private void updateCalendar() {
        robotoCalendarView.initializeCalendar(mCurCalendar);
    }

    public void markDay(String day) {
        robotoCalendarView.markSecondUnderlineWithStyle(RobotoCalendarView.RED_COLOR, DateUtils.formatDateFromString(day));
    }

    public void markCurDay() {
        if (mCurCalendar.get(Calendar.MONTH) == new Date().getMonth() &&
                mCurCalendar.get(Calendar.YEAR) == new Date().getYear()
                ) {
            robotoCalendarView.markDayAsSelectedDay(new Date());
        }
    }

    @Override
    public void hide() {
        super.hide();

    }
}
