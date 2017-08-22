package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/7/13.
 */

public class StatRule implements Parcelable{

  public boolean checkin;
  public boolean attendance;
  public boolean private_course;
  public boolean together;
  public boolean group_course;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.checkin ? (byte) 1 : (byte) 0);
    dest.writeByte(this.attendance ? (byte) 1 : (byte) 0);
    dest.writeByte(this.private_course ? (byte) 1 : (byte) 0);
    dest.writeByte(this.together ? (byte) 1 : (byte) 0);
    dest.writeByte(this.group_course ? (byte) 1 : (byte) 0);
  }

  public StatRule() {
  }

  protected StatRule(Parcel in) {
    this.checkin = in.readByte() != 0;
    this.attendance = in.readByte() != 0;
    this.private_course = in.readByte() != 0;
    this.together = in.readByte() != 0;
    this.group_course = in.readByte() != 0;
  }

  public static final Creator<StatRule> CREATOR = new Creator<StatRule>() {
    @Override public StatRule createFromParcel(Parcel source) {
      return new StatRule(source);
    }

    @Override public StatRule[] newArray(int size) {
      return new StatRule[size];
    }
  };
}
