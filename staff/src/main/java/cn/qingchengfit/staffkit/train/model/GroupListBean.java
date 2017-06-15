package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/3/28.
 */

public class GroupListBean implements Parcelable {

    public static final Creator<GroupListBean> CREATOR = new Creator<GroupListBean>() {
        @Override public GroupListBean createFromParcel(Parcel source) {
            return new GroupListBean(source);
        }

        @Override public GroupListBean[] newArray(int size) {
            return new GroupListBean[size];
        }
    };
    public String id;
    public String name;
    public int count;
    public String username;

    public GroupListBean() {
    }

    protected GroupListBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.count = in.readInt();
        this.username = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeString(this.username);
    }
}
