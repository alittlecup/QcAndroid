package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/4/1.
 */

public class SignUpGroupUser implements Parcelable {

    public static final Creator<SignUpGroupUser> CREATOR = new Creator<SignUpGroupUser>() {
        @Override public SignUpGroupUser createFromParcel(Parcel source) {
            return new SignUpGroupUser(source);
        }

        @Override public SignUpGroupUser[] newArray(int size) {
            return new SignUpGroupUser[size];
        }
    };
    public int id;
    public String username;
    public String avatar;

    public SignUpGroupUser() {
    }

    protected SignUpGroupUser(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.avatar = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.avatar);
    }
}
