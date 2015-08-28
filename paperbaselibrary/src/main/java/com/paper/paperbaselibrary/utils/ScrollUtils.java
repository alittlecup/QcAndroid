package com.paper.paperbaselibrary.utils;

import android.support.v4.view.ViewCompat;
import android.view.View;

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
 * Created by Paper on 15/8/27 2015.
 */
public class ScrollUtils {
    public static boolean canChildrenScrollHorizontally(View view, int up) {
        if (ViewCompat.canScrollHorizontally(view, up))
            return true;
        else {
        }

        return true;
    }
}
