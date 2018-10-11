package cn.qingchengfit.events;

import java.util.Map;


public class EventRePay {
  public Map<String, Object> getParams() {
    return params;
  }

  private Map<String, Object> params;

  public EventRePay(Map<String, Object> params) {
    this.params = params;
  }
}
