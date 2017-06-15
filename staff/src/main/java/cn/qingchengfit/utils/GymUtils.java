package cn.qingchengfit.utils;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import java.util.HashMap;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/13 2016.
 */
public class GymUtils {
    public static String getCourseTypeStr(boolean isPrivate) {
        return isPrivate ? "timetables" : "schedules";
    }

    public static boolean isInBrand(CoachService gymBase) {
        return gymBase == null || gymBase.getId() == null || gymBase.getModel() == null;
    }

    public static String getBrandId(CoachService coachService, Brand brand) {
        if (isInBrand(coachService)) {
            return brand.getId();
        } else {
            return coachService.getBrand_id();
        }
    }

    public static HashMap<String, Object> getParams(CoachService gymBase, Brand brand) {
        HashMap<String, Object> params = new HashMap<>();
        if (isInBrand(gymBase) && brand != null) {
            params.put("brand_id", brand.getId());
        } else {
            params.put("id", gymBase.getId());
            params.put("model", gymBase.getModel());
        }
        return params;
    }

    public static ArrayMap<String, String> getParamsV2(CoachService gymBase, Brand brand) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (isInBrand(gymBase)) {
            params.put("brand_id", brand == null ? null : brand.getId());
        } else {
            params.put("id", gymBase.getId());
            params.put("model", gymBase.getModel());
        }
        return params;
    }

    public static HashMap<String, Object> getParams(CoachService gymBase, Brand brand, String shopid) {
        HashMap<String, Object> params = new HashMap<>();
        if (isInBrand(gymBase)) {
            params.put("brand_id", brand.getId());
            if (shopid != null) params.put("shop_id", shopid);
        } else {
            params.put("id", gymBase.getId());
            params.put("model", gymBase.getModel());
        }
        return params;
    }

    public static int getSystemEndDay(CoachService coachService) {
        try {
            return cn.qingchengfit.utils.DateUtils.dayNumFromToday(DateUtils.formatDateFromYYYYMMDD(coachService.system_end()));
        } catch (Exception e) {
            return 0;
        }
    }

    //    public static void goQrScan(){
    //
    //    }
}
