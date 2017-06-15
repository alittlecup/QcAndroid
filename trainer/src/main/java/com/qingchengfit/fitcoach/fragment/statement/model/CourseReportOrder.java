package com.qingchengfit.fitcoach.fragment.statement.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.QcStudentBean;

/**
 * Created by fb on 2017/5/16.
 */

public class CourseReportOrder implements Parcelable {

    public static final Creator<CourseReportOrder> CREATOR = new Creator<CourseReportOrder>() {
        @Override public CourseReportOrder createFromParcel(Parcel source) {
            return new CourseReportOrder(source);
        }

        @Override public CourseReportOrder[] newArray(int size) {
            return new CourseReportOrder[size];
        }
    };
    public int count;
    public int status;
    public String created_at;
    public String schedule_id;
    public float total_price;
    public float total_real_price;
    public QcStudentBean user;
    public CourseReportCard card;
    public String channel;

    public CourseReportOrder() {
    }

    protected CourseReportOrder(Parcel in) {
        this.count = in.readInt();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.schedule_id = in.readString();
        this.total_price = in.readFloat();
        this.total_real_price = in.readFloat();
        this.user = in.readParcelable(QcStudentBean.class.getClassLoader());
        this.card = in.readParcelable(CourseReportCard.class.getClassLoader());
        this.channel = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.schedule_id);
        dest.writeFloat(this.total_price);
        dest.writeFloat(this.total_real_price);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.card, flags);
        dest.writeString(this.channel);
    }
}
