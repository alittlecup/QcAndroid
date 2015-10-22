package com.qingchengfit.fitcoach.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bigkoo.pickerview.TimePopupWindow;
import com.bigkoo.pickerview.lib.ScreenInfo;
import com.bigkoo.pickerview.lib.WheelTime;
import com.qingchengfit.fitcoach.R;

import java.text.ParseException;
import java.util.Calendar;
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
 * Created by Paper on 15/9/18 2015.
 */
public class TimePeriodChooser extends Dialog implements View.OnClickListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    WheelTime wheelTime_start;
    WheelTime wheelTime_end;
    private View rootView; // 总的布局
    private View btnSubmit, btnCancel;
    private OnTimeSelectListener timeSelectListener;

    public TimePeriodChooser(Context context, TimePopupWindow.Type type) {
        super(context, R.style.ChoosePicDialogStyle);
//        this.setWidth(LayoutParams.FILL_PARENT);
//        this.setHeight(LayoutParams.WRAP_CONTENT);
//        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
//        this.setOutsideTouchable(true);
//        this.setAnimationStyle(R.style.timepopwindow_anim_style);

        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.pw_time_period, null);
        // -----确定和取消按钮
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        // ----时间转轮
        final View timepickerviewstart = rootView.findViewById(R.id.timepicker_start);
        ScreenInfo screenInfo1 = new ScreenInfo((Activity) context);
        wheelTime_start = new WheelTime(timepickerviewstart, TimePopupWindow.Type.HOURS_MINS);
        wheelTime_start.screenheight = screenInfo1.getHeight();
        // ----时间转轮
        final View timepickerviewend = rootView.findViewById(R.id.timepicker_end);
        ScreenInfo screenInfo2 = new ScreenInfo((Activity) context);
        wheelTime_end = new WheelTime(timepickerviewend, TimePopupWindow.Type.HOURS_MINS);
        wheelTime_end.screenheight = screenInfo1.getHeight();
        wheelTime_end.setCyclic(true);
        wheelTime_start.setCyclic(true);

        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime_start.setPicker(year, month, day, hours, minute);
        wheelTime_end.setPicker(year, month, day, hours, minute);

        setContentView(rootView);
    }

    /**
     * 设置可以选择的时间范围
     *
     * @param START_YEAR
     * @param END_YEAR
     */
    public void setRange(int START_YEAR, int END_YEAR) {
        WheelTime.setSTART_YEAR(START_YEAR);
        WheelTime.setEND_YEAR(END_YEAR);
    }

    /**
     * 设置选中时间
     *
     * @param date
     */
    public void setTime(Date date, Date end) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime_start.setPicker(year, month, day, hours, minute);
        Calendar calendar1 = Calendar.getInstance();
        if (end == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(end);
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        int hours1 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute1 = calendar.get(Calendar.MINUTE);
        wheelTime_end.setPicker(year1, month1, day1, hours1, minute1);
    }

    /**
     * 指定选中的时间，显示选择器
     *
     * @param date
     */
    public void showAtLocation(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime_start.setPicker(year, month, day, hours, minute);
        wheelTime_end.setPicker(year, month, day, hours, minute);
//        update();
//        super.showAtLocation(parent, gravity, x, y);
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


    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wheelTime_start.setCyclic(cyclic);
        wheelTime_end.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (timeSelectListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime_start.getTime());
                    Date end = WheelTime.dateFormat.parse(wheelTime_end.getTime());
                    timeSelectListener.onTimeSelect(date, end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
        }
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

//    public enum Type {
//        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN
//    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    public interface OnTimeSelectListener {
        public void onTimeSelect(Date start, Date end);
    }


}
