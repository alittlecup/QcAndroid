package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.QcStudentBean;
import java.util.List;

/**
 * Created by fb on 2017/3/22.
 */

public class GroupBean implements Parcelable {

    public static final Creator<GroupBean> CREATOR = new Creator<GroupBean>() {
        @Override public GroupBean createFromParcel(Parcel source) {
            return new GroupBean(source);
        }

        @Override public GroupBean[] newArray(int size) {
            return new GroupBean[size];
        }
    };
    public int id;
    public String name;
    public String created_at;
    public List<MemberAttendanceBean> users_attendance;
    public List<QcStudentBean> users;
    public AttendanceBean team_attendance;

    public GroupBean() {
    }

    protected GroupBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.created_at = in.readString();
        this.users_attendance = in.createTypedArrayList(MemberAttendanceBean.CREATOR);
        this.users = in.createTypedArrayList(QcStudentBean.CREATOR);
        this.team_attendance = in.readParcelable(AttendanceBean.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.created_at);
        dest.writeTypedList(this.users_attendance);
        dest.writeTypedList(this.users);
        dest.writeParcelable(this.team_attendance, flags);
    }
}
