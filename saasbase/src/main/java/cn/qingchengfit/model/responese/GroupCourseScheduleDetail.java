package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * 团课排期详情
 * <p>
 * Created by Paper on 16/4/30 2016.
 */
public class GroupCourseScheduleDetail {
    @SerializedName("course") public CourseTypeSample course;
    @SerializedName("batches") public List<GroupCourseSchedule> batches;
}
