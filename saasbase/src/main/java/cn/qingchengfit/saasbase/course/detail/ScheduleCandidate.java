package cn.qingchengfit.saasbase.course.detail;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.User;
import com.google.gson.annotations.SerializedName;

public class ScheduleCandidate implements Parcelable {
  private String id;
  @SerializedName("created_at")
  private String createAt;
  private User user;
  /**
   * 已订阅           status = 1
   * 已取消预约    status = 2
   * 预约成功        statue = 3
   * 已过期            status = 1 && 已过期（根据schedule.start和当前事件判断）优先级最高
   */

  private int status;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreateAt() {
    return createAt;
  }

  public void setCreateAt(String createAt) {
    this.createAt = createAt;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.createAt);
    dest.writeParcelable(this.user, flags);
    dest.writeInt(this.status);
  }

  public ScheduleCandidate() {
  }

  protected ScheduleCandidate(Parcel in) {
    this.id = in.readString();
    this.createAt = in.readString();
    this.user = in.readParcelable(User.class.getClassLoader());
    this.status = in.readInt();
  }

  public static final Parcelable.Creator<ScheduleCandidate> CREATOR =
      new Parcelable.Creator<ScheduleCandidate>() {
        @Override public ScheduleCandidate createFromParcel(Parcel source) {
          return new ScheduleCandidate(source);
        }

        @Override public ScheduleCandidate[] newArray(int size) {
          return new ScheduleCandidate[size];
        }
      };
}
