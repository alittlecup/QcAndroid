package cn.qingchengfit.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import java.util.List;

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
 * Created by Paper on 2017/6/12.
 */

public class WorkExp implements Cloneable, Parcelable {
  public static final Creator<WorkExp> CREATOR = new Creator<WorkExp>() {
    @Override public WorkExp createFromParcel(Parcel source) {
      return new WorkExp(source);
    }

    @Override public WorkExp[] newArray(int size) {
      return new WorkExp[size];
    }
  };
  /**
   * coach : {"id":6}
   * description : 无
   * is_authenticated : false
   * position : 教练
   * private_course : 421
   * city : 北京
   * end : 2015-11-16T15:29:00
   * username : 中美引力工作室
   * group_user : 123
   * sale : 10000000
   * start : 2015-09-16T15:29:00
   * group_course : 100
   * private_user : 568
   */

  public Staff coach;
  public String description;
  public boolean is_authenticated;
  public String position;
  public Integer private_course;
  public String end;
  public String name;
  public String id;
  public Integer group_user;
  public Float sale;
  public String start;
  public Float coach_score;
  public Float course_score;
  public Integer group_course;
  public Integer private_user;
  public Gym gym;
  public Boolean is_hidden;
  public Boolean group_is_hidden;
  public Boolean private_is_hidden;
  public Boolean sale_is_hidden;
  public List<TeacherImpression> impression;
  public String gym_id;

  private WorkExp(Builder builder) {
    setCoach(builder.coach);
    setDescription(builder.description);
    setIs_authenticated(builder.is_authenticated);
    setPosition(builder.position);
    setPrivate_course(builder.private_course);
    setEnd(builder.end);
    setName(builder.name);
    setId(builder.id);
    setGroup_user(builder.group_user);
    setSale(builder.sale);
    setStart(builder.start);
    coach_score = builder.coach_score;
    course_score = builder.course_score;
    setGroup_course(builder.group_course);
    setPrivate_user(builder.private_user);
    setGym(builder.gym);
    is_hidden = builder.is_hidden;
    group_is_hidden = builder.group_is_hidden;
    private_is_hidden = builder.private_is_hidden;
    sale_is_hidden = builder.sale_is_hidden;
    setImpression(builder.impression);
    gym_id = builder.gym_id;
  }

  public WorkExp() {
  }

  protected WorkExp(Parcel in) {
    this.coach = in.readParcelable(Staff.class.getClassLoader());
    this.description = in.readString();
    this.is_authenticated = in.readByte() != 0;
    this.position = in.readString();
    this.private_course = (Integer) in.readValue(Integer.class.getClassLoader());
    this.end = in.readString();
    this.name = in.readString();
    this.id = in.readString();
    this.group_user = (Integer) in.readValue(Integer.class.getClassLoader());
    this.sale = (Float) in.readValue(Float.class.getClassLoader());
    this.start = in.readString();
    this.coach_score = (Float) in.readValue(Float.class.getClassLoader());
    this.course_score = (Float) in.readValue(Float.class.getClassLoader());
    this.group_course = (Integer) in.readValue(Integer.class.getClassLoader());
    this.private_user = (Integer) in.readValue(Integer.class.getClassLoader());
    this.gym = in.readParcelable(Gym.class.getClassLoader());
    this.is_hidden = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.group_is_hidden = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.private_is_hidden = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.sale_is_hidden = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.impression = in.createTypedArrayList(TeacherImpression.CREATOR);
    this.gym_id = in.readString();
  }

  public List<String> getImpressList() {
    return RecruitBusinessUtils.impress2Str(impression);
  }

  public WorkExp getPostBody() throws CloneNotSupportedException {
    WorkExp exp = (WorkExp) this.clone();
    exp.id = null;
    return exp;
  }

  public Staff getCoach() {
    return coach;
  }

