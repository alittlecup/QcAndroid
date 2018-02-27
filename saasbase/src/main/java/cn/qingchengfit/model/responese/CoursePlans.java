package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CoursePlans {

    @SerializedName("plans") public List<CoursePlan> plans;
}