package cn.qingchengfit.student.bean;

import java.util.List;

public class CoachStudentFilter {

  List<Filter> filters;
  String title;

  public List<Filter> getFilters() {
    return filters;
  }

  public String getTitle() {
    return title;
  }

  public class Filter{
    String id;
    String name;

    public String getId() {
      return id;
    }

    public String getName() {
      return name;
    }
  }

}
