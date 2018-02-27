package cn.qingchengfit.staffkit.allocate.coach.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/5/4.
 */

public class Coach implements Parcelable {
    public static final Creator<Coach> CREATOR = new Creator<Coach>() {
        @Override public Coach createFromParcel(Parcel source) {
            return new Coach(source);
        }

        @Override public Coach[] newArray(int size) {
            return new Coach[size];
        }
    };
    public Staff coach;
    public int count;
    public List<String> user = new ArrayList<>();

    public Coach() {
    }

    protected Coach(Parcel in) {
        this.coach = in.readParcelable(Staff.class.getClassLoader());
        this.count = in.readInt();
        this.user = in.createStringArrayList();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.coach, flags);
        dest.writeInt(this.count);
        dest.writeStringList(this.user);
    }
}
