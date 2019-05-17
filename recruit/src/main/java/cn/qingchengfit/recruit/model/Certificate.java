package cn.qingchengfit.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/6/10.
 */

public class Certificate implements Cloneable, Parcelable {
  public static final Creator<Certificate> CREATOR = new Creator<Certificate>() {
    @Override public Certificate createFromParcel(Parcel source) {
      return new Certificate(source);
    }

    @Override public Certificate[] newArray(int size) {
      return new Certificate[size];
    }
  };
  /**
   * date_of_issue : 2015-09-16T15:08:00
   * coach : {"id":6}
   * username : 青橙科技
   * photo : https://img.qingchengfit.cn/21d3bcb5600f8b2a005cdd40c57d0c4d.png
   * grade : 100
   * organization : {}
   * created_at : 2015-09-16T15:08:00
   * type : 1
   * id : 1
   * is_authenticated : true
   */

  public String date_of_issue;
  public Staff coach;
  public String name;
  public String project_name;
  public String certificate_name;
  public String photo;
  public String grade;
  public String created_at;
  public String meeting_start;
  public String start;
  public String end;
  public String organization_id;
  public Organization organization;
  public int type;
  public String id;
  public Boolean is_authenticated;
  public Boolean is_hidden; //是否隐藏
  public Boolean will_expired; //是否有有效期

  public Certificate() {
  }

  protected Certificate(Parcel in) {
    this.date_of_issue = in.readString();
    this.coach = in.readParcelable(Staff.class.getClassLoader());
    this.name = in.readString();
    this.project_name = in.readString();
    this.certificate_name = in.readString();
    this.photo = in.readString();
    this.grade = in.readString();
    this.created_at = in.readString();
    this.meeting_start = in.readString();
    this.start = in.readString();
    this.end = in.readString();
    this.organization_id = in.readString();
    this.organization = in.readParcelable(Organization.class.getClassLoader());
    this.type = in.readInt();
    this.id = in.readString();
    this.is_authenticated = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.is_hidden = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.will_expired = (Boolean) in.readValue(Boolean.class.getClassLoader());
  }

  public String getDate_of_issue() {
    return date_of_issue;
  }

  public void setDate_of_issue(String date_of_issue) {
    this.date_of_issue = date_of_issue;
  }

  public Staff getCoach() {
    return coach;
  }

  public void setCoach(Staff coach) {
    this.coach = coach;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCertificate_name() {
    return certificate_name;
  }

  public void setCertificate_name(String certificate_name) {
    this.certificate_name = certificate_name;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getOrganization_id() {
    return organization_id;
  }

  public void setOrganization_id(String organization_id) {
    this.organization_id = organization_id;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean is_authenticated() {
    return is_authenticated;
  }

  public void setIs_authenticated(boolean is_authenticated) {
    this.is_authenticated = is_authenticated;
  }

  public boolean is_hidden() {
    return is_hidden;
  }

  public void setIs_hidden(boolean is_hidden) {
    this.is_hidden = is_hidden;
  }

  public boolean isWill_expired() {
    return will_expired;
  }

  public void setWill_expired(boolean will_expired) {
    this.will_expired = will_expired;
  }

  public Certificate getPostBody() throws CloneNotSupportedException {
    Certificate c = (Certificate) this.clone();
    c.id = null;
    return c;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.date_of_issue);
    dest.writeParcelable(this.coach, flags);
    dest.writeString(this.name);
    dest.writeString(this.project_name);
    dest.writeString(this.certificate_name);
    dest.writeString(this.photo);
    dest.writeString(this.grade);
    dest.writeString(this.created_at);
    dest.writeString(this.meeting_start);
    dest.writeString(this.start);
    dest.writeString(this.end);
    dest.writeString(this.organization_id);
    dest.writeParcelable(this.organization, flags);
    dest.writeInt(this.type);
    dest.writeString(this.id);
    dest.writeValue(this.is_authenticated);
    dest.writeValue(this.is_hidden);
    dest.writeValue(this.will_expired);
  }
}
