package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/3/22.
 */

public class AttendanceBean implements Parcelable {
    public static final Creator<AttendanceBean> CREATOR = new Creator<AttendanceBean>() {
        @Override public AttendanceBean createFromParcel(Parcel in) {
            return new AttendanceBean(in);
        }

        @Override public AttendanceBean[] newArray(int size) {
            return new AttendanceBean[size];
        }
    };
    public RankTypeBean days;
    public RankTypeBean private_course;
    public RankTypeBean group_course;
    public RankTypeBean checkin;

    protected AttendanceBean(Parcel in) {
        days = in.readParcelable(RankTypeBean.class.getClassLoader());
        private_course = in.readParcelable(RankTypeBean.class.getClassLoader());
        group_course = in.readParcelable(RankTypeBean.class.getClassLoader());
        checkin = in.readParcelable(RankTypeBean.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(days, i);
        parcel.writeParcelable(private_course, i);
        parcel.writeParcelable(group_course, i);
        parcel.writeParcelable(checkin, i);
    }
}
