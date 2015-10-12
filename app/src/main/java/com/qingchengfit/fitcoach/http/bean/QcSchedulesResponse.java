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

    public static class Data {
        @SerializedName("systems")
        public List<System> systems;
    }

    public static class System {
        @SerializedName("rests")
        public List<Rest> rests;
        @SerializedName("system")
        public SubSystem system;
        @SerializedName("schedules")
        public List<Schedule> schedules;

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
    }

    public static class SubSystem {
        @SerializedName("name")
        public String name;
        @SerializedName("url")
        public String url;
        @SerializedName("color")
        public String color;
        @SerializedName("cname")
        public String cname;
        @SerializedName("id")
        public int id;
        @SerializedName("is_personal_system")
        public boolean is_personal_system;

    }

    public static class Schedule {
        @SerializedName("count")
        public int count;
        @SerializedName("end")
        public String end;
        @SerializedName("start")
        public String start;
        @SerializedName("id")
        public int id;
        @SerializedName("url")
        public String url;
        @SerializedName("shop")
        public Shop shop;
        @SerializedName("course")
        public Course course;


    }

    public static class Shop {
        @SerializedName("name")
        public String name;
    }

    public static class Course {
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;

    }


}
