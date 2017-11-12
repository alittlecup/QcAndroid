package cn.qingchengfit.saasbase.student.views.followup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.qingchengfit.saasbase.student.network.body.StudentFilter;

/**
 * Created by huangbaole on 2017/11/7.
 */

public class FollowUpEvent {
    @Retention(RetentionPolicy.SOURCE)
    public @interface Event {
        int STUDENT_STATUS = 0;
        int GENDER = 1;
        int TODAY = 2;
        int LATESTDAY = 3;
        int SALERS = 4;
        int TOP_SALERS = 5;
        int FILTER = 6;
    }

    StudentFilter filter = new StudentFilter();
    int EVENT = -1;

    public StudentFilter getFilter() {
        return filter;
    }

    public void setFilter(StudentFilter filter) {
        this.filter = filter;
    }

    public FollowUpEvent(@Event int event, StudentFilter filter) {
        this.filter = filter;
        this.EVENT = event;
    }

    public int getEVENT() {
        return EVENT;
    }

    public void setEVENT(int EVENT) {
        this.EVENT = EVENT;
    }
}
