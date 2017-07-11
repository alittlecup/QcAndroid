package cn.qingchengfit.recruit.event;

import cn.qingchengfit.recruit.network.body.JobBody;
import java.util.HashMap;

/**
 * Created by fb on 2017/7/3.
 */

public class EventPulishPosition {

  public JobBody body;

  public EventPulishPosition(JobBody body) {
    this.body = body;
  }

}
