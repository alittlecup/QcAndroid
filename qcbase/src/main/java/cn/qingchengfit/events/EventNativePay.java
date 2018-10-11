package cn.qingchengfit.events;

import java.util.Map;


public class EventNativePay {
  public Map<String, Object> getParams() {
    return params;
  }

  private Map<String, Object> params;

  public EventNativePay(Map<String, Object> params) {
    this.params = params;
  }
}
