package cn.qingchengfit.student.view.base;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.Constants;
import cn.qingchengfit.utils.DateUtils;

public class StudentBaseInfoBean implements Parcelable {

  private String head;
  private String phone;
  private String birthday;
  private String address;
  private String register;

  public StudentBaseInfoBean(String head, String phone, String birthday, String address,
      String register) {
    this.head = head;
    this.phone = phone;
    this.birthday = birthday;
    this.address = address;
    this.register = register;
  }

  public String getHead() {
    return (head == null || head.length() == 0) ? Constants.AVATAR_STUDENT_MALE : head;
  }

  public String getPhone() {
    return phone;
  }

  public String getBirthday() {
    return (birthday == null || birthday.length() == 0) ? "暂无" : birthday;
  }

  public String getAddress() {
    return address;
  }

  public String getRegister() {
    return DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(register));
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.head);
    dest.writeString(this.phone);
    dest.writeString(this.birthday);
    dest.writeString(this.address);
    dest.writeString(this.register);
  }

  public StudentBaseInfoBean() {
  }

  protected StudentBaseInfoBean(Parcel in) {
    this.head = in.readString();
    this.phone = in.readString();
    this.birthday = in.readString();
    this.address = in.readString();
    this.register = in.readString();
  }

  public static final Creator<StudentBaseInfoBean> CREATOR = new Creator<StudentBaseInfoBean>() {
    @Override public StudentBaseInfoBean createFromParcel(Parcel source) {
      return new StudentBaseInfoBean(source);
    }

    @Override public StudentBaseInfoBean[] newArray(int size) {
      return new StudentBaseInfoBean[size];
    }
  };
}
