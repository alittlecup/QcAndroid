package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.qingchengfit.staffkit.model.bean.CourseDetail;
import cn.qingchengfit.staffkit.usecase.bean.CourseTeacher;

public class QcResponseCourseTeacher extends QcResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("teachers")
        public List<CourseTeacher> teachers;
        @SerializedName("course")
        public CourseDetail courseDetail;
        @SerializedName("shop")
        public Shop shop;
    }

    public static class Shop{
        @SerializedName("logo")
        public String logo;
        @SerializedName("name")
        public String name;
        @SerializedName("address")
        public String address;

    }

}