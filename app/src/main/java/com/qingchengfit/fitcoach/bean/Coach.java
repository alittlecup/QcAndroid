package com.qingchengfit.fitcoach.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/2 2016.
 */
public class Coach implements Parcelable {
    @SerializedName("username")
    public String name;
    @SerializedName("gender")
    public int gender;
    @SerializedName("phone")
    public String phone;
    @SerializedName("avatar")
    public String header;
    @SerializedName("id")
    public String id;


    public Coach(String name, int gender, String phone, String header) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.header = header;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.gender);
        dest.writeString(this.phone);
        dest.writeString(this.header);
        dest.writeString(this.id);
    }

    protected Coach(Parcel in) {
        this.name = in.readString();
        this.gender = in.readInt();
        this.phone = in.readString();
        this.header = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<Coach> CREATOR = new Parcelable.Creator<Coach>() {
        @Override
        public Coach createFromParcel(Parcel source) {
            return new Coach(source);
        }

        @Override
        public Coach[] newArray(int size) {
            return new Coach[size];
        }
    };
}
