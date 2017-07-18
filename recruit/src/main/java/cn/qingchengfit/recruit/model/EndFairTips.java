package cn.qingchengfit.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/7/17.
 */

public class EndFairTips implements Parcelable {

  public String id;
  public String name;
  public EndFair fair;

  public EndFairTips() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeParcelable(this.fair, flags);
  }

  protected EndFairTips(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.fair = in.readParcelable(EndFair.class.getClassLoader());
  }

  public static final Creator<EndFairTips> CREATOR = new Creator<EndFairTips>() {
    @Override public EndFairTips createFromParcel(Parcel source) {
      return new EndFairTips(source);
    }

    @Override public EndFairTips[] newArray(int size) {
      return new EndFairTips[size];
    }
  };
}

