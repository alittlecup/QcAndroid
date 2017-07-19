package cn.qingchengfit.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.DistrictEntity;
import cn.qingchengfit.model.base.Gym;
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
 * Created by Paper on 2017/5/23.
 *
 * 招聘板块  工作职位
 *
 * -1      # 不限
 * 1         # 中专及以下
 * 2         # 高中
 * 3         # 大专
 * 4         # 本科
 * 5         # 硕士
 * 6         # 博士
 */

public class Job implements Parcelable {
  public static final int UN_LIMIT = -1;//不限
  public static final Creator<Job> CREATOR = new Creator<Job>() {
    @Override public Job createFromParcel(Parcel source) {
      return new Job(source);
    }

    @Override public Job[] newArray(int size) {
      return new Job[size];
    }
  };
  public String id;
  public int max_salary; //薪水
  public int min_salary;
  public int min_age;  //年龄
  public int max_age;
  public float min_height;  //身高
  public float min_weight;  //体重
  public float max_height;
  public float max_weight;
  public DistrictEntity gd_district;
  public int education; //
  public int max_work_year; //工作年限
  public int min_work_year;
  public String name;
  public int gender;
  public boolean published;
  public List<String> welfare;
  public Gym gym;
  public String description; //职位描述
  public String requirement; //职位描述
  public Created_by created_by;
  public String created_at;
  public String published_at;
  public Boolean favorited;
  public Boolean contacted;
  public Boolean deliveried;

  public int invitation_count;
  public int delivery_count;
  public int favorite_count;
  public int view;
  public boolean has_new_delivery;
  public boolean has_new_invite;
  public boolean has_new_resume;

  public Job() {
  }

  private Job(Builder builder) {
    id = builder.id;
    max_salary = builder.max_salary;
    min_salary = builder.min_salary;
    min_age = builder.min_age;
    max_age = builder.max_age;
    min_height = builder.min_height;
    min_weight = builder.min_weight;
    max_height = builder.max_height;
    max_weight = builder.max_weight;
    gd_district = builder.gd_district;
    education = builder.education;
    max_work_year = builder.max_work_year;
    min_work_year = builder.min_work_year;
    name = builder.name;
    gender = builder.gender;
    published = builder.published;
    welfare = builder.welfare;
    gym = builder.gym;
    description = builder.description;
    requirement = builder.requirement;
    created_by = builder.created_by;
    created_at = builder.created_at;
    published_at = builder.published_at;
    favorited = builder.favorited;
    contacted = builder.contacted;
    deliveried = builder.deliveried;
  }

  protected Job(Parcel in) {
    this.id = in.readString();
    this.max_salary = in.readInt();
    this.min_salary = in.readInt();
    this.min_age = in.readInt();
    this.max_age = in.readInt();
    this.min_height = in.readFloat();
    this.min_weight = in.readFloat();
    this.max_height = in.readFloat();
    this.max_weight = in.readFloat();
    this.gd_district = in.readParcelable(DistrictEntity.class.getClassLoader());
    this.education = in.readInt();
    this.max_work_year = in.readInt();
    this.min_work_year = in.readInt();
    this.name = in.readString();
    this.gender = in.readInt();
    this.published = in.readByte() != 0;
    this.welfare = in.createStringArrayList();
    this.gym = in.readParcelable(Gym.class.getClassLoader());
    this.description = in.readString();
    this.requirement = in.readString();
    this.created_by = in.readParcelable(Created_by.class.getClassLoader());
    this.created_at = in.readString();
    this.published_at = in.readString();
    this.favorited = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.contacted = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.deliveried = (Boolean) in.readValue(Boolean.class.getClassLoader());
  }

