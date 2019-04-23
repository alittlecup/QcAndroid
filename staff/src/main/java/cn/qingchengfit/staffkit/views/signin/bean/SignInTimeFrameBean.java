package cn.qingchengfit.staffkit.views.signin.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author huangbaole
 *
 * {
 * "shop": {
 * "id": 1,
 * "name": "阳光上东店one"
 * },
 * "once_support_trial": false,
 * "card_costs": [],
 * "support_online_pay": true,
 * "trial_price": 1,
 * "online_pay_price": 200,
 * "max_users": 10,
 * "users_count": 0,
 * "support_trial": true,
 * "time_repeats": [],
 * "shop_checkin_expire_time": 60,
 * "trial_original_price": 10,
 * "created_at": "2018-11-08T17:43:23",
 * "id": 3
 * }
 */
public class SignInTimeFrameBean implements Parcelable {
  public CoachService shop;
  public String id;
  @SerializedName("once_support_trial") private boolean onceSupportTrial;

  @SerializedName("card_costs") private List<SignInCardCostBean.CardCost> cardCosts;

  @SerializedName("support_online_pay") private boolean supportOnlinePay;

  @SerializedName("trial_price") private float trialPrice;

  @SerializedName("online_pay_price") private float onlinePayPrice;

  @SerializedName("max_users") private int maxUsers;

  @SerializedName("users_count") private int usersCount;

  @SerializedName("support_trial") private boolean supportTrial;

  @SerializedName("time_repeats") private List<SignTimeFrame> timeRepeats;

  @SerializedName("shop_checkin_expire_time") private int expireTime;

  @SerializedName("trial_original_price") private float trialOriginalPrice;

  @SerializedName("created_at") private String createdTime;

  public CoachService getShop() {
    return shop;
  }

  public void setShop(CoachService shop) {
    this.shop = shop;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isOnceSupportTrial() {
    return onceSupportTrial;
  }

  public void setOnceSupportTrial(boolean onceSupportTrial) {
    this.onceSupportTrial = onceSupportTrial;
  }

  public List<SignInCardCostBean.CardCost> getCardCosts() {
    return cardCosts;
  }

  public void setCardCosts(List<SignInCardCostBean.CardCost> cardCosts) {
    this.cardCosts = cardCosts;
  }

  public boolean isSupportOnlinePay() {
    return supportOnlinePay;
  }

  public boolean isSupportCardPay() {
    return cardCosts != null && !cardCosts.isEmpty();
  }

  public void setSupportOnlinePay(boolean supportOnlinePay) {
    this.supportOnlinePay = supportOnlinePay;
  }

  public float getTrialPrice() {
    return trialPrice;
  }

  public void setTrialPrice(float trialPrice) {
    this.trialPrice = trialPrice;
  }

  public float getOnlinePayPrice() {
    return onlinePayPrice;
  }

  public void setOnlinePayPrice(float onlinePayPrice) {
    this.onlinePayPrice = onlinePayPrice;
  }

  public int getMaxUsers() {
    return maxUsers;
  }

  public void setMaxUsers(int maxUsers) {
    this.maxUsers = maxUsers;
  }

  public int getUsersCount() {
    return usersCount;
  }

  public void setUsersCount(int usersCount) {
    this.usersCount = usersCount;
  }

  public boolean isSupportTrial() {
    return supportTrial;
  }

  public void setSupportTrial(boolean supportTrial) {
    this.supportTrial = supportTrial;
  }

  public List<SignTimeFrame> getTimeRepeats() {
    return timeRepeats;
  }

  public String getTimeFrameWeekWithSplit(String split) {
    if (checkTimeRepeates()) {
      StringBuilder stringBuilder = new StringBuilder();
      for (SignTimeFrame frame : timeRepeats) {
        stringBuilder.append(frame.weekday_display).append(split);
      }
      return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
    return "";
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

  public float getTrialOriginalPrice() {
    return trialOriginalPrice;
  }

  public void setTrialOriginalPrice(float trialOriginalPrice) {
    this.trialOriginalPrice = trialOriginalPrice;
  }

  public String getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(String createdTime) {
    this.createdTime = createdTime;
  }

  public static Creator<SignInTimeFrameBean> getCREATOR() {
    return CREATOR;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.shop, flags);
    dest.writeString(this.id);
    dest.writeByte(this.onceSupportTrial ? (byte) 1 : (byte) 0);
    dest.writeTypedList(this.cardCosts);
    dest.writeByte(this.supportOnlinePay ? (byte) 1 : (byte) 0);
    dest.writeFloat(this.trialPrice);
    dest.writeFloat(this.onlinePayPrice);
    dest.writeInt(this.maxUsers);
    dest.writeInt(this.usersCount);
    dest.writeByte(this.supportTrial ? (byte) 1 : (byte) 0);
    dest.writeTypedList(this.timeRepeats);
    dest.writeInt(this.expireTime);
    dest.writeFloat(this.trialOriginalPrice);
    dest.writeString(this.createdTime);
  }

  public SignInTimeFrameBean() {
  }

  public String getStartTime() {
    if (checkTimeRepeates()) {
      return timeRepeats.get(0).start;
    }
    return "";
  }

  public boolean checkTimeRepeates() {
    return timeRepeats != null && !timeRepeats.isEmpty();
  }

  public String getEndTime() {
    if (checkTimeRepeates()) {
      return timeRepeats.get(0).end;
    }
    return "";
  }

  protected SignInTimeFrameBean(Parcel in) {
    this.shop = in.readParcelable(CoachService.class.getClassLoader());
    this.id = in.readString();
    this.onceSupportTrial = in.readByte() != 0;
    this.cardCosts = in.createTypedArrayList(SignInCardCostBean.CardCost.CREATOR);
    this.supportOnlinePay = in.readByte() != 0;
    this.trialPrice = in.readFloat();
    this.onlinePayPrice = in.readFloat();
    this.maxUsers = in.readInt();
    this.usersCount = in.readInt();
    this.supportTrial = in.readByte() != 0;
    this.timeRepeats = in.createTypedArrayList(SignTimeFrame.CREATOR);
    this.expireTime = in.readInt();
    this.trialOriginalPrice = in.readFloat();
    this.createdTime = in.readString();
  }

  public static final Parcelable.Creator<SignInTimeFrameBean> CREATOR =
      new Parcelable.Creator<SignInTimeFrameBean>() {
        @Override public SignInTimeFrameBean createFromParcel(Parcel source) {
          return new SignInTimeFrameBean(source);
        }

        @Override public SignInTimeFrameBean[] newArray(int size) {
          return new SignInTimeFrameBean[size];
        }
      };
}
