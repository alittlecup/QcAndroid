package cn.qingchengfit.staffkit.dianping.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class GymFacility implements ISimpleChooseData, Parcelable {
  private int id;
  private String key;

  public int getId() {
    return id;
  }

  @Override public String getSign() {
    return String.valueOf(id);
  }

  @Override public String getText() {
    return GymFacilityConvert.convertFacilityKeyToString(key);
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof GymFacility)) {
      return false;
    }
    return ((GymFacility) obj).getId() == id;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.key);
  }

  public GymFacility() {
  }

  protected GymFacility(Parcel in) {
    this.id = in.readInt();
    this.key = in.readString();
  }

  public static final Parcelable.Creator<GymFacility> CREATOR =
      new Parcelable.Creator<GymFacility>() {
        @Override public GymFacility createFromParcel(Parcel source) {
          return new GymFacility(source);
        }

        @Override public GymFacility[] newArray(int size) {
          return new GymFacility[size];
        }
      };
}
