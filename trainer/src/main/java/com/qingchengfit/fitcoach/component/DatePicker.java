package com.qingchengfit.fitcoach.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;




import cn.qingchengfit.utils.DateUtils;
import com.marcohc.robotocalendar.CalendarFragment;
import com.qingchengfit.fitcoach.R;
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
 * Created by Paper on 15/10/17 2015.
 */
public class DatePicker extends DialogFragment {

	ViewPager vp;
	View bg;
	TextView tvMonth;

    private DatePickerChange listener;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TopDialogWindowStyle);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_datepicker, container, false);
      vp = (ViewPager) view.findViewById(R.id.vp);
      bg = (View) view.findViewById(R.id.bg);
      tvMonth = (TextView) view.findViewById(R.id.tv_month);
      view.findViewById(R.id.bg).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onBg();
        }
      });
      view.findViewById(R.id.bg_up).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onBg();
        }
      });

      tvMonth.setText(DateUtils.getChineseMonth(new Date()));
        vp.setAdapter(new CalendarAdapter(getChildFragmentManager()));
        vp.setCurrentItem(50);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override public void onPageSelected(int position) {

                //if (listener != null){
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, position - 50);
                tvMonth.setText(DateUtils.getChineseMonth(c.getTime()));
                //    listener.onMonthChange(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1);
                //}
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    public void setListener(DatePickerChange listener) {
        this.listener = listener;
    }

    @Override public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

 public void onBg() {
        this.dismiss();
    }

    @Override public void dismiss() {
        if (listener != null) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, vp.getCurrentItem() - 50);
            listener.onDismiss(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        }
        super.dismiss();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }

    public interface DatePickerChange {
        void onMonthChange(int year, int month);

        void onDismiss(int year, int month);
    }

    public class CalendarAdapter extends FragmentStatePagerAdapter {

        public CalendarAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return CalendarFragment.newInstance(position - 50);
        }

        @Override public int getCount() {
            return 100;
        }
    }
}
