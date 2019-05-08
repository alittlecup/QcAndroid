package cn.qingchengfit.staffkit.views.signin.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class UserCheckInOrder implements Parcelable {
  /**
   * checkin	Objects
   * 用户未签出的签到（没有的话返回空对象）
   *
   * id	Number
   * 签到ID
   *
   * is_expire	Boolean
   * 是否过期
   *
   * created_at	String
   * 签到创建时间
   *
   * expire_at	String
   * 签到过期时间
   */
  private int id;
  @SerializedName("is_expire") private boolean isExpire;
  @SerializedName("created_at") private String createTime;
  @SerializedName("expire_at") private String expireTime;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isExpire() {
    return isExpire;
  }

  public void setExpire(boolean expire) {
    isExpire = expire;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(String expireTime) {
    this.expireTime = expireTime;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeByte(this.isExpire ? (byte) 1 : (byte) 0);
    dest.writeString(this.createTime);
    dest.writeString(this.expireTime);
  }

  public UserCheckInOrder() {
  }

  protected UserCheckInOrder(Parcel in) {
    this.id = in.readInt();
    this.isExpire = in.readByte() != 0;
    this.createTime = in.readString();
    this.expireTime = in.readString();
  }

  public static final Parcelable.Creator<UserCheckInOrder> CREATOR =
      new Parcelable.Creator<UserCheckInOrder>() {
        @Override public UserCheckInOrder createFromParcel(Parcel source) {
          return new UserCheckInOrder(source);
        }

        @Override public UserCheckInOrder[] newArray(int size) {
          return new UserCheckInOrder[size];
        }
      };
}
