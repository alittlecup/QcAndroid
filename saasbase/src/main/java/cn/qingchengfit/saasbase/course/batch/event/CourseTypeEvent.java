package cn.qingchengfit.saasbase.course.batch.event;

import cn.qingchengfit.saasbase.course.course.bean.CourseType;

/**
 * Created by fb on 2018/4/18.
 */

public class CourseTypeEvent {

  private CourseType course;

  public CourseTypeEvent(CourseType course) {
    this.course = course;
  }

  public CourseType getCourseType() {
    return course;
  }
}
