package cn.qingchengfit.staffkit.views.signin.bean;

import java.util.List;

public class SignChooseWeekEvent {
  List<Integer> weeks;
  public SignChooseWeekEvent(List<Integer> weeks){
    this.weeks=weeks;
  }

  public List<Integer> getWeeks() {
    return weeks;
  }
}
