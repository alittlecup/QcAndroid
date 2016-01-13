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
 * Created by Paper on 16/1/13 2016.
 */
public class QcBodyTestTemplateRespone extends QcResponse {

    @SerializedName("data")
    public Data data;
    public class Data{
        @SerializedName("template")
        public Template template;
    }
    public class Template{
        @SerializedName("base")
        public Base base;
        @SerializedName("extra")
        public Base extra;
    }
    public static class Base implements Parcelable {
        @SerializedName("show_circumference_of_thigh")//大腿
        public boolean show_circumference_of_thigh;
        @SerializedName("show_weight")  //体重
        public boolean show_weight;
        @SerializedName("show_bmi")
        public boolean show_bmi;
        @SerializedName("show_circumference_of_upper")
        public boolean show_circumference_of_upper;//上臂围
        @SerializedName("id")
        public boolean id;
        @SerializedName("show_hipline") //臀围
        public boolean show_hipline;
        @SerializedName("show_waistline") //腰围
        public boolean show_waistline;
        @SerializedName("show_height")//身高
        public boolean show_height;
        @SerializedName("show_circumference_of_calf")//小腿围
        public boolean show_circumference_of_calf;
        @SerializedName("show_circumference_of_chest") //胸围
        public boolean show_circumference_of_chest;
        @SerializedName("show_body_fat_rate")   //体质比
        public boolean show_body_fat_rate;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.show_circumference_of_thigh);
            dest.writeString(this.show_weight);
            dest.writeString(this.show_bmi);
            dest.writeString(this.show_circumference_of_upper);
            dest.writeString(this.id);
            dest.writeString(this.show_hipline);
            dest.writeString(this.show_waistline);
            dest.writeString(this.show_height);
            dest.writeString(this.show_circumference_of_calf);
            dest.writeString(this.show_circumference_of_chest);
            dest.writeString(this.show_body_fat_rate);
        }

        public Base() {
        }

        protected Base(Parcel in) {
            this.show_circumference_of_thigh = in.readString();
            this.show_weight = in.readString();
            this.show_bmi = in.readString();
            this.show_circumference_of_upper = in.readString();
            this.id = in.readString();
            this.show_hipline = in.readString();
            this.show_waistline = in.readString();
            this.show_height = in.readString();
            this.show_circumference_of_calf = in.readString();
            this.show_circumference_of_chest = in.readString();
            this.show_body_fat_rate = in.readString();
        }

        public static final Parcelable.Creator<Base> CREATOR = new Parcelable.Creator<Base>() {
            public Base createFromParcel(Parcel source) {
                return new Base(source);
            }

            public Base[] newArray(int size) {
                return new Base[size];
            }
        };
    }
    public class Extra{
        @SerializedName("id")
        public String id;
        @SerializedName("unit")
        public String unit;
        @SerializedName("name")
        public String name;
    }

}
