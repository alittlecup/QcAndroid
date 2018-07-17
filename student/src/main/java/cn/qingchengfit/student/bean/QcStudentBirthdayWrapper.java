package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class QcStudentBirthdayWrapper implements Parcelable {
  private int total_count;
  private List<QcStudentWithUsers> birthday;

  public int getTotal_count() {
    return total_count;
  }

  public void setTotal_count(int total_count) {
    this.total_count = total_count;
  }

  public List<QcStudentWithUsers> getBirthday() {
    return birthday;
  }

  public void setBirthday(List<QcStudentWithUsers> birthday) {
    this.birthday = birthday;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.total_count);
    dest.writeTypedList(this.birthday);
  }

  public QcStudentBirthdayWrapper() {
  }

  protected QcStudentBirthdayWrapper(Parcel in) {
    this.total_count = in.readInt();
    this.birthday = in.createTypedArrayList(QcStudentWithUsers.CREATOR);
  }

  public static final Parcelable.Creator<QcStudentBirthdayWrapper> CREATOR =
      new Parcelable.Creator<QcStudentBirthdayWrapper>() {
        @Override public QcStudentBirthdayWrapper createFromParcel(Parcel source) {
          return new QcStudentBirthdayWrapper(source);
        }

        @Override public QcStudentBirthdayWrapper[] newArray(int size) {
          return new QcStudentBirthdayWrapper[size];
        }
      };
}
