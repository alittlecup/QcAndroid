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
 1         # 中专及以下
 2         # 高中
 3         # 大专
 4         # 本科
 5         # 硕士
 6         # 博士
 *
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
    public float max_salary; //薪水
    public float min_salary;
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
    public String description;
    public Created_by created_by;
    public String created_at;

    public Job() {
    }

    protected Job(Parcel in) {
        this.id = in.readString();
        this.max_salary = in.readFloat();
        this.min_salary = in.readFloat();
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
        this.created_by = in.readParcelable(Created_by.class.getClassLoader());
        this.created_at = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeFloat(this.max_salary);
        dest.writeFloat(this.min_salary);
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
        dest.writeParcelable(this.created_by, flags);
        dest.writeString(this.created_at);
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
        public String position;

        public Created_by() {
        }

        protected Created_by(Parcel in) {
            this.username = in.readString();
            this.position = in.readString();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.username);
            dest.writeString(this.position);
        }
    }
}
