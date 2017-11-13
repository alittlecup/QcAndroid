package cn.qingchengfit.saasbase.student.network.body;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fb on 2017/3/8.
 */

public class AttendanceCharDataBean {
    @SerializedName("attendances") public List<FollowUpDataStatistic.DateCountsBean> datas;
}
