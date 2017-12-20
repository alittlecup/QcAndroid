package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import cn.qingchengfit.bean.CourseDetail;
import java.util.List;

public class QcResponseCourseList extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("courses") public List<CourseDetail> courses;
    }
}