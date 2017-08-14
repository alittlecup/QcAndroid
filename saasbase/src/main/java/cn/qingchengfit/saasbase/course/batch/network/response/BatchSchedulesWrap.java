package cn.qingchengfit.saasbase.course.batch.network.response;

import cn.qingchengfit.network.response.QcListData;
import cn.qingchengfit.saasbase.course.batch.bean.BatchSchedule;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BatchSchedulesWrap extends QcListData {
    //团课
    @SerializedName("schedules") public List<BatchSchedule> schedules;
    //私教排期
    @SerializedName("timetables") public List<BatchSchedule> timetables;
}
