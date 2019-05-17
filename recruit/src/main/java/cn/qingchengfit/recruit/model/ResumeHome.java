package cn.qingchengfit.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.DistrictEntity;
import cn.qingchengfit.utils.CmStringUtils;
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
 * Created by Paper on 2017/6/13.
 *
 * "resume": {
 * "status": 1,
 * "weight": 0,
 * "exp_jobs": [],
 * "height": 0,
 * "exp_cities": [],
 * "gd_district": {},
 * "id": "XQZDaw4k",
 * "min_salary": -1,
 * "completion": 45.5,
 * "user_id": 7060,
 * "certificates": [],
 * "username": "",
 * "max_salary": -1,
 * "max_education": -1,
 * "educations": [],
 * "work_year": 0,
 * "photos": [],
 * "birthday": "",
 * "experiences": [],
 * "is_share": false,
 * "self_description": "",
 * "gender": 0,
 * "created_at": "2017-06-09T11:07:33",
 * "brief_description": "",
 * "avatar": "https://img.qingchengfit.cn/header/43a167df-ee5b-4641-84bb-a36fa018e0aa.png"
 * }
 */

public class ResumeHome extends Resume implements Parcelable {

  public static final Creator<ResumeHome> CREATOR = new Creator<ResumeHome>() {
    @Override public ResumeHome createFromParcel(Parcel source) {
      return new ResumeHome(source);
    }

    @Override public ResumeHome[] newArray(int size) {
      return new ResumeHome[size];
    }
  };
  public String self_description;  //富文本
  public String created_at;
  public String brief_description;  //一句话描述
  public DistrictEntity gd_district;
  public boolean is_share;
  public List<Certificate> certificates;
  public List<WorkExp> experiences;
  public List<Education> educations;
  public List<String> photos;

  public Float completion;
  public boolean favorited;
  public boolean sign_up;


  private ResumeHome(Builder builder) {
    id = builder.id;
    user_id = builder.user_id;
    username = builder.username;
    birthday = builder.birthday;
    avatar = builder.avatar;
    self_description = builder.self_description;
    created_at = builder.created_at;
    brief_description = builder.brief_description;
    gd_district = builder.gd_district;
    is_share = builder.is_share;
    status = builder.status;
    gender = builder.gender;
    weight = builder.weight;
    height = builder.height;
    exp_jobs = builder.exp_jobs;
    exp_cities = builder.exp_cities;
    certificates = builder.certificates;
    experiences = builder.experiences;
    educations = builder.educations;
    photos = builder.photos;
    max_salary = builder.max_salary;
    min_salary = builder.min_salary;
    completion = builder.completion;
    max_education = builder.max_education;
    work_year = builder.work_year;
  }

  protected ResumeHome(Parcel in) {
    this.id = in.readString();
    this.user_id = in.readString();
    this.username = in.readString();
    this.birthday = in.readString();
    this.avatar = in.readString();
    this.self_description = in.readString();
    this.created_at = in.readString();
    this.brief_description = in.readString();
    this.gd_district = in.readParcelable(DistrictEntity.class.getClassLoader());
    this.is_share = in.readByte() != 0;
    this.status = in.readInt();
    this.gender = in.readInt();
    this.weight = in.readFloat();
    this.height = in.readFloat();
    this.exp_jobs = in.createStringArrayList();
    this.exp_cities = in.createStringArrayList();
    this.certificates = in.createTypedArrayList(Certificate.CREATOR);
    this.experiences = in.createTypedArrayList(WorkExp.CREATOR);
    this.educations = in.createTypedArrayList(Education.CREATOR);
    this.photos = in.createStringArrayList();
    this.max_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.min_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.completion = (Float) in.readValue(Float.class.getClassLoader());
    this.max_education = (Integer) in.readValue(Integer.class.getClassLoader());
    this.work_year = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public boolean checkResumeCompleted(){
    return
        !CmStringUtils.isEmpty(username)
        && !CmStringUtils.isEmpty(birthday)
        && !CmStringUtils.isEmpty(avatar)
        && height != 0
        && weight != 0
        && gd_district != null
        && work_year != null
        && work_year != -1
        && !CmStringUtils.isEmpty(brief_description)
        && min_salary != null
        && max_salary != null
        && exp_jobs != null && exp_jobs.size() > 0
        && photos != null && photos.size() > 0
        && educations != null && educations.size() > 0;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.user_id);
    dest.writeString(this.username);
    dest.writeString(this.birthday);
    dest.writeString(this.avatar);
    dest.writeString(this.self_description);
    dest.writeString(this.created_at);
    dest.writeString(this.brief_description);
    dest.writeParcelable(this.gd_district, flags);
    dest.writeByte(this.is_share ? (byte) 1 : (byte) 0);
    dest.writeInt(this.status);
    dest.writeInt(this.gender);
    dest.writeFloat(this.weight);
    dest.writeFloat(this.height);
    dest.writeStringList(this.exp_jobs);
    dest.writeStringList(this.exp_cities);
    dest.writeTypedList(this.certificates);
    dest.writeTypedList(this.experiences);
    dest.writeTypedList(this.educations);
    dest.writeStringList(this.photos);
    dest.writeValue(this.max_salary);
    dest.writeValue(this.min_salary);
    dest.writeValue(this.completion);
    dest.writeValue(this.max_education);
    dest.writeValue(this.work_year);
  }

