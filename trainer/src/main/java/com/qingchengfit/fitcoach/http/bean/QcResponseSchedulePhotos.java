package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import cn.qingchengfit.bean.SchedulePhotos;
import java.util.List;

public class QcResponseSchedulePhotos extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("total_count") public int total_count;
        @SerializedName("current_page") public int current_page;
        @SerializedName("pages") public int pages;
        @SerializedName("schedules") public List<SchedulePhotos> schedules;
    }
}