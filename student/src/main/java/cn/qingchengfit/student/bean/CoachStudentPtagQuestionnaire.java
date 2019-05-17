package cn.qingchengfit.student.bean;

public class CoachStudentPtagQuestionnaire {
  String answer;
  String title;
  String type;

  public String getAnswer() {
    return answer == null ? "" : answer;
  }

  public String getTitle() {
    return title;
  }

  public String getType() {
    return type;
  }
}
