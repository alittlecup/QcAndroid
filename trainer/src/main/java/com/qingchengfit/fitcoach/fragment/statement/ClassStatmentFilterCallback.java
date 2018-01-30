package com.qingchengfit.fitcoach.fragment.statement;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.report.bean.CourseTypeSample;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/6/29 2016.
 */
public interface ClassStatmentFilterCallback {
    void setCourse(CourseTypeSample course);

    void setCoach(Staff coach);

    void setStudent(QcStudentBean student);
}
