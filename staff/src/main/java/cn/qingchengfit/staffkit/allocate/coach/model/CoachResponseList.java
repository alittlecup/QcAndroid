package cn.qingchengfit.staffkit.allocate.coach.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/5/4.
 */

public class CoachResponseList {

    @SerializedName("coaches") public List<Coach> coaches;
}
