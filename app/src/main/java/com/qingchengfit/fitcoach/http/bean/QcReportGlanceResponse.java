package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.model.base.CoachService;
import com.google.gson.annotations.SerializedName;

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
public class QcReportGlanceResponse extends QcResponse {

    @SerializedName("data") public System data;

    public static class glanceservice {
        @SerializedName("service") public CoachService service;
        @SerializedName("stat") public System stat;
    }

    public static class System {
        @SerializedName("week") public UseData week;
        @SerializedName("today") public UseData today;
        @SerializedName("month") public UseData month;
    }

    public static class UseData {
        @SerializedName("from_date") public String from_date;
        @SerializedName("to_date") public String to_date;
        @SerializedName("user_count") public int user_count;
        @SerializedName("order_count") public int order_count;
        @SerializedName("course_count") public int course_count;
    }
}
