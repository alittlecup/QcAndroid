package cn.qingchengfit.recruit.network.body;

import android.os.Parcel;
import android.os.Parcelable;
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
 * Created by Paper on 2017/6/27.
 */

public class JobBody implements Parcelable {
  public static final Parcelable.Creator<JobBody> CREATOR = new Parcelable.Creator<JobBody>() {
    @Override public JobBody createFromParcel(Parcel source) {
      return new JobBody(source);
    }

    @Override public JobBody[] newArray(int size) {
      return new JobBody[size];
    }
  };
  public String name;
  public String description;
  public String requirement;
  public Integer min_work_year;
  public Integer max_work_year;
  public Integer min_age;
  public Integer max_age;
  public Integer gender;
  public Integer education;
  public Integer min_salary;
  public Integer max_salary;
  public Float max_height;
  public Float min_height;
  public Float max_weight;
  public Float min_weight;
  public List<String> welfare;
  public Boolean published;

  private JobBody(Builder builder) {
    name = builder.name;
    description = builder.description;
    requirement = builder.requirement;
    min_work_year = builder.min_work_year;
    max_work_year = builder.max_work_year;
    min_age = builder.min_age;
    max_age = builder.max_age;
    gender = builder.gender;
    education = builder.education;
    min_salary = builder.min_salary;
    max_salary = builder.max_salary;
    max_height = builder.max_height;
    min_height = builder.min_height;
    max_weight = builder.max_weight;
    min_weight = builder.min_weight;
    welfare = builder.welfare;
    published = builder.published;
  }

  protected JobBody(Parcel in) {
    this.name = in.readString();
    this.description = in.readString();
    this.requirement = in.readString();
    this.min_work_year = (Integer) in.readValue(Integer.class.getClassLoader());
    this.max_work_year = (Integer) in.readValue(Integer.class.getClassLoader());
    this.min_age = (Integer) in.readValue(Integer.class.getClassLoader());
    this.max_age = (Integer) in.readValue(Integer.class.getClassLoader());
    this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
    this.education = (Integer) in.readValue(Integer.class.getClassLoader());
    this.min_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.max_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.max_height = (Float) in.readValue(Float.class.getClassLoader());
    this.min_height = (Float) in.readValue(Float.class.getClassLoader());
    this.max_weight = (Float) in.readValue(Float.class.getClassLoader());
    this.min_weight = (Float) in.readValue(Float.class.getClassLoader());
    this.welfare = in.createStringArrayList();
    this.published = (Boolean) in.readValue(Boolean.class.getClassLoader());
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.description);
    dest.writeString(this.requirement);
    dest.writeValue(this.min_work_year);
    dest.writeValue(this.max_work_year);
    dest.writeValue(this.min_age);
    dest.writeValue(this.max_age);
    dest.writeValue(this.gender);
    dest.writeValue(this.education);
    dest.writeValue(this.min_salary);
    dest.writeValue(this.max_salary);
    dest.writeValue(this.max_height);
    dest.writeValue(this.min_height);
    dest.writeValue(this.max_weight);
    dest.writeValue(this.min_weight);
    dest.writeStringList(this.welfare);
    dest.writeValue(this.published);
  }

  public static final class Builder {
    private String name;
    private String description;
    private String requirement;
    private Integer min_work_year;
    private Integer max_work_year;
    private Integer min_age;
    private Integer max_age;
    private Integer gender;
    private Integer education;
    private Integer min_salary;
    private Integer max_salary;
    private Float max_height;
    private Float min_height;
    private Float max_weight;
    private Float min_weight;
    private List<String> welfare;
    private Boolean published;

    public Builder() {
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder description(String val) {
      description = val;
      return this;
    }

    public Builder requirement(String val) {
      requirement = val;
      return this;
    }

    public Builder min_work_year(Integer val) {
      min_work_year = val;
      return this;
    }

    public Builder max_work_year(Integer val) {
      max_work_year = val;
      return this;
    }

    public Builder min_age(Integer val) {
      min_age = val;
      return this;
    }

    public Builder max_age(Integer val) {
      max_age = val;
      return this;
    }

    public Builder gender(Integer val) {
      gender = val;
      return this;
    }

    public Builder education(Integer val) {
      education = val;
      return this;
    }

    public Builder min_salary(Integer val) {
      min_salary = val;
      return this;
    }

    public Builder max_salary(Integer val) {
      max_salary = val;
      return this;
    }

    public Builder max_height(Float val) {
      max_height = val;
      return this;
    }

    public Builder min_height(Float val) {
      min_height = val;
      return this;
    }

    public Builder max_weight(Float val) {
      max_weight = val;
      return this;
    }

    public Builder min_weight(Float val) {
      min_weight = val;
      return this;
    }

    public Builder welfare(List<String> val) {
      welfare = val;
      return this;
    }

    public Builder published(Boolean val) {
      published = val;
      return this;
    }

    public JobBody build() {
      return new JobBody(this);
    }
  }
}
