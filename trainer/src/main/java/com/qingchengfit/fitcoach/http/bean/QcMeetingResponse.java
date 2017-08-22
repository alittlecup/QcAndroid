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
 * Created by Paper on 15/11/18 2015.
 */
public class QcMeetingResponse extends QcResponse {
    @SerializedName("data") public MeetingData data;

    public static class MeetingData {
        @SerializedName("meetings") public List<Meeting> meetings;
    }

    public static class Meeting {
        @SerializedName("city") public String city;
        @SerializedName("name") public String name;
        @SerializedName("open_end") public String open_end;
        @SerializedName("image") public String image;
        @SerializedName("open_start") public String open_start;
        @SerializedName("link") public String link;
        @SerializedName("address") public String address;
    }
}
