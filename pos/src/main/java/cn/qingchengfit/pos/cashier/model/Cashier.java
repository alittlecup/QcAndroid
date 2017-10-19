package cn.qingchengfit.pos.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/10/18.
 */

public class Cashier implements Parcelable{

  public String id;
  public String avatar;
  public String username;
  public String phone;
  public int gender;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.avatar);
    dest.writeString(this.username);
    dest.writeString(this.phone);
    dest.writeInt(this.gender);
  }

  public Cashier() {
  }

  protected Cashier(Parcel in) {
    this.id = in.readString();
    this.avatar = in.readString();
    this.username = in.readString();
    this.phone = in.readString();
    this.gender = in.readInt();
  }

  public static final Creator<Cashier> CREATOR = new Creator<Cashier>() {
    @Override public Cashier createFromParcel(Parcel source) {
      return new Cashier(source);
    }

    @Override public Cashier[] newArray(int size) {
      return new Cashier[size];
    }
  };
}
