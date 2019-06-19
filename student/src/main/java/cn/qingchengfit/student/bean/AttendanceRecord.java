package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.User_Student;

public class AttendanceRecord implements Parcelable {

    public static final Parcelable.Creator<AttendanceRecord> CREATOR = new Parcelable.Creator<AttendanceRecord>() {
        @Override public AttendanceRecord createFromParcel(Parcel source) {
            return new AttendanceRecord(source);
        }

        @Override public AttendanceRecord[] newArray(int size) {
            return new AttendanceRecord[size];
        }
    };
    public Shop shop;
    public Course course;
    public User_Student teacher;
    public String start;
    public String end;
    public String url;
    public String id;
    public int type;

    public AttendanceRecord() {
    }

    protected AttendanceRecord(Parcel in) {
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.course = in.readParcelable(Course.class.getClassLoader());
        this.teacher = in.readParcelable(User_Student.class.getClassLoader());
        this.start = in.readString();
        this.end = in.readString();
        this.url = in.readString();
        this.id = in.readString();
        this.type = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shop, flags);
        dest.writeParcelable(this.course, flags);
        dest.writeParcelable(this.teacher, flags);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.url);
        dest.writeString(this.id);
        dest.writeInt(this.type);
    }
}
