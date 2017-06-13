package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

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
 * Created by Paper on 15/12/29 2015.
 */
public class QcGymDetailResponse extends QcResponse {
    @SerializedName("data") public GymDetailData data;

    public static class GymDetailData {
        @SerializedName("shop") public Shop shop;
        @SerializedName("service") public CoachService service;
    }

    public static class Shop {
        @SerializedName("shop") public ShopBean shop;
        @SerializedName("courses") public List<ShopCourse> courses;
        @SerializedName("user_count") public int user_count;
        @SerializedName("courses_count") public int courses_count;
        @SerializedName("system_logo") public String system_logo;
        @SerializedName("private_url") public String private_url;
        @SerializedName("team_url") public String team_url;
    }
}
