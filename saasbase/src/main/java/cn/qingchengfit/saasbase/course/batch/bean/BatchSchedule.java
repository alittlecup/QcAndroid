package cn.qingchengfit.saasbase.course.batch.bean;

import com.google.gson.annotations.SerializedName;

public class BatchSchedule {
    @SerializedName("id") public String id;
    @SerializedName("start") public String start;
    @SerializedName("end") public String end;

    //public CourseManageBean toCourseScheduleBean(int mCourseType) {
    //    CourseManageBean b = new CourseManageBean();
    //    b.month = DateUtils.Date2YYYYMM(DateUtils.formatDateFromServer(start));
    //    b.day = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(start));
    //    b.WeekDay = DateUtils.getDayOfWeek(DateUtils.formatDateFromServer(start));
    //    if (mCourseType == Configs.TYPE_GROUP) {
    //        b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(start));
    //    } else {
    //        b.time = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(start)) + "-" + DateUtils.getTimeHHMM(
    //            DateUtils.formatDateFromServer(end));
    //    }
    //    b.start = DateUtils.formatDateFromServer(start);
    //    b.end = DateUtils.formatDateFromServer(end);
    //    b.outdue = DateUtils.formatDateFromServer(start).getTime() < new Date().getTime();
    //    b.id = id + "";
    //    b.length = DateUtils.formatDateFromServer(end).getTime() - DateUtils.formatDateFromServer(start).getTime();
    //    return b;
    //}
}