package cn.qingchengfit.writeoff.vo.impl;

import android.os.Parcel;
import android.os.Parcelable;

public class TicketPostBody implements Parcelable {
  private String e_code;

  public int getUsedType() {
    return used_type;
  }

  private int used_type;
  private String course_id;
  private String teacher_id;
  private String used_at;
  private int people_num;

  public String getE_code() {
    return e_code;
  }

  public void setE_code(String e_code) {
    this.e_code = e_code;
  }

  public boolean isPrivate() {
    return used_type == 2;
  }

  public String getCourse_id() {
    return course_id;
  }

  public void setCourse_id(String course_id) {
    this.course_id = course_id;
  }

  public String getTeacher_id() {
    return teacher_id;
  }

  public void setTeacher_id(String teacher_id) {
    this.teacher_id = teacher_id;
  }

  public String getUsed_at() {
    return used_at;
  }

  public void setUsed_at(String used_at) {
    this.used_at = used_at;
  }

  public int getPeople_num() {
    return people_num;
  }

  public void setPeople_num(int people_num) {
    this.people_num = people_num;
  }

  public void setBatchType(boolean isPrivate) {
    used_type = (isPrivate ? 2 : 1);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.e_code);
    dest.writeInt(this.used_type);
    dest.writeString(this.course_id);
    dest.writeString(this.teacher_id);
    dest.writeString(this.used_at);
    dest.writeInt(this.people_num);
  }

  public TicketPostBody() {
  }

  protected TicketPostBody(Parcel in) {
    this.e_code = in.readString();
    this.used_type = in.readInt();
    this.course_id = in.readString();
    this.teacher_id = in.readString();
    this.used_at = in.readString();
    this.people_num = in.readInt();
  }

  public static final Parcelable.Creator<TicketPostBody> CREATOR =
      new Parcelable.Creator<TicketPostBody>() {
        @Override public TicketPostBody createFromParcel(Parcel source) {
          return new TicketPostBody(source);
        }

        @Override public TicketPostBody[] newArray(int size) {
          return new TicketPostBody[size];
        }
      };
}
