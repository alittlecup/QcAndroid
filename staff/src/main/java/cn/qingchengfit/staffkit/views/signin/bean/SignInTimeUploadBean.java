package cn.qingchengfit.staffkit.views.signin.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SignInTimeUploadBean {
  @SerializedName("max_users") private int maxUsers;
  @SerializedName("time_repeats") private List<SignTimeFrame> timeRepeats;
  @SerializedName("shop_checkin_expire_time") private int expireTime;
  @SerializedName("support_online_pay") private boolean supportOnlinePay;
  @SerializedName("card_costs") private List<CardCost> cardCosts;
  @SerializedName("online_pay_price") private float onlinePayPrice;

  public int getMaxUsers() {
    return maxUsers;
  }

  public boolean isSupportCardPay() {
    return cardCosts != null && !cardCosts.isEmpty();
  }

  public void setMaxUsers(int maxUsers) {
    this.maxUsers = maxUsers;
  }

  public List<SignTimeFrame> getTimeRepeats() {
    return timeRepeats;
  }

  public void setTimeRepeats(List<SignTimeFrame> timeRepeats) {
    this.timeRepeats = timeRepeats;
  }

  public int getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(int expireTime) {
    this.expireTime = expireTime;
  }

  public boolean isSupportOnlinePay() {
    return supportOnlinePay;
  }

  public void setSupportOnlinePay(boolean supportOnlinePay) {
    this.supportOnlinePay = supportOnlinePay;
  }

  public List<CardCost> getCardCosts() {
    return cardCosts;
  }

  public void setCardCosts(List<CardCost> cardCosts) {
    this.cardCosts = cardCosts;
  }

  public float getOnlinePayPrice() {
    return onlinePayPrice;
  }

  public void setOnlinePayPrice(float onlinePayPrice) {
    this.onlinePayPrice = onlinePayPrice;
  }


}
