package com.qingchengfit.fitcoach.fragment.statement.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.bean.CourseTypeSample;
import cn.qingchengfit.bean.StudentBean;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/6/29 2016.
 */
public class ClassStatmentFilterBean implements Parcelable {
    public static final Creator<ClassStatmentFilterBean> CREATOR = new Creator<ClassStatmentFilterBean>() {
        @Override public ClassStatmentFilterBean createFromParcel(Parcel source) {
            return new ClassStatmentFilterBean(source);
        }

        @Override public ClassStatmentFilterBean[] newArray(int size) {
            return new ClassStatmentFilterBean[size];
        }
    };
    public String start;
    public String end;
    public CourseTypeSample course;
    public int course_type;
    public Staff coach;
    public StudentBean student;

    public ClassStatmentFilterBean() {
    }

    protected ClassStatmentFilterBean(Parcel in) {
        this.start = in.readString();
        this.end = in.readString();
        this.course = in.readParcelable(CourseTypeSample.class.getClassLoader());
        this.course_type = in.readInt();
        this.coach = in.readParcelable(Staff.class.getClassLoader());
        this.student = in.readParcelable(StudentBean.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeParcelable(this.course, flags);
        dest.writeInt(this.course_type);
        dest.writeParcelable(this.coach, flags);
        dest.writeParcelable(this.student, flags);
    }
}
