package cn.qingchengfit.gym.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class GymPosition implements Parcelable {
  public String id;
  public String shop_id;
  @SerializedName(value = "name",alternate = "position")
  public String name;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.shop_id);
    dest.writeString(this.name);
  }

  public GymPosition() {
  }

  protected GymPosition(Parcel in) {
    this.id = in.readString();
    this.shop_id = in.readString();
    this.name = in.readString();
  }

  public static final Parcelable.Creator<GymPosition> CREATOR =
      new Parcelable.Creator<GymPosition>() {
        @Override public GymPosition createFromParcel(Parcel source) {
          return new GymPosition(source);
        }

        @Override public GymPosition[] newArray(int size) {
          return new GymPosition[size];
        }
      };
}
