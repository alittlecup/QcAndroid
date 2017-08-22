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
 * Created by Paper on 16/1/13 2016.
 */
public class StudentCourseResponse extends QcResponse {
    @SerializedName("data") public Data data;

    public static class Schedule {
        @SerializedName("start") public String start;
        @SerializedName("end") public String end;
        @SerializedName("url") public String url;
        @SerializedName("id") public long id;
        @SerializedName("course") public QcCourseResponse.Course course;
        @SerializedName("teacher") public QcSchedulesResponse.Teacher teacher;
    }

    public class Data {
        @SerializedName("schedules") public List<Schedule> schedules;
    }
}
