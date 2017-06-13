package cn.qingchengfit.model.common;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.responese.CourseTypeSample;
import com.google.gson.annotations.SerializedName;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/11/22.
 */

public class Course extends CourseTypeSample implements Parcelable {

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override public Course[] newArray(int size) {
            return new Course[size];
        }
    };
    @SerializedName("image_url") public String image_url;
    @SerializedName("course_count") public int course_count;
    @SerializedName("service_count") public int service_count;

    //private List<String> photos;
    //private int min_users;
    //private List<CourseDetailTeacher> teachers;
    //private List<QcScheduleBean.Shop> shops;
    //private Float course_score;
    //private Float service_score;
    //private Float teacher_score;
    //private int capacity;
    //private String description;
    //private List<TeacherImpression> impressions;
    //private CoursePlan plan;
    //private int permission;//1 全部权限,2部分权限 3没有权限
    //private int schedule_count;
    //private String edit_url;
    //private boolean random_show_photos;

    public Course() {
    }

    public Course(Parcel in, String image_url, int course_count, int service_count) {
        super(in);
        this.image_url = image_url;
        this.course_count = course_count;
        this.service_count = service_count;
    }

    protected Course(Parcel in) {
        super(in);
        this.image_url = in.readString();
        this.course_count = in.readInt();
        this.service_count = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.image_url);
        dest.writeInt(this.course_count);
        dest.writeInt(this.service_count);
    }
}
