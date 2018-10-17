package cn.qingchengfit.saascommon.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Advertisement implements Parcelable {
    public String ad_text;
    public List<AdvertiseInfo> ad_data;
    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {

        @Override
        public Advertisement createFromParcel(Parcel source) {
            return new Advertisement(source);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }

    };

    public Advertisement() {}

    protected Advertisement(Parcel in) {
        this.ad_text = in.readString();
        this.ad_data = in.createTypedArrayList(AdvertiseInfo.CREATOR);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ad_text);
        dest.writeTypedList(this.ad_data);
    }
}
