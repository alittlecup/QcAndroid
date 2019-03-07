package cn.qingchengfit.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GymSettingInfo implements Parcelable {
  public String gym_type;
  public boolean open_checkin;
  public boolean has_team;
  public boolean has_teacher;
  public boolean has_private;
  public boolean has_mall;
  public boolean skip_window;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.gym_type);
    dest.writeByte(this.open_checkin ? (byte) 1 : (byte) 0);
    dest.writeByte(this.has_team ? (byte) 1 : (byte) 0);
    dest.writeByte(this.has_teacher ? (byte) 1 : (byte) 0);
    dest.writeByte(this.has_private ? (byte) 1 : (byte) 0);
    dest.writeByte(this.has_mall ? (byte) 1 : (byte) 0);
    dest.writeByte(this.skip_window ? (byte) 1 : (byte) 0);
  }

  public GymSettingInfo() {
  }

  protected GymSettingInfo(Parcel in) {
    this.gym_type = in.readString();
    this.open_checkin = in.readByte() != 0;
    this.has_team = in.readByte() != 0;
    this.has_teacher = in.readByte() != 0;
    this.has_private = in.readByte() != 0;
    this.has_mall = in.readByte() != 0;
    this.skip_window = in.readByte() != 0;
  }

  public static final Parcelable.Creator<GymSettingInfo> CREATOR =
      new Parcelable.Creator<GymSettingInfo>() {
        @Override public GymSettingInfo createFromParcel(Parcel source) {
          return new GymSettingInfo(source);
        }

        @Override public GymSettingInfo[] newArray(int size) {
          return new GymSettingInfo[size];
        }
      };
}
