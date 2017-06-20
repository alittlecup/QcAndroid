package com.qingchengfit.fitcoach.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import cn.qingchengfit.widgets.PagerSlidingTabImageStrip;
import com.qingchengfit.fitcoach.fragment.NotificationFragment;
import java.util.List;

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
 * Created by Paper on 16/1/7 2016.
 */
public class NotiFragmentAdater extends FragmentStatePagerAdapter implements PagerSlidingTabImageStrip.ImageTabProvider {

    private List<Fragment> datas;

    public NotiFragmentAdater(FragmentManager fm, List<Fragment> d) {
        super(fm);
        datas = d;
    }

    @Override public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Override public int getCount() {
        return datas.size();
    }

    @Override public CharSequence getPageTitle(int position) {
        return getTextStr(position);
    }

    @Override public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override public String getTextStr(int position) {
        if (position == 2) {
            return "会议培训";
        } else if (position == 0) {
            return "课程通知";
        } else {
            return "系统通知";
        }
    }

    @Override public boolean getShowRed(int position) {
        if (getItem(position) instanceof NotificationFragment) {
            return ((NotificationFragment) getItem(position)).getUnReadCount() > 0;
        }
        return false;
    }
}
