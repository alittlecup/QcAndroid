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
 * Created by Paper on 15/10/12 2015.
 */
public class QcSchedulesResponse extends QcResponse {

    @SerializedName("data")
    public Data data;

    private QcSchedulesResponse() {
    }

    public static QcSchedulesResponse newInstance() {
        QcSchedulesResponse qcMyhomeResponse = new QcSchedulesResponse();
        return qcMyhomeResponse;
    }

    public void cache() {

    }

    public void queryResponse() {

    }

    public static class Data {
        @SerializedName("services")
        public List<Service> services;
//        @SerializedName("services")
//        public List<Schedule> schedules;
    }

    public static class Service {
        @SerializedName("rests")
        public List<Rest> rests;
        @SerializedName("service")
        public CoachService system;
        @SerializedName("schedules")
        public List<QcScheduleBean> schedules;

    }

    public static class Schedule {
        @SerializedName("rests")
        public List<Rest> rests;
        @SerializedName("system")
        public QcCoachSystem system;
        @SerializedName("schedules")
        public List<QcScheduleBean> schedules;

    }

    public static class Rest {
        @SerializedName("url")
        public String url;
        @SerializedName("start")
        public String start;
        @SerializedName("end")
        public String end;
        @SerializedName("teacher")
        public Teacher teacher;
    }

    public static class Teacher {
        @SerializedName("username")
        public String username;
        @SerializedName("id")
        public String id;
        @SerializedName("avatar")
        public String avatar;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }


}
