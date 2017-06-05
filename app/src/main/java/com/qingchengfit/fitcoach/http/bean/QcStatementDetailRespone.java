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
 * Created by Paper on 15/10/13 2015.
 */

public class QcStatementDetailRespone extends QcResponse {
    @SerializedName("data") public DetailData data;

    public static class DetailData {
        @SerializedName("service") public CoachService service;
        @SerializedName("schedules") public StatamentSchedule schedules;
    }

    public static class StatamentSchedule {
        @SerializedName("start") public String start;
        @SerializedName("end") public String end;
        @SerializedName("stat") public StatementGlance stat;
        @SerializedName("user_id") public String user_id;
        @SerializedName("schedules") public List<QcScheduleBean> schedules;
    }

    public static class StatementGlance {
        @SerializedName("course_count") public int course_count;
        @SerializedName("user_count") public int user_count;
        @SerializedName("order_count") public int order_count;
    }
}
