package cn.qingchengfit.di.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/10/16.
 */

public class SuperUser implements Parcelable {

  public String username;
  public String phone;

  public SuperUser(String username, String phone) {
    this.username = username;
    this.phone = phone;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.username);
    dest.writeString(this.phone);
  }

  protected SuperUser(Parcel in) {
    this.username = in.readString();
    this.phone = in.readString();
  }

  public static final Creator<SuperUser> CREATOR = new Creator<SuperUser>() {
    @Override public SuperUser createFromParcel(Parcel source) {
      return new SuperUser(source);
    }

    @Override public SuperUser[] newArray(int size) {
      return new SuperUser[size];
    }
  };
}
