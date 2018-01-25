package cn.qingchengfit.saasbase.db;

import android.text.TextUtils;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import io.reactivex.Flowable;
import java.util.List;
import javax.inject.Inject;
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
    @Inject QcDbManager qcDbManager;

    @Inject
    public GymBaseInfoAction() {
    }

    public Flowable<List<CoachService>> getAllGyms() {
        return qcDbManager.getAllCoachService();
    }

    public  Flowable<List<CoachService>> getAllGymsByBrand(String brandId) {
        return qcDbManager.getAllCoachServiceByBrand(brandId);
    }

    public  void writeGyms(final List<CoachService> services) {

        qcDbManager.writeGyms(services);
    }

    //read
    public  Observable<List<CoachService>> getGymsByBrand(final String brand_id) {
        return null;
    }

    public  CoachService getGymNow(String gymid, String gymmodel) {
        return qcDbManager.getGymNow(gymid, gymmodel);
    }

    public  String getGymBrandNow(String gymid, String gymmodel) {
        CoachService gyms = getGymNow(gymid, gymmodel);

        if (gyms != null) {
            return gyms.getBrand_id();
        } else {
            return null;
        }
    }

    public  String getShopId(String id, String model) {
        CoachService coachService = getGymNow(id, model);
        if (coachService != null) {
            return coachService.getShop_id();
        } else {
            return "";
        }
    }

    public  String getShopIdNow(String id, String model) {
        return getShopId(id, model);
    }

    public  CoachService getShopNow(String id, String model) {

        return getGymNow(id, model);
    }

    public  String getShopNamesByIds(String brandid, List<String> ids) {
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

    public  String getShopNameById(String brandid, String shopid) {
        CoachService coachService = qcDbManager.getShopNameById(brandid, shopid);
        if (coachService != null) {
            return coachService.getName();
        } else {
            return "";
        }
    }

    public  CoachService getGymByShopIdNow(String brandid, String shopid) {
        CoachService coachService = qcDbManager.getShopNameById(brandid, shopid);
        return coachService;
    }

    public  Flowable<CoachService> getGymByModel(final String id, final String model) {
        return qcDbManager.getGymByModel(id, model);
    }

    public  Flowable<List<CoachService>> getGymByShopIds(String brandid, final List<String> shopid) {
        return qcDbManager.getGymByShopIds(brandid, shopid);
    }

    public  Flowable<List<CoachService>> getGymBaseInfo() {
        return qcDbManager.getAllCoachService();
    }
}
