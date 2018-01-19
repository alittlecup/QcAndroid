package cn.qingchengfit.saasbase.course.course.network.response;

import cn.qingchengfit.saasbase.course.course.bean.CoursePlan;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CoursePlans {

    @SerializedName("plans") public List<CoursePlan> plans;
}