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
 * Created by Paper on 16/1/8 2016.
 */
public class QcBatchResponse extends QcResponse {
    @SerializedName("data")
    public Data data;

    public static class Data{
        @SerializedName("total_count")
        public int total_count;
        @SerializedName("current_page")
        public int current_page;
        @SerializedName("pages")
        public int pages;
        @SerializedName("schedules")
        public List<Schedule> schedules;
        @SerializedName("timetables")
        public List<Schedule> timetables;
    }

    public static class Schedule{
        @SerializedName("start")
        public String start;
        @SerializedName("end")
        public String end;
        @SerializedName("id")
        public long id;
    }
}
