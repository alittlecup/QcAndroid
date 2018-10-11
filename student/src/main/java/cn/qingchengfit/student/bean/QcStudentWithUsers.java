package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import java.util.List;

public class QcStudentWithUsers implements Parcelable {
  private  String date;
  private int count;
  private List<QcStudentBeanWithFollow> users;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<QcStudentBeanWithFollow> getUsers() {
    return users;
  }

  public void setUsers(List<QcStudentBeanWithFollow> users) {
    this.users = users;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.date);
    dest.writeInt(this.count);
    dest.writeTypedList(this.users);
  }

  public QcStudentWithUsers() {
  }

  protected QcStudentWithUsers(Parcel in) {
    this.date = in.readString();
    this.count = in.readInt();
    this.users = in.createTypedArrayList(QcStudentBeanWithFollow.CREATOR);
  }

  public static final Parcelable.Creator<QcStudentWithUsers> CREATOR =
      new Parcelable.Creator<QcStudentWithUsers>() {
        @Override public QcStudentWithUsers createFromParcel(Parcel source) {
          return new QcStudentWithUsers(source);
        }

        @Override public QcStudentWithUsers[] newArray(int size) {
          return new QcStudentWithUsers[size];
        }
      };
}
