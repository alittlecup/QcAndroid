package cn.qingchengfit.student.bean;

import cn.qingchengfit.saascommon.model.FollowUpDataStatistic;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/3/8.
 */

public class AttendanceCharDataBean {
    @SerializedName("attendances") public List<FollowUpDataStatistic.DateCountsBean> datas;
}
