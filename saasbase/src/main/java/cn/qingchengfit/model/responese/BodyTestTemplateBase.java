package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   阿弥陀佛
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/11/17.
 */

public class BodyTestTemplateBase implements Parcelable {

    public static final Parcelable.Creator<BodyTestTemplateBase> CREATOR = new Parcelable.Creator<BodyTestTemplateBase>() {
        @Override public BodyTestTemplateBase createFromParcel(Parcel source) {
            return new BodyTestTemplateBase(source);
        }

        @Override public BodyTestTemplateBase[] newArray(int size) {
            return new BodyTestTemplateBase[size];
        }
    };
    @SerializedName("show_circumference_of_left_thigh")//大腿
    public boolean show_circumference_of_left_thigh;
    @SerializedName("show_circumference_of_right_thigh")//大腿
    public boolean show_circumference_of_right_thigh;
    @SerializedName("show_weight")  //体重
    public boolean show_weight;
    @SerializedName("show_bmi") public boolean show_bmi;
    @SerializedName("show_circumference_of_left_upper") public boolean show_circumference_of_left_upper;//上臂围
    @SerializedName("show_circumference_of_right_upper") public boolean show_circumference_of_right_upper;//上臂围
    @SerializedName("id") public String id;
    @SerializedName("show_hipline") //臀围
    public boolean show_hipline;
    @SerializedName("show_waistline") //腰围
    public boolean show_waistline;
    @SerializedName("show_height")//身高
    public boolean show_height;
    @SerializedName("show_circumference_of_left_calf")//小腿围
    public boolean show_circumference_of_left_calf;
    @SerializedName("show_circumference_of_right_calf")//小腿围
    public boolean show_circumference_of_right_calf;
    @SerializedName("show_circumference_of_chest") //胸围
    public boolean show_circumference_of_chest;
    @SerializedName("show_body_fat_rate")   //体质比
    public boolean show_body_fat_rate;

    public BodyTestTemplateBase() {
    }

    protected BodyTestTemplateBase(Parcel in) {
        this.show_circumference_of_left_thigh = in.readByte() != 0;
        this.show_circumference_of_right_thigh = in.readByte() != 0;
        this.show_weight = in.readByte() != 0;
        this.show_bmi = in.readByte() != 0;
        this.show_circumference_of_left_upper = in.readByte() != 0;
        this.show_circumference_of_right_upper = in.readByte() != 0;
        this.id = in.readString();
        this.show_hipline = in.readByte() != 0;
        this.show_waistline = in.readByte() != 0;
        this.show_height = in.readByte() != 0;
        this.show_circumference_of_left_calf = in.readByte() != 0;
        this.show_circumference_of_right_calf = in.readByte() != 0;
        this.show_circumference_of_chest = in.readByte() != 0;
        this.show_body_fat_rate = in.readByte() != 0;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(show_circumference_of_left_thigh ? (byte) 1 : (byte) 0);
        dest.writeByte(show_circumference_of_right_thigh ? (byte) 1 : (byte) 0);
        dest.writeByte(show_weight ? (byte) 1 : (byte) 0);
        dest.writeByte(show_bmi ? (byte) 1 : (byte) 0);
        dest.writeByte(show_circumference_of_left_upper ? (byte) 1 : (byte) 0);
        dest.writeByte(show_circumference_of_right_upper ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeByte(show_hipline ? (byte) 1 : (byte) 0);
        dest.writeByte(show_waistline ? (byte) 1 : (byte) 0);
        dest.writeByte(show_height ? (byte) 1 : (byte) 0);
        dest.writeByte(show_circumference_of_left_calf ? (byte) 1 : (byte) 0);
        dest.writeByte(show_circumference_of_right_calf ? (byte) 1 : (byte) 0);
        dest.writeByte(show_circumference_of_chest ? (byte) 1 : (byte) 0);
        dest.writeByte(show_body_fat_rate ? (byte) 1 : (byte) 0);
    }
}
