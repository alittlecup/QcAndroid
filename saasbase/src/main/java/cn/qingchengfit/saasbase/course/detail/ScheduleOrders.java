package cn.qingchengfit.saasbase.course.detail;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.response.QcListData;
import java.util.List;

public class ScheduleOrders extends QcListData implements Parcelable {
  public List<ScheduleOrder> orders;

  public static class ScheduleOrder implements Parcelable {
    private String id;
    private String remarks;
    private User user;
    private int count;

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    /**
     * STATUS_CREATE = 0
     * STATUS_DONE = 1
     * STATUS_CANCEL = 2
     * STATUS_FAIL = 3
     * STATUS_SIGN_IN = 4
     *
     * STATUS_CHOICES = (
     * (STATUS_CREATE, '已预约'),
     * (STATUS_DONE, '已完成'),
     * (STATUS_CANCEL, '已取消'),
     * (STATUS_FAIL, '失败'),
     * (STATUS_SIGN_IN, '已签课')
     * )
     */
    private int status;

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

    public ScheduleOrder() {
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.id);
      dest.writeString(this.remarks);
      dest.writeParcelable(this.user, flags);
      dest.writeInt(this.count);
      dest.writeInt(this.status);
    }

    protected ScheduleOrder(Parcel in) {
      this.id = in.readString();
      this.remarks = in.readString();
      this.user = in.readParcelable(User.class.getClassLoader());
      this.count = in.readInt();
      this.status = in.readInt();
    }

    public static final Creator<ScheduleOrder> CREATOR = new Creator<ScheduleOrder>() {
      @Override public ScheduleOrder createFromParcel(Parcel source) {
        return new ScheduleOrder(source);
      }

      @Override public ScheduleOrder[] newArray(int size) {
        return new ScheduleOrder[size];
      }
    };

  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(this.orders);
  }

  public ScheduleOrders() {
  }

  protected ScheduleOrders(Parcel in) {
    this.orders = in.createTypedArrayList(ScheduleOrder.CREATOR);
  }

  public static final Parcelable.Creator<ScheduleOrders> CREATOR =
      new Parcelable.Creator<ScheduleOrders>() {
        @Override public ScheduleOrders createFromParcel(Parcel source) {
          return new ScheduleOrders(source);
        }

        @Override public ScheduleOrders[] newArray(int size) {
          return new ScheduleOrders[size];
        }
      };
}
