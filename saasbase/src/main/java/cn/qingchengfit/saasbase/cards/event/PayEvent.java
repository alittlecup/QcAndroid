package cn.qingchengfit.saasbase.cards.event;

import cn.qingchengfit.saasbase.cards.bean.PayMethod;

/**
 * Created by fb on 2018/1/5.
 */

public class PayEvent {

  PayMethod payMethod;
  int position;

  public PayEvent(PayMethod payMethod, int position) {
    this.payMethod = payMethod;
    this.position = position;
  }

  public PayMethod getPayMethod() {
    return payMethod;
  }

  public int getPosition() {
    return position;
  }
}
