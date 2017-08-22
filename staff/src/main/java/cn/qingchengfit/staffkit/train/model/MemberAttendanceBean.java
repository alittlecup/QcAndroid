package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/4/1.
 */

public class MemberAttendanceBean implements Parcelable {

    public static final Parcelable.Creator<MemberAttendanceBean> CREATOR = new Parcelable.Creator<MemberAttendanceBean>() {
        @Override public MemberAttendanceBean createFromParcel(Parcel source) {
            return new MemberAttendanceBean(source);
        }

        @Override public MemberAttendanceBean[] newArray(int size) {
            return new MemberAttendanceBean[size];
        }
    };
    public Integer checkin_count;
    public Integer group_count;
    public Integer day_count;
    public Integer private_count;
    public SignUpGroupUser user;

    public MemberAttendanceBean() {
    }

    protected MemberAttendanceBean(Parcel in) {
        this.checkin_count = in.readInt();
        this.group_count = in.readInt();
        this.day_count = in.readInt();
        this.private_count = in.readInt();
        this.user = in.readParcelable(SignUpGroupUser.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.checkin_count);
        dest.writeInt(this.group_count);
        dest.writeInt(this.day_count);
        dest.writeInt(this.private_count);
        dest.writeParcelable(this.user, flags);
    }
}
