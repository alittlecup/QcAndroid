package com.paper.paperbaselibrary.utils;

import android.text.TextUtils;

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
 * Created by Paper on 15/8/8 2015.
 */
public class TextpaperUtils {
    public static boolean isEmpty(String... s) {
        for (int i = 0; i < s.length; i++) {
            if (TextUtils.isEmpty(s[i]))
                return true;
        }
        return false;
    }
}
