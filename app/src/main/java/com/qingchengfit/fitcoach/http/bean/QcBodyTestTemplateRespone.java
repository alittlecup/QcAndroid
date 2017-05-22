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
 * Created by Paper on 16/1/13 2016.
 */
public class QcBodyTestTemplateRespone extends QcResponse {

    @SerializedName("data") public Data data;

    public static class Base implements Parcelable {
        public static final Creator<Base> CREATOR = new Creator<Base>() {
            @Override public Base createFromParcel(Parcel source) {
                return new Base(source);
            }

            @Override public Base[] newArray(int size) {
                return new Base[size];
            }
        };
        @SerializedName("show_circumference_of_right_thigh")//大腿
        public boolean show_circumference_of_right_thigh;
        @SerializedName("show_circumference_of_left_thigh")//大腿
        public boolean show_circumference_of_left_thigh;
        @SerializedName("show_weight")  //体重
        public boolean show_weight;
        @SerializedName("show_bmi") public boolean show_bmi;
        @SerializedName("show_circumference_of_right_upper") public boolean show_circumference_of_right_upper;//上臂围
        @SerializedName("show_circumference_of_left_upper") public boolean show_circumference_of_left_upper;//上臂围
        @SerializedName("id") public String id;
        @SerializedName("show_hipline") //臀围
        public boolean show_hipline;
        @SerializedName("show_waistline") //腰围
        public boolean show_waistline;
        @SerializedName("show_height")//身高
        public boolean show_height;
        @SerializedName("show_circumference_of_right_calf")//小腿围
        public boolean show_circumference_of_right_calf;
        @SerializedName("show_circumference_of_left_calf")//小腿围
        public boolean show_circumference_of_left_calf;
        @SerializedName("show_circumference_of_chest") //胸围
        public boolean show_circumference_of_chest;
        @SerializedName("show_body_fat_rate")   //体质比
        public boolean show_body_fat_rate;

        public Base() {
        }

        protected Base(Parcel in) {
            this.show_circumference_of_right_thigh = in.readByte() != 0;
            this.show_circumference_of_left_thigh = in.readByte() != 0;
            this.show_weight = in.readByte() != 0;
            this.show_bmi = in.readByte() != 0;
            this.show_circumference_of_right_upper = in.readByte() != 0;
            this.show_circumference_of_left_upper = in.readByte() != 0;
            this.id = in.readString();
            this.show_hipline = in.readByte() != 0;
            this.show_waistline = in.readByte() != 0;
            this.show_height = in.readByte() != 0;
            this.show_circumference_of_right_calf = in.readByte() != 0;
            this.show_circumference_of_left_calf = in.readByte() != 0;
            this.show_circumference_of_chest = in.readByte() != 0;
            this.show_body_fat_rate = in.readByte() != 0;
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.show_circumference_of_right_thigh ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_circumference_of_left_thigh ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_weight ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_bmi ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_circumference_of_right_upper ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_circumference_of_left_upper ? (byte) 1 : (byte) 0);
            dest.writeString(this.id);
            dest.writeByte(this.show_hipline ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_waistline ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_height ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_circumference_of_right_calf ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_circumference_of_left_calf ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_circumference_of_chest ? (byte) 1 : (byte) 0);
            dest.writeByte(this.show_body_fat_rate ? (byte) 1 : (byte) 0);
        }
    }

    public static class Extra implements Parcelable {
        public static final Creator<Extra> CREATOR = new Creator<Extra>() {
            public Extra createFromParcel(Parcel source) {
                return new Extra(source);
            }

            public Extra[] newArray(int size) {
                return new Extra[size];
            }
        };
        @SerializedName("id") public String id;
        @SerializedName("unit") public String unit;
        @SerializedName("name") public String name;
        @SerializedName("value") public String value;

        public Extra() {
        }

        protected Extra(Parcel in) {
            this.id = in.readString();
            this.unit = in.readString();
            this.name = in.readString();
            this.value = in.readString();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.unit);
            dest.writeString(this.name);
            dest.writeString(this.value);
        }
    }

    public class Data {
        @SerializedName("template") public Template template;
    }

    public class Template {
        @SerializedName("base") public Base base;
        @SerializedName("extra") public List<Extra> extra;
    }
}
