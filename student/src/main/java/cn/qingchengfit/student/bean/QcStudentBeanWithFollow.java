package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class QcStudentBeanWithFollow extends QcStudentBean implements Parcelable{

    public String track_record;
    public String track_status;
    public String track_at;
    public String first_card_info;
    public String origin;
    public Staff recommend_by;//推荐人

    public int unfollowed_count;
    public int total_count;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.track_record);
        dest.writeString(this.track_status);
        dest.writeString(this.track_at);
        dest.writeString(this.first_card_info);
        dest.writeString(this.origin);
        dest.writeInt(unfollowed_count);
        dest.writeInt(total_count);
        dest.writeParcelable(this.recommend_by, flags);
    }

    public QcStudentBeanWithFollow() {
    }

    protected QcStudentBeanWithFollow(Parcel in) {
        super(in);
        this.track_record = in.readString();
        this.track_status = in.readString();
        this.track_at = in.readString();
        this.first_card_info = in.readString();
        this.origin = in.readString();
        this.unfollowed_count=in.readInt();
        this.total_count=in.readInt();
        this.recommend_by = in.readParcelable(Staff.class.getClassLoader());
    }

    public static final Parcelable.Creator<QcStudentBeanWithFollow> CREATOR = new Parcelable.Creator<QcStudentBeanWithFollow>() {
        @Override
        public QcStudentBeanWithFollow createFromParcel(Parcel source) {
            return new QcStudentBeanWithFollow(source);
        }

        @Override
        public QcStudentBeanWithFollow[] newArray(int size) {
            return new QcStudentBeanWithFollow[size];
        }
    };
}
