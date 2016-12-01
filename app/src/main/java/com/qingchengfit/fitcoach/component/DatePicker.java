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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.marcohc.robotocalendar.CalendarFragment;
import com.qingchengfit.fitcoach.R;
import java.util.Calendar;

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

    @BindView(R.id.vp) ViewPager vp;
    @BindView(R.id.bg) View bg;
    private Unbinder unbinder;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TopDialogWindowStyle);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_datepicker, container, false);
        unbinder = ButterKnife.bind(this,view);
        vp.setAdapter(new CalendarAdapter(getChildFragmentManager()));
        vp.setCurrentItem(500);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                //RxBus.getBus().post(new EventMonthChange(position-500));
                if (listener != null){
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH,position-500);
                    listener.onMonthChange(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1);
                }
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }
    public  interface DatePickerChange{
        void onMonthChange(int year,int month);
    }

    private DatePickerChange listener;

    public void setListener(DatePickerChange listener) {
        this.listener = listener;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @OnClick({R.id.bg,R.id.bg_up})
    public void onBg(){
        this.dismiss();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public class CalendarAdapter extends FragmentStatePagerAdapter{

        public CalendarAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return CalendarFragment.newInstance(position -500);
        }

        @Override public int getCount() {
            return 1000;
        }

    }




}
