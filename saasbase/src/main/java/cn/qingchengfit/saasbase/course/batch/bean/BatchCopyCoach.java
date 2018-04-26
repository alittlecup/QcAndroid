package cn.qingchengfit.saasbase.course.batch.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.common.ICommonUser;

/**
 * Created by fb on 2018/4/18.
 */

public class BatchCopyCoach implements ICommonUser, Parcelable {

  public String id;
  public String username;
  public String avatar;
  public int gender;

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.username = name;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public void setGender(int gender) {
    this.gender = gender;
  }

  @Override public String getAvatar() {
    return avatar;
  }

  @Override public String getTitle() {
    return username;
  }

  @Override public String getSubTitle() {
    return "";
  }

  @Override public String getContent() {
    return "";
  }

  @Override public String getId() {
    return id;
  }

  @Override public String getRight() {
    return null;
  }

  @Override public int getRightColor() {
    return 0;
  }

  @Override public int getGender() {
    return gender;
  }

  @Override public boolean filter(String s) {
    return username.contains(s);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.username);
    dest.writeString(this.avatar);
    dest.writeInt(this.gender);
  }

  public BatchCopyCoach() {
  }

  protected BatchCopyCoach(Parcel in) {
    this.id = in.readString();
    this.username = in.readString();
    this.avatar = in.readString();
    this.gender = in.readInt();
  }

  public static final Creator<BatchCopyCoach> CREATOR = new Creator<BatchCopyCoach>() {
    @Override public BatchCopyCoach createFromParcel(Parcel source) {
      return new BatchCopyCoach(source);
    }

    @Override public BatchCopyCoach[] newArray(int size) {
      return new BatchCopyCoach[size];
    }
  };

  @Override public boolean equals(Object obj) {
    return (obj instanceof BatchCopyCoach) && ((BatchCopyCoach)obj).id.equals(id);
  }

  @Override public int hashCode() {
    return Integer.parseInt(id);
  }
}
