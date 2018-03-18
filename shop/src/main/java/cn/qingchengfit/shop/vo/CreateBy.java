package cn.qingchengfit.shop.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangbaole on 2018/1/19.
 */

public final class CreateBy implements Parcelable {
  private String username;
  private int id;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.username);
    dest.writeInt(this.id);
  }

  public CreateBy() {
  }

  protected CreateBy(Parcel in) {
    this.username = in.readString();
    this.id = in.readInt();
  }

  public static final Parcelable.Creator<CreateBy> CREATOR = new Parcelable.Creator<CreateBy>() {
    @Override public CreateBy createFromParcel(Parcel source) {
      return new CreateBy(source);
    }

    @Override public CreateBy[] newArray(int size) {
      return new CreateBy[size];
    }
  };
}
