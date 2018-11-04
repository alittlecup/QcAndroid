package cn.qingchengfit.writeoff.vo.impl;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.TextureView;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;

public class TicketPostBody implements Parcelable {
  private String e_code;

  public int getUsedType() {
    return used_type;
  }

  private int used_type;
  private String course_id;
  private String course_name;
  private String teacher_id;
  private String teacher_name;
  private String used_at;
  private int people_num;
  private String phone;
  private String area_code;
  private int gender = -1;
  private String id;

  public void setId(String id) {
    this.id = id;
  }

  public void setModel(String model) {
    this.model = model;
  }

  private String model;

  public int getUsed_type() {
    return used_type;
  }

  public void setUsed_type(int used_type) {
    this.used_type = used_type;
  }

  public String getCourse_name() {
    return course_name;
  }

  public void setCourse_name(String course_name) {
    this.course_name = course_name;
  }

  public String getTeacher_name() {
    return teacher_name;
  }

  public void setTeacher_name(String teacher_name) {
    this.teacher_name = teacher_name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getArea_code() {
    return area_code;
  }

  public void setArea_code(String area_code) {
    this.area_code = area_code;
  }

  public int isGender() {
    return gender;
  }

  public void setGender(int gender) {
    this.gender = gender;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  private String username;

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

  public TicketPostBody() {
  }

  public boolean checkBody() {
    if (TextUtils.isEmpty(e_code)) {
      showToast("请输入核销码");
      return false;
    } else if (used_type == 0) {
      showToast("请选择课程类型");
      return false;
    } else if (TextUtils.isEmpty(course_name) || TextUtils.isEmpty(course_id)) {
      showToast("请选择课程种类");
      return false;
    } else if (TextUtils.isEmpty(teacher_id) || TextUtils.isEmpty(teacher_name)) {
      showToast("请选择教练");
      return false;
    } else if (TextUtils.isEmpty(used_at)) {
      showToast("请选择上课时间");
      return false;
    } else if (people_num == 0) {
      showToast("请填写约课人数");
      return false;
    } else if (TextUtils.isEmpty(username)) {
      showToast("请填写体验者姓名");
      return false;
    } else if (gender == -1) {
      showToast("请选择体验者性别");
      return false;
    } else if (!StringUtils.checkPhoneNumber(phone, "+866".equals(area_code))) {
      return false;
    }
    return true;
  }

  private void showToast(String text) {
    ToastUtils.show(text);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.e_code);
    dest.writeInt(this.used_type);
    dest.writeString(this.course_id);
    dest.writeString(this.course_name);
    dest.writeString(this.teacher_id);
    dest.writeString(this.teacher_name);
    dest.writeString(this.used_at);
    dest.writeInt(this.people_num);
    dest.writeString(this.phone);
    dest.writeString(this.area_code);
    dest.writeInt(this.gender);
    dest.writeString(this.id);
    dest.writeString(this.model);
    dest.writeString(this.username);
  }

  protected TicketPostBody(Parcel in) {
    this.e_code = in.readString();
    this.used_type = in.readInt();
    this.course_id = in.readString();
    this.course_name = in.readString();
    this.teacher_id = in.readString();
    this.teacher_name = in.readString();
    this.used_at = in.readString();
    this.people_num = in.readInt();
    this.phone = in.readString();
    this.area_code = in.readString();
    this.gender = in.readInt();
    this.id = in.readString();
    this.model = in.readString();
    this.username = in.readString();
  }

  public static final Creator<TicketPostBody> CREATOR = new Creator<TicketPostBody>() {
    @Override public TicketPostBody createFromParcel(Parcel source) {
      return new TicketPostBody(source);
    }

    @Override public TicketPostBody[] newArray(int size) {
      return new TicketPostBody[size];
    }
  };
}
