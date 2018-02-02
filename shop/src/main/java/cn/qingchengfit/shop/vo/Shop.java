package cn.qingchengfit.shop.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangbaole on 2018/1/19.
 */

public class Shop implements Parcelable {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
  }

  public Shop() {
  }

  protected Shop(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
  }

  public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
    @Override public Shop createFromParcel(Parcel source) {
      return new Shop(source);
    }

    @Override public Shop[] newArray(int size) {
      return new Shop[size];
    }
  };
}
