package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import java.util.List;

public class InactiveStat implements Parcelable {
  private int users_count;
  private StatData inactive;

  public int getUsers_count() {
    return users_count;
  }

  public void setUsers_count(int users_count) {
    this.users_count = users_count;
  }

  public StatData getInactive() {
    return inactive;
  }

  public void setInactive(StatData inactive) {
    this.inactive = inactive;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.users_count);
    dest.writeParcelable(this.inactive, flags);
  }

  public InactiveStat() {
  }

  protected InactiveStat(Parcel in) {
    this.users_count = in.readInt();
    this.inactive = in.readParcelable(StatData.class.getClassLoader());
  }

  public static final Creator<InactiveStat> CREATOR = new Creator<InactiveStat>() {
    @Override public InactiveStat createFromParcel(Parcel source) {
      return new InactiveStat(source);
    }

    @Override public InactiveStat[] newArray(int size) {
      return new InactiveStat[size];
    }
  };
}
