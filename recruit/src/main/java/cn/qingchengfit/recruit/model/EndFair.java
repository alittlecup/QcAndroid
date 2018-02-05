package cn.qingchengfit.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/7/17.
 */

public class EndFair implements Parcelable{

  public String id;
  public String name;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
  }

  public EndFair() {
  }

  protected EndFair(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
  }

  public static final Creator<EndFair> CREATOR = new Creator<EndFair>() {
    @Override public EndFair createFromParcel(Parcel source) {
      return new EndFair(source);
    }

    @Override public EndFair[] newArray(int size) {
      return new EndFair[size];
    }
  };
}
