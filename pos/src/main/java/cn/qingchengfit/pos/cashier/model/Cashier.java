package cn.qingchengfit.pos.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.User;

/**
 * Created by fb on 2017/10/18.
 */

public class Cashier implements Parcelable{

  public String id;
  public User user;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeParcelable(this.user, flags);
  }

  public Cashier() {
  }

  protected Cashier(Parcel in) {
    this.id = in.readString();
    this.user = in.readParcelable(User.class.getClassLoader());
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
