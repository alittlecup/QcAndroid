package cn.qingchengfit.student.bean;

import java.util.List;

public class CoachPtagQuestionnaire {
 String type;
 String id;
 List<PtagQuestion> questions;
 String title;

  public String getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public List<PtagQuestion> getQuestions() {
    return questions;
  }

  public String getTitle() {
    return title == null ? "无标题" : title;
  }
}
