package cn.qingchengfit.model.responese;

import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.utils.DateUtils;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //课程安排 item bean
 * //Created by yangming on 16/11/19.
 */

public class CourseSchedule {
    @SerializedName("id") public String id;
    @SerializedName("start") public String start;
    @SerializedName("end") public String end;

    public CourseManageBean toCourseScheduleBean(int mCourseType) {
        CourseManageBean b = new CourseManageBean();
        b.month = DateUtils.Date2YYYYMM(DateUtils.formatDateFromServer(start));
        b.day = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(start));
        b.WeekDay = DateUtils.getDayOfWeek(DateUtils.formatDateFromServer(start));
        if (mCourseType == Configs.TYPE_GROUP) {
            b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(start));
        } else {
            b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(start)) + "-" + DateUtils.getTimeHHMM(
                DateUtils.formatDateFromServer(end));
        }
        b.start = DateUtils.formatDateFromServer(start);
        b.end = DateUtils.formatDateFromServer(end);
        b.outdue = DateUtils.formatDateFromServer(start).getTime() < new Date().getTime();
        b.id = id + "";
        b.length = DateUtils.formatDateFromServer(end).getTime() - DateUtils.formatDateFromServer(start).getTime();
        return b;
    }
}
