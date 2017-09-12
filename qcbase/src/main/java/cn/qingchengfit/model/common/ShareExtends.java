package cn.qingchengfit.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/9/11.
 */

public class ShareExtends implements Parcelable {
  public String type;
  public String key;
  public String title;
  public String icon;
  public String desc;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.type);
    dest.writeString(this.key);
    dest.writeString(this.title);
    dest.writeString(this.icon);
    dest.writeString(this.desc);
  }

  public ShareExtends() {
  }

  protected ShareExtends(Parcel in) {
    this.type = in.readString();
    this.key = in.readString();
    this.title = in.readString();
    this.icon = in.readString();
    this.desc = in.readString();
  }

  public static final Creator<ShareExtends> CREATOR = new Creator<ShareExtends>() {
    @Override public ShareExtends createFromParcel(Parcel source) {
      return new ShareExtends(source);
    }

    @Override public ShareExtends[] newArray(int size) {
      return new ShareExtends[size];
    }
  };
}
