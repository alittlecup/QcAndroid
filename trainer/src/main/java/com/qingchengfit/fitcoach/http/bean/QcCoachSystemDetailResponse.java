package com.qingchengfit.fitcoach.http.bean;

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
 * Created by Paper on 15/10/13 2015.
 */
public class QcCoachSystemDetailResponse extends QcResponse {

    @SerializedName("data") public Data date;

    public static class Data {
        @SerializedName("systems") public List<CoachSystemDetail> systems;
    }

    public static class CoachSystemDetail {
        @SerializedName("name") public String name;
        @SerializedName("url") public String url;
        @SerializedName("color") public String color;
        @SerializedName("photo") public String photo;
        @SerializedName("address") public String address;
        @SerializedName("users_count") public int users_count;
        @SerializedName("courses_count") public int courses_count;
        @SerializedName("id") public int id;
        @SerializedName("is_personal_system") public boolean is_personal_system;
    }
}