  public String getAddress() {
    if (gym != null) {
      return gym.getAddressStr();
    } else {
      return "";
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeInt(this.max_salary);
    dest.writeInt(this.min_salary);
    dest.writeInt(this.min_age);
    dest.writeInt(this.max_age);
    dest.writeFloat(this.min_height);
    dest.writeFloat(this.min_weight);
    dest.writeFloat(this.max_height);
    dest.writeFloat(this.max_weight);
    dest.writeParcelable(this.gd_district, flags);
    dest.writeInt(this.education);
    dest.writeInt(this.max_work_year);
    dest.writeInt(this.min_work_year);
    dest.writeString(this.name);
    dest.writeInt(this.gender);
    dest.writeByte(this.published ? (byte) 1 : (byte) 0);
    dest.writeStringList(this.welfare);
    dest.writeParcelable(this.gym, flags);
    dest.writeString(this.description);
    dest.writeString(this.requirement);
    dest.writeParcelable(this.created_by, flags);
    dest.writeString(this.created_at);
    dest.writeString(this.published_at);
    dest.writeValue(this.favorited);
    dest.writeValue(this.contacted);
    dest.writeValue(this.deliveried);
  }

  public static class Created_by implements Parcelable {
    public static final Creator<Created_by> CREATOR = new Creator<Created_by>() {
      @Override public Created_by createFromParcel(Parcel source) {
        return new Created_by(source);
      }

      @Override public Created_by[] newArray(int size) {
        return new Created_by[size];
      }
    };
    public String username;
    public String avatar;
    public String id;

    public Created_by() {
    }

    protected Created_by(Parcel in) {
      this.username = in.readString();
      this.avatar = in.readString();
      this.id = in.readString();
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.username);
      dest.writeString(this.avatar);
      dest.writeString(this.id);
    }
  }

  public static final class Builder {
    private String id;
    private int max_salary;
    private int min_salary;
    private int min_age;
    private int max_age;
    private float min_height;
    private float min_weight;
    private float max_height;
    private float max_weight;
    private DistrictEntity gd_district;
    private int education;
    private int max_work_year;
    private int min_work_year;
    private String name;
    private int gender;
    private boolean published;
    private List<String> welfare;
    private Gym gym;
    private String description;
    private String requirement;
    private Created_by created_by;
    private String created_at;
    private String published_at;
    private Boolean favorited;
    private Boolean contacted;
    private Boolean deliveried;

    public Builder() {
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder max_salary(int val) {
      max_salary = val;
      return this;
    }

    public Builder min_salary(int val) {
      min_salary = val;
      return this;
    }

    public Builder min_age(int val) {
      min_age = val;
      return this;
    }

    public Builder max_age(int val) {
      max_age = val;
      return this;
    }

    public Builder min_height(float val) {
      min_height = val;
      return this;
    }

    public Builder min_weight(float val) {
      min_weight = val;
      return this;
    }

    public Builder max_height(float val) {
      max_height = val;
      return this;
    }

    public Builder max_weight(float val) {
      max_weight = val;
      return this;
    }

    public Builder gd_district(DistrictEntity val) {
      gd_district = val;
      return this;
    }

    public Builder education(int val) {
      education = val;
      return this;
    }

    public Builder max_work_year(int val) {
      max_work_year = val;
      return this;
    }

    public Builder min_work_year(int val) {
      min_work_year = val;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder gender(int val) {
      gender = val;
      return this;
    }

    public Builder published(boolean val) {
      published = val;
      return this;
    }

    public Builder welfare(List<String> val) {
      welfare = val;
      return this;
    }

    public Builder gym(Gym val) {
      gym = val;
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

    public Builder created_by(Created_by val) {
      created_by = val;
      return this;
    }

    public Builder created_at(String val) {
      created_at = val;
      return this;
    }

    public Builder published_at(String val) {
      published_at = val;
      return this;
    }

    public Builder favorited(Boolean val) {
      favorited = val;
      return this;
    }

    public Builder contacted(Boolean val) {
      contacted = val;
      return this;
    }

    public Builder deliveried(Boolean val) {
      deliveried = val;
      return this;
    }

    public Job build() {
      return new Job(this);
    }
  }
}
