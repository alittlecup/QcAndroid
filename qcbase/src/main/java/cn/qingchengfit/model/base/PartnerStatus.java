package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;

public class PartnerStatus implements Parcelable {

  public boolean alipay;
  public boolean meituan;
  public boolean taobao;
  public boolean koubei;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.alipay ? (byte) 1 : (byte) 0);
    dest.writeByte(this.meituan ? (byte) 1 : (byte) 0);
    dest.writeByte(this.taobao ? (byte) 1 : (byte) 0);
    dest.writeByte(this.koubei ? (byte) 1 : (byte) 0);
  }

  public PartnerStatus() {
  }

  protected PartnerStatus(Parcel in) {
    this.alipay = in.readByte() != 0;
    this.meituan = in.readByte() != 0;
    this.taobao = in.readByte() != 0;
    this.koubei = in.readByte() != 0;
  }

  public static final Creator<PartnerStatus> CREATOR = new Creator<PartnerStatus>() {
    @Override public PartnerStatus createFromParcel(Parcel source) {
      return new PartnerStatus(source);
    }

    @Override public PartnerStatus[] newArray(int size) {
      return new PartnerStatus[size];
    }
  };
}