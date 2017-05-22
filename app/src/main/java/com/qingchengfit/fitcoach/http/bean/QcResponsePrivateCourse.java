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
 * Created by Paper on 16/4/30 2016.
 */
public class QcResponsePrivateCourse extends QcResponse {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("coaches") public List<PrivateClass> coaches;

        @SerializedName("order_url") public String order_url;
        @SerializedName("total_count") public int total_count;
    }

    public class PrivateClass {

        @SerializedName("courses_count") public int courses_count;

        @SerializedName("username") public String username;

        @SerializedName("avatar") public String avatar;

        @SerializedName("from_date") public String from_date;

        @SerializedName("to_date") public String to_date;

        @SerializedName("id") public String id;
    }
}
