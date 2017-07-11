package cn.qingchengfit.recruit.event;

import cn.qingchengfit.recruit.model.PublishPosition;
import cn.qingchengfit.utils.Hash;
import java.util.HashMap;

/**
 * Created by fb on 2017/7/3.
 */

public class EventPulishPosition {
  public HashMap<String, Object> damenMap;

  public EventPulishPosition(HashMap<String, Object> damenMap) {
    this.damenMap = damenMap;
  }

  public static EventPulishPosition build(HashMap<String, Object> map){
    return new EventPulishPosition(map);
  }

}
