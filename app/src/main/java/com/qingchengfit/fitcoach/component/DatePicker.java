package com.qingchengfit.fitcoach.component;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.qingchengfit.fitcoach.R;

import java.util.Date;

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

    public DatePicker(Context context) {
        super(context, R.style.ChoosePicDialogStyle);
        View view = getLayoutInflater().inflate(R.layout.dialog_datepicker, null);
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.calendarView);
        robotoCalendarView.markDayAsCurrentDay(new Date());

    }

    public void setDayClickListener(RobotoCalendarView.RobotoCalendarListener listener) {
        robotoCalendarView.setRobotoCalendarListener(listener);
    }

    public void markDay(Date date) {
        robotoCalendarView.markDayAsSelectedDay(date);
    }


    @Override
    public void show() {
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        super.show();
    }
}
