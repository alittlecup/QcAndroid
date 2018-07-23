package cn.qingchengfit.student.bean;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.StudentBean;

public class StudentWrap {
  StudentBean studentBean;

  public StudentBean getStudentBean() {
    return studentBean;
  }

  public void setStudentBean(StudentBean studentBean) {
    this.studentBean = studentBean;
  }



  public String username() {
    if (studentBean != null) return studentBean.username;
    return "";
  }

  public String id() {
    if (studentBean != null) return studentBean.id;
    return "";
  }

  public String phone() {
    if (studentBean != null) return studentBean.phone;
    return "";
  }

  public String avatar() {
    if (studentBean != null) return studentBean.avatar;
    return "";
  }

  public int gender() {
    if (studentBean != null) return studentBean.gender ? 0 : 1;
    return 0;
  }

  public String checkin_avatar() {
    if (studentBean != null) return studentBean.checkin_avatar;
    return "";
  }
}
