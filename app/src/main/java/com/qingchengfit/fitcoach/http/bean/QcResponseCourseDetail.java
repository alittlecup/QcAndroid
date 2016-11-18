package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;

import cn.qingchengfit.staffkit.model.bean.CourseDetail;

public class QcResponseCourseDetail extends QcResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("course")
        public CourseDetail courseDetail;
    }
}