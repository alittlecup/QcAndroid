package cn.qingchengfit.checkout.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckoutBill implements Parcelable, OrderListItemData {

  /**
   * id : ID
   * price : 金额
   * channel : 支付方式
   * balance_at : 支付时间
   * remarks : 备注
   */

  private String id;
  private String price;
  private String channel;
  private String balance_at;
  private String remarks;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getBalance_at() {
    return balance_at;
  }

  public void setBalance_at(String balance_at) {
    this.balance_at = balance_at;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.price);
    dest.writeString(this.channel);
    dest.writeString(this.balance_at);
    dest.writeString(this.remarks);
  }

  public CheckoutBill() {
  }

  protected CheckoutBill(Parcel in) {
    this.id = in.readString();
    this.price = in.readString();
    this.channel = in.readString();
    this.balance_at = in.readString();
    this.remarks = in.readString();
  }

  public static final Parcelable.Creator<CheckoutBill> CREATOR =
      new Parcelable.Creator<CheckoutBill>() {
        @Override public CheckoutBill createFromParcel(Parcel source) {
          return new CheckoutBill(source);
        }

        @Override public CheckoutBill[] newArray(int size) {
          return new CheckoutBill[size];
        }
      };

  @Override public int getType() {
    if ("WEIXIN".equals(channel)) {
      return PayChannel.WEIXIN_QRCODE;
    } else {
      return PayChannel.ALIPAY_QRCODE;
    }
  }

  @Override public String getOrderMoney() {
    return price;
  }

  @Override public String getOrderCreateDate() {
    return balance_at;
  }
}
