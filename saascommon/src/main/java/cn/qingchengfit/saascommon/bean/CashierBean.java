package cn.qingchengfit.saascommon.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CashierBean implements Parcelable {
  private String pay_trade_no="";
  private String out_trade_no="";
  private String url="";


  public String getPay_trade_no() {
    return pay_trade_no;
  }

  public void setPay_trade_no(String pay_trade_no) {
    this.pay_trade_no = pay_trade_no;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public CashierBean() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.pay_trade_no);
    dest.writeString(this.out_trade_no);
    dest.writeString(this.url);
  }

  protected CashierBean(Parcel in) {
    this.pay_trade_no = in.readString();
    this.out_trade_no = in.readString();
    this.url = in.readString();
  }

  public static final Creator<CashierBean> CREATOR = new Creator<CashierBean>() {
    @Override public CashierBean createFromParcel(Parcel source) {
      return new CashierBean(source);
    }

    @Override public CashierBean[] newArray(int size) {
      return new CashierBean[size];
    }
  };
}
