package cn.qingchengfit.student.event;

/**
 * 根据选项内容修改问题题目的event
 */
public class DynamicPtagItemEvent {

  public static final int TYPE_TRAINER_FEEDBACK = 10001;
  public static final int TYPE_TRAINER_GOAL = 10002;

  //问题Id
  private String parentId;
  //针对训练反馈,为答案位置; 针对运动目标动机，为选择的评分
  private int position;
  private int type;

  public DynamicPtagItemEvent(String parentId, int position, int type){
    this.parentId = parentId;
    this.position = position;
    this.type = type;
  }

  public int getPosition() {
    return position;
  }

  public String getParentId() {
    return parentId;
  }

  public int getType() {
    return type;
  }
}
