package cn.qingchengfit.staffkit.views.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import cn.qingchengfit.staffkit.views.custom.PagerSlidingTabStrip;
import cn.qingchengfit.staffkit.views.notification.page.NotificationFragment;
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
public class NotiFragmentAdater extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.RedDotTabProvider {

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
        if (position == 0) {
            return "签到/签出";
        } else {
            return "系统通知";
        }
    }

    @Override public boolean showRed(int position) {
        return getItem(position) instanceof NotificationFragment && ((NotificationFragment) getItem(position)).getUnReadCount() > 0;
    }
}
