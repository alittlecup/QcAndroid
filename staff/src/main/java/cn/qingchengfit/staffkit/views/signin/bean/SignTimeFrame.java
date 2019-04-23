package cn.qingchengfit.staffkit.views.signin.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SignTimeFrame implements Parcelable {
  public String end;
  public String start;
  public String weekday_display;
  public int weekday;

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getWeekday_display() {
    return weekday_display;
  }

  public void setWeekday_display(String weekday_display) {
    this.weekday_display = weekday_display;
  }

  public int getWeekday() {
    return weekday;
  }

  public void setWeekday(int weekday) {
    this.weekday = weekday;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.end);
    dest.writeString(this.start);
    dest.writeString(this.weekday_display);
    dest.writeInt(this.weekday);
  }

  public SignTimeFrame() {
  }

  protected SignTimeFrame(Parcel in) {
    this.end = in.readString();
    this.start = in.readString();
    this.weekday_display = in.readString();
    this.weekday = in.readInt();
  }

  public static final Parcelable.Creator<SignTimeFrame> CREATOR =
      new Parcelable.Creator<SignTimeFrame>() {
        @Override public SignTimeFrame createFromParcel(Parcel source) {
          return new SignTimeFrame(source);
        }

        @Override public SignTimeFrame[] newArray(int size) {
          return new SignTimeFrame[size];
        }
      };
}
