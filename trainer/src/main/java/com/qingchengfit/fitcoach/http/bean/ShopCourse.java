package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/12/29 2015.
 */
public class ShopCourse implements Parcelable {
    public static final Parcelable.Creator<ShopCourse> CREATOR = new Parcelable.Creator<ShopCourse>() {
        public ShopCourse createFromParcel(Parcel source) {
            return new ShopCourse(source);
        }

        public ShopCourse[] newArray(int size) {
            return new ShopCourse[size];
        }
    };
    @SerializedName("name") public String name;
    @SerializedName("tags") public String tags;
    @SerializedName("image_url") public String image_url;
    @SerializedName("course_count") public int course_count;
    @SerializedName("length") public int length;
    @SerializedName("service_count") public int service_count;
    @SerializedName("is_private") public boolean is_private;
    @SerializedName("id") public long id;

    public ShopCourse() {
    }

    protected ShopCourse(Parcel in) {
        this.name = in.readString();
        this.tags = in.readString();
        this.image_url = in.readString();
        this.course_count = in.readInt();
        this.length = in.readInt();
        this.service_count = in.readInt();
        this.is_private = in.readByte() != 0;
        this.id = in.readLong();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.tags);
        dest.writeString(this.image_url);
        dest.writeInt(this.course_count);
        dest.writeInt(this.length);
        dest.writeInt(this.service_count);
        dest.writeByte(is_private ? (byte) 1 : (byte) 0);
        dest.writeLong(this.id);
    }
}
