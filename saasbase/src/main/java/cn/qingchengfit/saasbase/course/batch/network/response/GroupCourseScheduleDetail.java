package cn.qingchengfit.saasbase.course.batch.network.response;

import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GroupCourseScheduleDetail {
    @SerializedName("course") public BatchCourse course;
    @SerializedName("batches") public List<GroupCourseSchedule> batches;
}