package cn.qingchengfit.checkout.bean;

import android.os.Parcel;

public class TestOrderData implements OrderListItemData {
  private String date = "2-18-12-12";
  private int type = PayChannel.ALIPAY_QRCODE;
  private String money = "1000";

  @Override public int getType() {
    return type;
  }

  @Override public String getOrderMoney() {
    return money;
  }

  @Override public String getOrderCreateDate() {
    return date;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.date);
    dest.writeInt(this.type);
    dest.writeString(this.money);
  }

  public TestOrderData(@PayChannel int type) {
    this.type = type;
  }
  public TestOrderData(){}
  protected TestOrderData(Parcel in) {
    this.date = in.readString();
    this.type = in.readInt();
    this.money = in.readString();
  }

  public static final Creator<TestOrderData> CREATOR = new Creator<TestOrderData>() {
    @Override public TestOrderData createFromParcel(Parcel source) {
      return new TestOrderData(source);
    }

    @Override public TestOrderData[] newArray(int size) {
      return new TestOrderData[size];
    }
  };
}
