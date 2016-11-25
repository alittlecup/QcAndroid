package com.qingchengfit.fitcoach.fragment.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.DateUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import java.util.Date;

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

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tv_month) TextView tvMonth;
    private CoachService mCoachService;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_week, container, false);
        unbinder = ButterKnife.bind(this, view);
        viewpager.setAdapter(new ScheduleWeekAdapter(getChildFragmentManager()));
        viewpager.setCurrentItem(1000);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                Date d = DateUtils.formatDateFromYYYYMMDD(DateUtils.getWeek(position-1000).first);
                //tvMonth.setText();
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override public String getFragmentName() {
        return ScheduleWeekFragment.class.getName();
    }

    @OnClick({ R.id.tv_month, R.id.day_view }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_month:

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
