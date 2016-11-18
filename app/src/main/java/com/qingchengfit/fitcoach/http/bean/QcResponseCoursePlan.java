package com.qingchengfit.fitcoach.http.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.qingchengfit.staffkit.usecase.bean.CoursePlan;

public class QcResponseCoursePlan extends QcResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("plans")
        public List<CoursePlan> plans;
    }
}