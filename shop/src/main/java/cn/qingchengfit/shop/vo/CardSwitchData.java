package cn.qingchengfit.shop.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class CardSwitchData implements Parcelable {
  private String id;
  private String name;

  public String getId() {
    return id;
  }

  public void setId(String id) {
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
    dest.writeString(this.id);
    dest.writeString(this.name);
  }

  public CardSwitchData() {
  }

  protected CardSwitchData(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
  }

  public static final Parcelable.Creator<CardSwitchData> CREATOR =
      new Parcelable.Creator<CardSwitchData>() {
        @Override public CardSwitchData createFromParcel(Parcel source) {
          return new CardSwitchData(source);
        }

        @Override public CardSwitchData[] newArray(int size) {
          return new CardSwitchData[size];
        }
      };
}
