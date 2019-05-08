package cn.qingchengfit.student.bean;

public class PtagParams {
  String gym_id;
  String type;

  public PtagParams(String gym_id, String type){
    this.gym_id = gym_id;
    this.type = type;
  }

  public String getGym_id() {
    return gym_id;
  }

  public String getType() {
    return type;
  }
}
