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
 * Created by Paper on 15/10/22 2015.
 */
public class QcPrivateGymReponse extends QcResponse {

    @SerializedName("data") public Data data;

    public static class Data {
        @SerializedName("system") public System system;
    }

    public static class System {
        @SerializedName("open_time") public List<OpenTime> openTimes;
        @SerializedName("name") public String name;
    }

    public static class OpenTime {
        @SerializedName("start") public String start;
        @SerializedName("end") public String end;
        @SerializedName("day") public int day;
    }
}
