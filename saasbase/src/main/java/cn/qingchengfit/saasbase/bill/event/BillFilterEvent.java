package cn.qingchengfit.saasbase.bill.event;

import java.util.HashMap;

/**
 * Created by fb on 2017/10/31.
 */

public class BillFilterEvent {

  public HashMap<String, Object> map = new HashMap<>();

  public BillFilterEvent(HashMap<String, Object> map) {
    this.map = map;
  }
}
