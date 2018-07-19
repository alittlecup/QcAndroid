package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import java.util.List;

public class InactiveStat implements Parcelable {
  private int users_count;
  private List<InactiveBean> inactive;

  public int getUsers_count() {
    return users_count;
  }

  public void setUsers_count(int users_count) {
    this.users_count = users_count;
  }

  public List<InactiveBean> getInactive() {
    return inactive;
  }

  public void setInactive(List<InactiveBean> inactive) {
    this.inactive = inactive;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.users_count);
    dest.writeTypedList(this.inactive);
  }

  public InactiveStat() {
  }

  protected InactiveStat(Parcel in) {
    this.users_count = in.readInt();
    this.inactive = in.createTypedArrayList(InactiveBean.CREATOR);
  }

  public static final Parcelable.Creator<InactiveStat> CREATOR =
      new Parcelable.Creator<InactiveStat>() {
        @Override public InactiveStat createFromParcel(Parcel source) {
          return new InactiveStat(source);
        }

        @Override public InactiveStat[] newArray(int size) {
          return new InactiveStat[size];
        }
      };
}
