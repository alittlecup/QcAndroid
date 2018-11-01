package cn.qingchengfit.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import cn.qingchengfit.widgets.R;
import java.lang.reflect.Method;

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

  public static int autoPaddingBottom(float itemHeihgt, int itemCount, Context context,
      int offset) {
    Resources resources = context.getResources();
    int padding =
        getTrueheight(context) - getActionbarBarHeight(context) - offset - ((int) dpToPx(itemHeihgt,
            resources) * itemCount);
    if (padding > 0) {
      return padding;
    } else {
      return dpToPx(R.dimen.activity_vertical_margin, resources);
    }
  }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            String sNavBarOverride = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    Class c = Class.forName("android.os.SystemProperties");
                    Method m = c.getDeclaredMethod("get", String.class);
                    m.setAccessible(true);
                    sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
                } catch (Throwable e) {
                    sNavBarOverride = null;
                }
            }

            // check override flag (see static block)
            if ("1".equals(sNavBarOverride)) {
                hasNav = true;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = false;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

  public static int getActionbarBarHeight(Context context) {
    int actionBarHeight = 0;
    TypedValue tv = new TypedValue();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
            context.getResources().getDisplayMetrics());
      }
    } else {
      actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
          context.getResources().getDisplayMetrics());
    }
    return actionBarHeight;
    }
  public static boolean isTouchPointInView(View view, float x, float y) {
    if (view == null) {
      return false;
    }
    int[] location = new int[2];
    view.getLocationOnScreen(location);
    int left = location[0];
    int top = location[1];
    int right = left + view.getMeasuredWidth();
    int bottom = top + view.getMeasuredHeight();
    if (y >= top && y <= bottom && x >= left
        && x <= right) {
      return true;
    }
    return false;
  }
}