  public static final class Builder {
    private String id;
    private String user_id;
    private String username;
    private String birthday;
    private String avatar;
    private String self_description;
    private String created_at;
    private String brief_description;
    private DistrictEntity gd_district;
    private boolean is_share;
    private int status;
    private int gender;
    private float weight;
    private float height;
    private List<String> exp_jobs;
    private List<String> exp_cities;
    private List<Certificate> certificates;
    private List<WorkExp> experiences;
    private List<Education> educations;
    private List<String> photos;
    private Integer max_salary;
    private Integer min_salary;
    private Float completion;
    private Integer max_education;
    private Integer work_year;

    public Builder() {
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder user_id(String val) {
      user_id = val;
      return this;
    }

    public Builder username(String val) {
      username = val;
      return this;
    }

    public Builder birthday(String val) {
      birthday = val;
      return this;
    }

    public Builder avatar(String val) {
      avatar = val;
      return this;
    }

    public Builder self_description(String val) {
      self_description = val;
      return this;
    }

    public Builder created_at(String val) {
      created_at = val;
      return this;
    }

    public Builder brief_description(String val) {
      brief_description = val;
      return this;
    }

    public Builder gd_district(DistrictEntity val) {
      gd_district = val;
      return this;
    }

    public Builder is_share(boolean val) {
      is_share = val;
      return this;
    }

    public Builder status(int val) {
      status = val;
      return this;
    }

    public Builder gender(int val) {
      gender = val;
      return this;
    }

    public Builder weight(float val) {
      weight = val;
      return this;
    }

    public Builder height(float val) {
      height = val;
      return this;
    }

    public Builder exp_jobs(List<String> val) {
      exp_jobs = val;
      return this;
    }

    public Builder exp_cities(List<String> val) {
      exp_cities = val;
      return this;
    }

    public Builder certificates(List<Certificate> val) {
      certificates = val;
      return this;
    }

    public Builder experiences(List<WorkExp> val) {
      experiences = val;
      return this;
    }

    public Builder educations(List<Education> val) {
      educations = val;
      return this;
    }

    public Builder photos(List<String> val) {
      photos = val;
      return this;
    }

    public Builder max_salary(Integer val) {
      max_salary = val;
      return this;
    }

    public Builder min_salary(Integer val) {
      min_salary = val;
      return this;
    }

    public Builder completion(Float val) {
      completion = val;
      return this;
    }

    public Builder max_education(Integer val) {
      max_education = val;
      return this;
    }

    public Builder work_year(Integer val) {
      work_year = val;
      return this;
    }

    public Builder completion(float val) {
      completion = val;
      return this;
    }

    public Builder max_education(int val) {
      max_education = val;
      return this;
    }

    public Builder work_year(int val) {
      work_year = val;
      return this;
    }

    public ResumeHome build() {
      return new ResumeHome(this);
    }
  }
}
