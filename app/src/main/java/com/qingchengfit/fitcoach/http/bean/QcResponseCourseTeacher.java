package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.CourseTeacher;
import java.util.List;

public class QcResponseCourseTeacher extends QcResponse {
    @SerializedName("data") public Data data;

    public static class Shop {
        @SerializedName("logo") public String logo;
        @SerializedName("name") public String name;
        @SerializedName("address") public String address;
    }

    public class Data {
        @SerializedName("teachers") public List<CourseTeacher> teachers;
        @SerializedName("course") public CourseDetail courseDetail;
        @SerializedName("shop") public Shop shop;
    }
}