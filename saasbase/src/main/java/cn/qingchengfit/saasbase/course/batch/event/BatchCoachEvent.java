package cn.qingchengfit.saasbase.course.batch.event;

import cn.qingchengfit.saasbase.course.batch.bean.BatchCopyCoach;

/**
 * Created by fb on 2018/4/18.
 */

public class BatchCoachEvent {
  private BatchCopyCoach coach;

  public BatchCoachEvent(BatchCopyCoach coach) {
    this.coach = coach;
  }

  public BatchCopyCoach getCoach() {
    return coach;
  }
}
