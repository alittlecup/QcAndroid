package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.CourseDetail;

import java.util.List;


public class QcResponseCourseList extends QcResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("courses")
        public List<CourseDetail> courses;
    }
}