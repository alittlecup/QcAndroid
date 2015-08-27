package com.paper.paperbaselibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.util.TypedValue;
import android.view.ViewConfiguration;

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
 * Created by Paper on 15/8/17 2015.
 */
public class MeasureUtils {
    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int dpToPx(@DimenRes int res, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, resources.getDimension(res), resources.getDisplayMetrics());
        return (int) px;
    }


    public static int getScreenWidth(Resources resources) {
        return resources.getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Resources resources) {
        return resources.getDisplayMetrics().heightPixels;
    }

    /**
     * height with out virtual bar and system bar
     */
    public static int getTrueheight(Context context) {
        boolean hasVir = ViewConfiguration.get(context).hasPermanentMenuKey();
        int navigationHeight = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = context.getResources().getDimensionPixelSize(navigationHeight);
        int statusHeight = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusHeight = context.getResources().getDimensionPixelSize(statusHeight);
        if (hasVir)
            return getScreenHeight(context.getResources()) - navigationHeight - statusHeight;
        else return getScreenHeight(context.getResources()) - statusHeight;
    }




}
