package cn.qingchengfit.staffkit.rxbus.event;

import cn.qingchengfit.model.base.User_Student;

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
 * <p>
 * Created by Paper on 16/5/16 2016.
 */
public class StudentBaseInfoEvent {
    public User_Student user_student;
    public String privateUrl;
    public String groupUrl;
    public int status;

    public StudentBaseInfoEvent(User_Student user_student, String privateUrl, String groupUrl, int status) {
        this.user_student = user_student;
        this.privateUrl = privateUrl;
        this.groupUrl = groupUrl;
        this.status = status;
    }
}
