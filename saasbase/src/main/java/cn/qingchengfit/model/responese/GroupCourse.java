package cn.qingchengfit.model.responese;

import android.os.Parcel;
import cn.qingchengfit.model.common.Course;
import com.google.gson.annotations.SerializedName;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //团课
 * //Created by yangming on 16/11/24.
 */

public class GroupCourse extends Course {

    public static final Creator<GroupCourse> CREATOR = new Creator<GroupCourse>() {
        @Override public GroupCourse createFromParcel(Parcel source) {
            return new GroupCourse(source);
        }

        @Override public GroupCourse[] newArray(int size) {
            return new GroupCourse[size];
        }
    };
    @SerializedName("count") public int count;
    @SerializedName("from_date") public String from_date;
    @SerializedName("to_date") public String to_date;

    public GroupCourse() {
    }

    public GroupCourse(Parcel in, int count, String from_date, String to_date) {
        super(in);
        this.count = count;
        this.from_date = from_date;
        this.to_date = to_date;
    }

    protected GroupCourse(Parcel in) {
        super(in);
        this.count = in.readInt();
        this.from_date = in.readString();
        this.to_date = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.count);
        dest.writeString(this.from_date);
        dest.writeString(this.to_date);
    }
}
