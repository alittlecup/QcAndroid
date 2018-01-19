package cn.qingchengfit.saasbase.course.batch.network.response;

import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;

public class GroupCourseSchedule {
    @SerializedName("teacher") public Staff teacher;
    @SerializedName("from_date") public String from_date;
    @SerializedName("to_date") public String to_date;
    @SerializedName("id") public String id;
}