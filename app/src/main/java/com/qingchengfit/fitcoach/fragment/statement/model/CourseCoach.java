package com.qingchengfit.fitcoach.fragment.statement.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;

/**
 * Created by fb on 2017/5/17.
 */

public class CourseCoach extends Staff {

    public int score;

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.score);
    }

    public CourseCoach() {
    }

    protected CourseCoach(Parcel in) {
        super(in);
        this.score = in.readInt();
    }

    public static final Parcelable.Creator<CourseCoach>
        CREATOR = new Parcelable.Creator<CourseCoach>() {
        @Override public CourseCoach createFromParcel(Parcel source) {
            return new CourseCoach(source);
        }

        @Override public CourseCoach[] newArray(int size) {
            return new CourseCoach[size];
        }
    };
}
