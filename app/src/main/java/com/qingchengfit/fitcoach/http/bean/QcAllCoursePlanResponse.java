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
 * Created by Paper on 15/10/15 2015.
 */
public class QcAllCoursePlanResponse extends QcResponse {

    @SerializedName("data")
    public Plans data;

    public static class Plans {
        @SerializedName("plans")
        public List<Plan> plans;
    }

    public static class Plan {

        @SerializedName("tags")
        public List<String> tags;
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
        @SerializedName("url")
        public String url;
        @SerializedName("type")
        public int type;//1.个人 2.所属 3.会议
    }
}
