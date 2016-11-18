package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.qingchengfit.staffkit.model.bean.CourseDetail;

public class QcResponseCourseList extends QcResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("courses")
        public List<CourseDetail> courses;
    }
}