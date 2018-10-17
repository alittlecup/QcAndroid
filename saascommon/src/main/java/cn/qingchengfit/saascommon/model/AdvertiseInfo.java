package cn.qingchengfit.saascommon.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bob Du on 2018/10/17 13:11
 */
public class AdvertiseInfo implements Parcelable {
    public String web_url;
    public String photo_url;

    public static final Creator<AdvertiseInfo> CREATOR = new Creator<AdvertiseInfo>() {
        @Override
        public AdvertiseInfo createFromParcel(Parcel source) {
            return new AdvertiseInfo(source);
        }

        @Override
        public AdvertiseInfo[] newArray(int size) {
            return new AdvertiseInfo[size];
        }
    };

    public AdvertiseInfo() {}

    protected AdvertiseInfo(Parcel in) {
        this.web_url = in.readString();
        this.photo_url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.web_url);
        dest.writeString(this.photo_url);
    }
}
