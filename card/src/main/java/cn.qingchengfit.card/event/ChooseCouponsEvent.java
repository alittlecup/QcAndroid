package cn.qingchengfit.card.event;

import cn.qingchengfit.card.bean.Coupon;

public class ChooseCouponsEvent {
  public Coupon getCoupon() {
    return coupon;
  }

  private Coupon coupon;
  public ChooseCouponsEvent(Coupon coupon){
    this.coupon=coupon;
  }
}
