package cn.qingchengfit.writeoff.vo.impl;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class TicketInnerTempObject implements Parcelable {
  public String id;
  @SerializedName(value = "name",alternate = "username")
  public String name;

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

  public TicketInnerTempObject() {
  }

  protected TicketInnerTempObject(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
  }

  public static final Parcelable.Creator<TicketInnerTempObject> CREATOR =
      new Parcelable.Creator<TicketInnerTempObject>() {
        @Override public TicketInnerTempObject createFromParcel(Parcel source) {
          return new TicketInnerTempObject(source);
        }

        @Override public TicketInnerTempObject[] newArray(int size) {
          return new TicketInnerTempObject[size];
        }
      };
}
