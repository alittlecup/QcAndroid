package cn.qingchengfit.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by fb on 2017/4/14.
 */

public class GpsUtil {
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    public static int isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
        if (gps) return 3;
        return 0;
    }
}
