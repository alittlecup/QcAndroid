package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.CoursePlan;
import java.util.List;

public class QcResponseCoursePlan extends QcResponse {
    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("plans") public List<CoursePlan> plans;
    }
}