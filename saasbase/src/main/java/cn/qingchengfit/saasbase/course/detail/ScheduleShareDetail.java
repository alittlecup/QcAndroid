package cn.qingchengfit.saasbase.course.detail;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ScheduleShareDetail implements Parcelable {
  @SerializedName("is_shared") private boolean shared;
  @SerializedName("is_public_shared") private boolean publicShared;
  @SerializedName("shared_text") private String sharedText;
  @SerializedName("can_shared_max_user") private int shareUser;
  @SerializedName("max_users") private int maxUsers;//最多人数
  @SerializedName("first_count") private int firstCount;//不知道这是什么意思，不过 max_users-first_count = 可邀请人数上限

  public boolean isShared() {
    return shared;
  }

  public void setShared(boolean shared) {
    this.shared = shared;
  }

  public boolean isPublicShared() {
    return publicShared;
  }

  public void setPublicShared(boolean publicShared) {
    this.publicShared = publicShared;
  }

  public String getSharedText() {
    return sharedText;
  }

  public void setSharedText(String sharedText) {
    this.sharedText = sharedText;
  }

  public int getShareUser() {
    return shareUser;
  }

  public void setShareUser(int shareUser) {
    this.shareUser = shareUser;
  }

  public int getMaxUsers() {
    return maxUsers;
  }

  public void setMaxUsers(int maxUsers) {
    this.maxUsers = maxUsers;
  }

  public int getFirstCount() {
    return firstCount;
  }

  public void setFirstCount(int firstCount) {
    this.firstCount = firstCount;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.shared ? (byte) 1 : (byte) 0);
    dest.writeByte(this.publicShared ? (byte) 1 : (byte) 0);
    dest.writeString(this.sharedText);
    dest.writeInt(this.shareUser);
    dest.writeInt(this.maxUsers);
    dest.writeInt(this.firstCount);
  }

  public ScheduleShareDetail() {
  }

  protected ScheduleShareDetail(Parcel in) {
    this.shared = in.readByte() != 0;
    this.publicShared = in.readByte() != 0;
    this.sharedText = in.readString();
    this.shareUser = in.readInt();
    this.maxUsers = in.readInt();
    this.firstCount = in.readInt();
  }

  public static final Parcelable.Creator<ScheduleShareDetail> CREATOR =
      new Parcelable.Creator<ScheduleShareDetail>() {
        @Override public ScheduleShareDetail createFromParcel(Parcel source) {
          return new ScheduleShareDetail(source);
        }

        @Override public ScheduleShareDetail[] newArray(int size) {
          return new ScheduleShareDetail[size];
        }
      };
}
