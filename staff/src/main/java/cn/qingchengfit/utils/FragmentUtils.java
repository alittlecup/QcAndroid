package cn.qingchengfit.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

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
 * Created by Paper on 16/3/31 2016.
 */
public class FragmentUtils {
    public static Fragment getCurrentFragment(FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
            return currentFragment;
        } else {
            return null;
        }
    }
}
