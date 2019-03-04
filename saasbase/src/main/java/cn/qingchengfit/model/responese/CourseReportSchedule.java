package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.saasbase.report.bean.CourseCoach;
import cn.qingchengfit.saascommon.bean.Shop;

/**
 * Created by fb on 2017/5/16.
 */

public class CourseReportSchedule implements Parcelable {

    public static final Creator<CourseReportSchedule> CREATOR = new Creator<CourseReportSchedule>() {
        @Override public CourseReportSchedule createFromParcel(Parcel source) {
            return new CourseReportSchedule(source);
        }

        @Override public CourseReportSchedule[] newArray(int size) {
            return new CourseReportSchedule[size];
        }
    };
    public String id;
    public Shop shop;
    public String count;
    public String score;
    public float total_real_price;
    public String start;
    public int total_times;
    public int order_count;
    public float total_account;
    public CourseTypeSample course;
    public CourseCoach teacher;

    public CourseReportSchedule() {
    }

    protected CourseReportSchedule(Parcel in) {
        this.id = in.readString();
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.count = in.readString();
        this.score = in.readString();
        this.total_real_price = in.readFloat();
        this.start = in.readString();
        this.total_times = in.readInt();
        this.order_count = in.readInt();
        this.total_account = in.readFloat();
        this.course = in.readParcelable(CourseTypeSample.class.getClassLoader());
        this.teacher = in.readParcelable(CourseCoach.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.shop, flags);
        dest.writeString(this.count);
        dest.writeString(this.score);
        dest.writeFloat(this.total_real_price);
        dest.writeString(this.start);
        dest.writeInt(this.total_times);
        dest.writeInt(this.order_count);
        dest.writeFloat(this.total_account);
        dest.writeParcelable(this.course, flags);
        dest.writeParcelable(this.teacher, flags);
    }
}
