package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class CourseDetailTeacher implements Parcelable {
    public static final Parcelable.Creator<CourseDetailTeacher> CREATOR = new Parcelable.Creator<CourseDetailTeacher>() {
        @Override public CourseDetailTeacher createFromParcel(Parcel source) {
            return new CourseDetailTeacher(source);
        }

        @Override public CourseDetailTeacher[] newArray(int size) {
            return new CourseDetailTeacher[size];
        }
    };
    @SerializedName("username") public String username;
    @SerializedName("score") public float score;
    @SerializedName("avatar") public String avatar;
    @SerializedName("id") public Long id;

    public CourseDetailTeacher() {
    }

    protected CourseDetailTeacher(Parcel in) {
        this.username = in.readString();
        this.score = in.readFloat();
        this.avatar = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeFloat(this.score);
        dest.writeString(this.avatar);
        dest.writeValue(this.id);
    }
}