package cn.qingchengfit.staffkit.views.signin.zq.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/9/26.
 */

public class AccessBody implements Parcelable{

  public String name;
  public String device_id;
  public int behavior;
  public String start;
  public String end;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.device_id);
    dest.writeInt(this.behavior);
    dest.writeString(this.start);
    dest.writeString(this.end);
  }

  public AccessBody() {
  }

  protected AccessBody(Parcel in) {
    this.name = in.readString();
    this.device_id = in.readString();
    this.behavior = in.readInt();
    this.start = in.readString();
    this.end = in.readString();
  }

  public static final Creator<AccessBody> CREATOR = new Creator<AccessBody>() {
    @Override public AccessBody createFromParcel(Parcel source) {
      return new AccessBody(source);
    }

    @Override public AccessBody[] newArray(int size) {
      return new AccessBody[size];
    }
  };
}
