package cn.qingchengfit.saasbase.student.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangbaole on 2017/12/7.
 */

public class SourceBean implements Parcelable {
    public String id;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public SourceBean() {
    }

    protected SourceBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<SourceBean> CREATOR = new Parcelable.Creator<SourceBean>() {
        @Override
        public SourceBean createFromParcel(Parcel source) {
            return new SourceBean(source);
        }

        @Override
        public SourceBean[] newArray(int size) {
            return new SourceBean[size];
        }
    };
}
