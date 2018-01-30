package cn.qingchengfit.saasbase.events;

import cn.qingchengfit.model.base.QcStudentBean;

/**
 * Created by fb on 2018/1/30.
 */

public class EventStudentBuyCard {

  QcStudentBean studentBean;

  public EventStudentBuyCard(QcStudentBean studentBean) {
    this.studentBean = studentBean;
  }

  public QcStudentBean getStudentBean() {
    return studentBean;
  }
}
