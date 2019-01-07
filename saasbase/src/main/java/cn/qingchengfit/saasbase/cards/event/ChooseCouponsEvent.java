package cn.qingchengfit.saasbase.cards.event;

import cn.qingchengfit.saasbase.cards.bean.Coupon;

public class ChooseCouponsEvent {
  public Coupon getCoupon() {
    return coupon;
  }

  private Coupon coupon;
  public ChooseCouponsEvent(Coupon coupon){
    this.coupon=coupon;
  }
}
