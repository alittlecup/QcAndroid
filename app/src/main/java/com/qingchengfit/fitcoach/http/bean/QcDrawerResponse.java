package com.qingchengfit.fitcoach.http.bean;

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
 * Created by Paper on 15/10/16 2015.
 */
public class QcDrawerResponse extends QcResponse {
    @SerializedName("data") public DrawerData data;

    public static class DrawerData {
        @SerializedName("plan_count") public String plan_count;
        @SerializedName("user_count") public String user_count;
        @SerializedName("system_count") public String system_count;
        @SerializedName("coach") public DrawerCoach coach;
        @SerializedName("activities") public List<Activity> activities;
    }

    public static class DrawerCoach {
        @SerializedName("username") public String username;
        @SerializedName("avatar") public String avatar;
        @SerializedName("id") public int id;
    }

    public static class Activity {
        @SerializedName("image") public String image;
        @SerializedName("link") public String link;
        @SerializedName("name") public String name;
        @SerializedName("id") public String id;
    }
}
