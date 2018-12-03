package cn.qingchengfit.card.bean;

import cn.qingchengfit.model.base.User;
import java.util.List;

public class UserWithCoupons {
  private User user;
  private List<Coupon> coupons;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Coupon> getCoupons() {
    return coupons;
  }

  public void setCoupons(List<Coupon> coupons) {
    this.coupons = coupons;
  }
}
