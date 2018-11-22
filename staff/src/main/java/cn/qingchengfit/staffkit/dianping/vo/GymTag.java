package cn.qingchengfit.staffkit.dianping.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class GymTag implements Parcelable, ISimpleChooseData {

  /**
   * id : 236350
   * name : 瑜伽
   */

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

  public GymTag() {
  }

  protected GymTag(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
  }

  public static final Parcelable.Creator<GymTag> CREATOR = new Parcelable.Creator<GymTag>() {
    @Override public GymTag createFromParcel(Parcel source) {
      return new GymTag(source);
    }

    @Override public GymTag[] newArray(int size) {
      return new GymTag[size];
    }
  };

  @Override public String getSign() {
    return String.valueOf(id);
  }

  @Override public String getText() {
    return name;
  }
}
