package com.qingchengfit.fitcoach.Utils;

import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.http.bean.CoachService;

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
    public static boolean isInBrand(CoachService gymBase) {
        return gymBase == null || gymBase.id == 0 || gymBase.model == null;
    }
    public static String getBrandId(CoachService coachService, Brand brand){
        if (isInBrand(coachService)){
            return brand.getId();
        }else {
            return coachService.brand_name;
        }
    }
    public static HashMap<String,String> getParams(CoachService gymBase, Brand brand){
        HashMap<String,String> params = new HashMap<>();
        if (isInBrand(gymBase)){
            params.put("brand_id",brand.getId());
        }
        else {
            params.put("id",gymBase.getId()+"");
            params.put("model",gymBase.getModel());
        }
        return params;
    }

    public static HashMap<String,String> getParams(CoachService gymBase, Brand brand , String shopid){
        HashMap<String,String> params = new HashMap<>();
        if (isInBrand(gymBase)){
            params.put("brand_id",brand.getId());
            if (shopid != null)
                params.put("shop_id",shopid);
        }
        else {
            params.put("id",gymBase.getId()+"");
            params.put("model",gymBase.getModel());
        }
        return params;
    }


}
