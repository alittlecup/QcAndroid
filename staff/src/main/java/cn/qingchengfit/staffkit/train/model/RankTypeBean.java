package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/3/22.
 */

public class RankTypeBean implements Parcelable {

    public int count;
    public String rank_country;
    public String rank_gym;
    public int together_count;

    public RankTypeBean() {
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeString(this.rank_country);
        dest.writeString(this.rank_gym);
        dest.writeInt(this.together_count);
    }

    protected RankTypeBean(Parcel in) {
        this.count = in.readInt();
        this.rank_country = in.readString();
        this.rank_gym = in.readString();
        this.together_count = in.readInt();
    }

    public static final Creator<RankTypeBean> CREATOR = new Creator<RankTypeBean>() {
        @Override public RankTypeBean createFromParcel(Parcel source) {
            return new RankTypeBean(source);
        }

        @Override public RankTypeBean[] newArray(int size) {
            return new RankTypeBean[size];
        }
    };
}
