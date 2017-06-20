package cn.qingchengfit.recruit.network.body;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.DistrictEntity;
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
 * Created by Paper on 2017/6/15.
 */

public class ResumeBody implements Parcelable {
  public static final Creator<ResumeBody> CREATOR = new Creator<ResumeBody>() {
    @Override public ResumeBody createFromParcel(Parcel source) {
      return new ResumeBody(source);
    }

    @Override public ResumeBody[] newArray(int size) {
      return new ResumeBody[size];
    }
  };
  public String user_id;
  public String username;
  public String birthday;
  public String avatar;
  public String city;
  public String gd_district_id;
  public String self_description;  //富文本
  public String created_at;
  public String brief_description;  //一句话描述
  public Boolean is_share;
  public Integer status;
  public Integer gender;
  public Float weight;
  public Float height;
  public List<String> exp_jobs;
  public List<String> exp_cities;
  public List<String> photos;
  public Integer max_salary; //薪水
  public Integer min_salary;
  public Integer max_education;
  public Integer work_year;

  private ResumeBody(Builder builder) {
    user_id = builder.user_id;
    username = builder.username;
    birthday = builder.birthday;
    avatar = builder.avatar;
    city = builder.city;
    gd_district_id = builder.gd_district_id;
    self_description = builder.self_description;
    created_at = builder.created_at;
    brief_description = builder.brief_description;
    is_share = builder.is_share;
    status = builder.status;
    gender = builder.gender;
    weight = builder.weight;
    height = builder.height;
    exp_jobs = builder.exp_jobs;
    exp_cities = builder.exp_cities;
    photos = builder.photos;
    max_salary = builder.max_salary;
    min_salary = builder.min_salary;
    max_education = builder.max_education;
    work_year = builder.work_year;
  }

  protected ResumeBody(Parcel in) {
    this.user_id = in.readString();
    this.username = in.readString();
    this.birthday = in.readString();
    this.avatar = in.readString();
    this.city = in.readString();
    this.gd_district_id = in.readString();
    this.self_description = in.readString();
    this.created_at = in.readString();
    this.brief_description = in.readString();
    this.is_share = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.status = (Integer) in.readValue(Integer.class.getClassLoader());
    this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
    this.weight = (Float) in.readValue(Float.class.getClassLoader());
    this.height = (Float) in.readValue(Float.class.getClassLoader());
    this.exp_jobs = in.createStringArrayList();
    this.exp_cities = in.createStringArrayList();
    this.photos = in.createStringArrayList();
    this.max_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.min_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.max_education = (Integer) in.readValue(Integer.class.getClassLoader());
    this.work_year = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.user_id);
    dest.writeString(this.username);
    dest.writeString(this.birthday);
    dest.writeString(this.avatar);
    dest.writeString(this.city);
    dest.writeString(this.gd_district_id);
    dest.writeString(this.self_description);
    dest.writeString(this.created_at);
    dest.writeString(this.brief_description);
    dest.writeValue(this.is_share);
    dest.writeValue(this.status);
    dest.writeValue(this.gender);
    dest.writeValue(this.weight);
    dest.writeValue(this.height);
    dest.writeStringList(this.exp_jobs);
    dest.writeStringList(this.exp_cities);
    dest.writeStringList(this.photos);
    dest.writeValue(this.max_salary);
    dest.writeValue(this.min_salary);
    dest.writeValue(this.max_education);
    dest.writeValue(this.work_year);
  }

  public static final class Builder {
    private String user_id;
    private String username;
    private String birthday;
    private String avatar;
    private String city;
    private String gd_district_id;
    private DistrictEntity gd_district;
    private String self_description;
    private String created_at;
    private String brief_description;
    private Boolean is_share;
    private Integer status;
    private Integer gender;
    private Float weight;
    private Float height;
    private List<String> exp_jobs;
    private List<String> exp_cities;
    private List<String> photos;
    private Integer max_salary;
    private Integer min_salary;
    private Integer max_education;
    private Integer work_year;

    public Builder() {
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

    public Builder city(String val) {
      city = val;
      return this;
    }

    public Builder gd_district_id(String val) {
      gd_district_id = val;
      return this;
    }

    public Builder gd_district(DistrictEntity val) {
      gd_district = val;
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

    public Builder is_share(Boolean val) {
      is_share = val;
      return this;
    }

    public Builder status(Integer val) {
      status = val;
      return this;
    }

    public Builder gender(Integer val) {
      gender = val;
      return this;
    }

    public Builder weight(Float val) {
      weight = val;
      return this;
    }

    public Builder height(Float val) {
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

    public Builder max_education(Integer val) {
      max_education = val;
      return this;
    }

    public Builder work_year(Integer val) {
      work_year = val;
      return this;
    }

    public ResumeBody build() {
      return new ResumeBody(this);
    }
  }
}
