package cn.qingchengfit.saasbase.course.detail;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.response.QcListData;
import java.util.List;

public class ScheduleOrders extends QcListData {
  public List<ScheduleOrder> orders;

  public static class ScheduleOrder implements Parcelable {
    private String id;
    private String remarks;
    private User user;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getRemarks() {
      return remarks;
    }

    public void setRemarks(String remarks) {
      this.remarks = remarks;
    }

    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.id);
      dest.writeString(this.remarks);
      dest.writeParcelable(this.user, flags);
    }

    public ScheduleOrder() {
    }

    protected ScheduleOrder(Parcel in) {
      this.id = in.readString();
      this.remarks = in.readString();
      this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<ScheduleOrder> CREATOR =
        new Parcelable.Creator<ScheduleOrder>() {
          @Override public ScheduleOrder createFromParcel(Parcel source) {
            return new ScheduleOrder(source);
          }

          @Override public ScheduleOrder[] newArray(int size) {
            return new ScheduleOrder[size];
          }
        };
  }
}
