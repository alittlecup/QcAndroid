package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 15/9/18 2015.
 */
public class CityBean implements Parcelable {
    @SerializedName("code")
    public int id;
    @SerializedName("gd_province_id")
    public int province_id;
    @SerializedName("name")
    public String name;
    @SerializedName("districts")
    public List<DistrictBean> districts;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.province_id);
        dest.writeString(this.name);
        dest.writeList(this.districts);
    }

    public CityBean() {
    }

    protected CityBean(Parcel in) {
        this.id = in.readInt();
        this.province_id = in.readInt();
        this.name = in.readString();
        this.districts = new ArrayList<DistrictBean>();
        in.readList(this.districts, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<CityBean> CREATOR = new Parcelable.Creator<CityBean>() {
        public CityBean createFromParcel(Parcel source) {
            return new CityBean(source);
        }

        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };
}
