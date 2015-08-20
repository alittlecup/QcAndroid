package com.paper.paperbaselibrary.utils;

import android.content.Context;
import android.content.pm.PackageManager;

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
 * Created by Paper on 15/8/19 2015.
 */
public class PermissionUtils {
    /**
     * 判断权限是否被禁止
     *
     * @param context
     * @param per
     * @return
     */

    public static boolean checkPermission(Context context, String per) {
        int result = context.checkCallingOrSelfPermission(per);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
