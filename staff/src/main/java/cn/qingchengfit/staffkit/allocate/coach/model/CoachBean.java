package cn.qingchengfit.staffkit.allocate.coach.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/5/4.
 */

public class CoachBean implements Parcelable {

    public static final Creator<CoachBean> CREATOR = new Creator<CoachBean>() {
        @Override public CoachBean createFromParcel(Parcel source) {
            return new CoachBean(source);
        }

        @Override public CoachBean[] newArray(int size) {
            return new CoachBean[size];
        }
    };
    public String username;
    public String id;
    public String gender;
    public String avatar;

    public CoachBean() {
    }

    protected CoachBean(Parcel in) {
        this.username = in.readString();
        this.id = in.readString();
        this.gender = in.readString();
        this.avatar = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.id);
        dest.writeString(this.gender);
        dest.writeString(this.avatar);
    }
}
