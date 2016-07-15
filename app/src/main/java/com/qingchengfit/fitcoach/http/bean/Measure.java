package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

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
 * Created by Paper on 16/1/14 2016.
 */
public class Measure implements Parcelable {

    @SerializedName("weight")
    public String weight;
    @SerializedName("waistline")
    public String waistline;
    @SerializedName("height")
    public String height;
    @SerializedName("hipline")
    public String hipline;
    @SerializedName("circumference_of_left_calf") //小腿
    public String circumference_of_left_calf;
    @SerializedName("circumference_of_right_calf") //小腿
    public String circumference_of_right_calf;
    @SerializedName("circumference_of_left_thigh") //大腿
    public String circumference_of_left_thigh;
    @SerializedName("circumference_of_right_thigh") //大腿
    public String circumference_of_right_thigh;
    @SerializedName("bmi")
    public String bmi;
    @SerializedName("body_fat_rate")
    public String body_fat_rate;
    @SerializedName("circumference_of_left_upper")
    public String circumference_of_left_upper;
    @SerializedName("circumference_of_right_upper")
    public String circumference_of_right_upper;
    @SerializedName("circumference_of_chest")
    public String circumference_of_chest;
    @SerializedName("photos")
    public List<AddBodyTestBean.Photo> photos;
    @SerializedName("extra")
    public List<QcBodyTestTemplateRespone.Extra> extra;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("id")
    public String id;

    public Measure() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weight);
        dest.writeString(this.waistline);
        dest.writeString(this.height);
        dest.writeString(this.hipline);
        dest.writeString(this.circumference_of_left_calf);
        dest.writeString(this.circumference_of_right_calf);
        dest.writeString(this.circumference_of_left_thigh);
        dest.writeString(this.circumference_of_right_thigh);
        dest.writeString(this.bmi);
        dest.writeString(this.body_fat_rate);
        dest.writeString(this.circumference_of_left_upper);
        dest.writeString(this.circumference_of_right_upper);
        dest.writeString(this.circumference_of_chest);
        dest.writeTypedList(this.photos);
        dest.writeTypedList(this.extra);
        dest.writeString(this.created_at);
        dest.writeString(this.id);
    }

    protected Measure(Parcel in) {
        this.weight = in.readString();
        this.waistline = in.readString();
        this.height = in.readString();
        this.hipline = in.readString();
        this.circumference_of_left_calf = in.readString();
        this.circumference_of_right_calf = in.readString();
        this.circumference_of_left_thigh = in.readString();
        this.circumference_of_right_thigh = in.readString();
        this.bmi = in.readString();
        this.body_fat_rate = in.readString();
        this.circumference_of_left_upper = in.readString();
        this.circumference_of_right_upper = in.readString();
        this.circumference_of_chest = in.readString();
        this.photos = in.createTypedArrayList(AddBodyTestBean.Photo.CREATOR);
        this.extra = in.createTypedArrayList(QcBodyTestTemplateRespone.Extra.CREATOR);
        this.created_at = in.readString();
        this.id = in.readString();
    }

    public static final Creator<Measure> CREATOR = new Creator<Measure>() {
        @Override
        public Measure createFromParcel(Parcel source) {
            return new Measure(source);
        }

        @Override
        public Measure[] newArray(int size) {
            return new Measure[size];
        }
    };
}
