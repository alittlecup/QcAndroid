package cn.qingchengfit.staffkit.model.dbaction;

import android.text.TextUtils;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import java.util.List;
import rx.Observable;

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
 * Created by Paper on 16/2/18 2016.
 */
public class GymBaseInfoAction {

    static CoachService res = null;

    //    @Inject
    //    public GymBaseInfoAction(QCDbManager qcDbManager) {
    //
    //    }

    public static Observable<List<CoachService>> getAllGyms() {
        return QCDbManager.getAllCoachService();
    }

    public static Observable<List<CoachService>> getAllGymsByBrand(String brandId) {
        return QCDbManager.getAllCoachServiceByBrand(brandId);
    }

    public static void writeGyms(final List<CoachService> services) {

        QCDbManager.writeGyms(services);
    }

    //read
    public static Observable<List<CoachService>> getGymsByBrand(final String brand_id) {
        return null;
    }

    public static CoachService getGymNow(String gymid, String gymmodel) {
        return QCDbManager.getGymNow(gymid, gymmodel);
    }

    public static String getGymBrandNow(String gymid, String gymmodel) {
        CoachService gyms = getGymNow(gymid, gymmodel);

        if (gyms != null) {
            return gyms.getBrand_id();
        } else {
            return null;
        }
    }

    public static String getShopId(String id, String model) {
        CoachService coachService = getGymNow(id, model);
        if (coachService != null) {
            return coachService.getShop_id();
        } else {
            return "";
        }
    }

    public static String getShopIdNow(String id, String model) {
        return getShopId(id, model);
    }

    public static CoachService getShopNow(String id, String model) {

        return getGymNow(id, model);
    }

    public static String getShopNamesByIds(String brandid, List<String> ids) {
        String ret = "";
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            if (i < ids.size() - 1) {
                ret = TextUtils.concat(ret, getShopNameById(brandid, id), ",").toString();
            } else {
                ret = TextUtils.concat(ret, getShopNameById(brandid, id)).toString();
            }
        }
        return ret;
    }

    public static String getShopNameById(String brandid, String shopid) {
        CoachService coachService = QCDbManager.getShopNameById(brandid, shopid);
        if (coachService != null) {
            return coachService.getName();
        } else {
            return "";
        }
    }

    public static CoachService getGymByShopIdNow(String brandid, String shopid) {
        CoachService coachService = QCDbManager.getShopNameById(brandid, shopid);
        return coachService;
    }

    public static Observable<List<CoachService>> getGymByModel(final String id, final String model) {
        return QCDbManager.getGymByModel(id, model);
    }

    public static Observable<List<CoachService>> getGymByShopIds(String brandid, final List<String> shopid) {
        return QCDbManager.getGymByShopIds(brandid, shopid);
    }

    public static Observable<List<CoachService>> getGymBaseInfo() {
        return QCDbManager.getAllCoachService();
    }
}