  public void setCoach(Staff coach) {
    this.coach = coach;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean is_authenticated() {
    return is_authenticated;
  }

  public void setIs_authenticated(boolean is_authenticated) {
    this.is_authenticated = is_authenticated;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Integer getPrivate_course() {
    return private_course;
  }

  public void setPrivate_course(Integer private_course) {
    this.private_course = private_course;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getGroup_user() {
    return group_user;
  }

  public void setGroup_user(Integer group_user) {
    this.group_user = group_user;
  }

  public Float getSale() {
    return sale;
  }

  public void setSale(Float sale) {
    this.sale = sale;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public float getCoach_score() {
    return coach_score;
  }

  public void setCoach_score(float coach_score) {
    this.coach_score = coach_score;
  }

  public float getCourse_score() {
    return course_score;
  }

  public void setCourse_score(float course_score) {
    this.course_score = course_score;
  }

  public Integer getGroup_course() {
    return group_course;
  }

  public void setGroup_course(Integer group_course) {
    this.group_course = group_course;
  }

  public Integer getPrivate_user() {
    return private_user;
  }

  public void setPrivate_user(Integer private_user) {
    this.private_user = private_user;
  }

  public Gym getGym() {
    return gym;
  }

  public void setGym(Gym gym) {
    this.gym = gym;
  }

  public boolean is_hidden() {
    return is_hidden;
  }

  public void setIs_hidden(boolean is_hidden) {
    this.is_hidden = is_hidden;
  }

  public boolean isGroup_is_hidden() {
    return group_is_hidden;
  }

  public void setGroup_is_hidden(boolean group_is_hidden) {
    this.group_is_hidden = group_is_hidden;
  }

  public boolean isPrivate_is_hidden() {
    return private_is_hidden;
  }

  public void setPrivate_is_hidden(boolean private_is_hidden) {
    this.private_is_hidden = private_is_hidden;
  }

  public boolean isSale_is_hidden() {
    return sale_is_hidden;
  }

  public void setSale_is_hidden(boolean sale_is_hidden) {
    this.sale_is_hidden = sale_is_hidden;
  }

  public List<TeacherImpression> getImpression() {
    return impression;
  }

  public void setImpression(List<TeacherImpression> impression) {
    this.impression = impression;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.coach, flags);
    dest.writeString(this.description);
    dest.writeByte(this.is_authenticated ? (byte) 1 : (byte) 0);
    dest.writeString(this.position);
    dest.writeValue(this.private_course);
    dest.writeString(this.end);
    dest.writeString(this.name);
    dest.writeString(this.id);
    dest.writeValue(this.group_user);
    dest.writeValue(this.sale);
    dest.writeString(this.start);
    dest.writeValue(this.coach_score);
    dest.writeValue(this.course_score);
    dest.writeValue(this.group_course);
    dest.writeValue(this.private_user);
    dest.writeParcelable(this.gym, flags);
    dest.writeValue(this.is_hidden);
    dest.writeValue(this.group_is_hidden);
    dest.writeValue(this.private_is_hidden);
    dest.writeValue(this.sale_is_hidden);
    dest.writeTypedList(this.impression);
    dest.writeString(this.gym_id);
  }

  public static final class Builder {
    private Staff coach;
    private String description;
    private boolean is_authenticated;
    private String position;
    private Integer private_course;
    private String end;
    private String name;
    private String id;
    private Integer group_user;
    private Float sale;
    private String start;
    private Float coach_score;
    private Float course_score;
    private Integer group_course;
    private Integer private_user;
    private Gym gym;
    private Boolean is_hidden;
    private Boolean group_is_hidden;
    private Boolean private_is_hidden;
    private Boolean sale_is_hidden;
    private List<TeacherImpression> impression;
    private String gym_id;

    public Builder() {
    }

    public Builder coach(Staff val) {
      coach = val;
      return this;
    }

    public Builder description(String val) {
      description = val;
      return this;
    }

    public Builder is_authenticated(boolean val) {
      is_authenticated = val;
      return this;
    }

    public Builder position(String val) {
      position = val;
      return this;
    }

    public Builder private_course(Integer val) {
      private_course = val;
      return this;
    }

    public Builder end(String val) {
      end = val;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder group_user(Integer val) {
      group_user = val;
      return this;
    }

    public Builder sale(Float val) {
      sale = val;
      return this;
    }

    public Builder start(String val) {
      start = val;
      return this;
    }

    public Builder coach_score(Float val) {
      coach_score = val;
      return this;
    }

    public Builder course_score(Float val) {
      course_score = val;
      return this;
    }

    public Builder group_course(Integer val) {
      group_course = val;
      return this;
    }

    public Builder private_user(Integer val) {
      private_user = val;
      return this;
    }

    public Builder gym(Gym val) {
      gym = val;
      return this;
    }

    public Builder is_hidden(Boolean val) {
      is_hidden = val;
      return this;
    }

    public Builder group_is_hidden(Boolean val) {
      group_is_hidden = val;
      return this;
    }

    public Builder private_is_hidden(Boolean val) {
      private_is_hidden = val;
      return this;
    }

    public Builder sale_is_hidden(Boolean val) {
      sale_is_hidden = val;
      return this;
    }

    public Builder impression(List<TeacherImpression> val) {
      impression = val;
      return this;
    }

    public Builder gym_id(String val) {
      gym_id = val;
      return this;
    }

    public WorkExp build() {
      return new WorkExp(this);
    }
  }
}
