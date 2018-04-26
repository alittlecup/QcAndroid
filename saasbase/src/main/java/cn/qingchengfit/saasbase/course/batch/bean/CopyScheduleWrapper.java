package cn.qingchengfit.saasbase.course.batch.bean;

import java.util.List;

/**
 * Created by fb on 2018/4/17.
 */

public class CopyScheduleWrapper {
  List<CopySchedule> schedules;

  public void setSchedules(List<CopySchedule> schedules) {
    this.schedules = schedules;
  }

  public List<CopySchedule> getSchedules() {
    return schedules;
  }

}
