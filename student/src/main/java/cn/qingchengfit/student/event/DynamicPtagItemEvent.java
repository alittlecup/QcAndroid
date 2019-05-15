package cn.qingchengfit.student.event;

import cn.qingchengfit.student.bean.PtagAnswerOption;

/**
 * 根据选项内容修改问题题目的event
 */
public class DynamicPtagItemEvent {

  public static final int TYPE_TRAINER_FEEDBACK = 10001;
  public static final int TYPE_TRAINER_GOAL = 10002;
  public static final int TYPE_TRAINER_STYLE = 10003;

  //问题Id
  private String parentId;
  //针对训练反馈,为答案位置; 针对运动目标动机，为选择的评分
  private int position;
  private int type;

  //计算训练风格
  private PtagAnswerOption option;

  public DynamicPtagItemEvent(String parentId, int position, int type){
    this.parentId = parentId;
    this.position = position;
    this.type = type;
  }

  public DynamicPtagItemEvent(PtagAnswerOption option, int type, String parentId){
    this.option = option;
    this.type = type;
    this.parentId = parentId;
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

  public PtagAnswerOption getOption() {
    return option;
  }
}
