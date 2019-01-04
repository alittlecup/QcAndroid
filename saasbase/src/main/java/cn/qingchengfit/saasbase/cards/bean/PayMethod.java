package cn.qingchengfit.saasbase.cards.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * Created by fb on 2018/1/5.
 */

public class PayMethod implements Parcelable {

  public int payType;
  public String name;
  @DrawableRes public int icon;
  public boolean isPro =true;

  public PayMethod(int payType, String name, @DrawableRes int icon) {
    this.payType = payType;
    this.name = name;
    this.icon = icon;
  }


  public PayMethod(int payType, String name, @DrawableRes int icon, boolean isPro) {
    this(payType, name, icon);
    this.isPro = isPro;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.payType);
    dest.writeString(this.name);
    dest.writeInt(this.icon);
    dest.writeByte(this.isPro ? (byte) 1 : (byte) 0);
  }

  protected PayMethod(Parcel in) {
    this.payType = in.readInt();
    this.name = in.readString();
    this.icon = in.readInt();
    this.isPro = in.readByte() != 0;
  }

  public static final Creator<PayMethod> CREATOR = new Creator<PayMethod>() {
    @Override public PayMethod createFromParcel(Parcel source) {
      return new PayMethod(source);
    }

    @Override public PayMethod[] newArray(int size) {
      return new PayMethod[size];
    }
  };
}
