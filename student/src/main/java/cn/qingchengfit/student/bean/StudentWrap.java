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
}
