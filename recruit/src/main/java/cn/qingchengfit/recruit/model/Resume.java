package cn.qingchengfit.recruit.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.DateUtils;
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

public class Resume implements Parcelable {
  public static final Creator<Resume> CREATOR = new Creator<Resume>() {
    @Override public Resume createFromParcel(Parcel source) {
      return new Resume(source);
    }

    @Override public Resume[] newArray(int size) {
      return new Resume[size];
    }
  };
  public String id;
  public String user_id;
  public String username;
  public String birthday;
  public String avatar;
  public int status;
  public int gender;
  public float weight;
  public float height;
  public List<String> exp_jobs;
  public List<String> exp_cities;
  public Integer max_salary; //薪水
  public Integer min_salary;
  public Integer max_education;
  public Integer work_year;

  public Resume() {
  }

  protected Resume(Parcel in) {
    this.id = in.readString();
    this.user_id = in.readString();
    this.username = in.readString();
    this.birthday = in.readString();
    this.avatar = in.readString();
    this.status = in.readInt();
    this.gender = in.readInt();
    this.weight = in.readFloat();
    this.height = in.readFloat();
    this.exp_jobs = in.createStringArrayList();
    this.exp_cities = in.createStringArrayList();
    this.max_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.min_salary = (Integer) in.readValue(Integer.class.getClassLoader());
    this.max_education = (Integer) in.readValue(Integer.class.getClassLoader());
    this.work_year = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public String getExpStr() {
    return "期望";
  }

  public String getBaseInfoStr(Context context) {
    StringBuffer sb = new StringBuffer();
    if (work_year == 0) {
      sb.append("应届生 / ");
    } else {
      sb.append(work_year + "年经验 / ");
    }
    sb.append(DateUtils.getAge(DateUtils.formatDateFromServer(birthday)) + "岁 / ");
    sb.append(height + "cm，" + weight + "kg / ");
    sb.append(RecruitBusinessUtils.getDegree(context, max_education));
    return sb.toString();
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
    dest.writeInt(this.status);
    dest.writeInt(this.gender);
    dest.writeFloat(this.weight);
    dest.writeFloat(this.height);
    dest.writeStringList(this.exp_jobs);
    dest.writeStringList(this.exp_cities);
    dest.writeValue(this.max_salary);
    dest.writeValue(this.min_salary);
    dest.writeValue(this.max_education);
    dest.writeValue(this.work_year);
  }
}
