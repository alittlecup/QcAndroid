package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import cn.qingchengfit.widgets.R;

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
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * student Staff coach sale property base class.
 * //Created by yangming on 16/11/18.
 */

public class Personage implements Parcelable {

    public String tag;
    public String id;
    public String username;
    public String area_code;
    public String phone;
    public String avatar;
    public String checkin_avatar;
    public int gender;// 0:man；1：woman
    public String head;
    public String brand_id;
    public boolean is_superuser;



    public Personage() {
    }

    public Personage(String username, String phone, String avatar, int gender) {
        this.username = username;
        this.phone = phone;
        this.avatar = avatar;
        this.gender = gender;
    }

    public Personage(String username, String phone, String avatar, String area_code, int gender) {
        this.username = username;
        this.phone = phone;
        this.area_code = area_code;
        this.avatar = avatar;
        this.gender = gender;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCheckin_avatar() {
        return checkin_avatar;
    }

    public void setCheckin_avatar(String checkin_avatar) {
        this.checkin_avatar = checkin_avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Personage) {
            return ((Personage) obj).getId().equals(getId());
        } else return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @DrawableRes
    public int getDefaultAvatar() {
        if (gender == 0) {
            return R.drawable.default_student_male;
        } else {
            return R.drawable.default_student_female;
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.area_code);
        dest.writeString(this.phone);
        dest.writeString(this.avatar);
        dest.writeString(this.checkin_avatar);
        dest.writeInt(this.gender);
        dest.writeString(this.head);
        dest.writeString(this.brand_id);
        dest.writeByte(this.is_superuser ? (byte) 1 : (byte) 0);
    }

    protected Personage(Parcel in) {
        this.tag = in.readString();
        this.id = in.readString();
        this.username = in.readString();
        this.area_code = in.readString();
        this.phone = in.readString();
        this.avatar = in.readString();
        this.checkin_avatar = in.readString();
        this.gender = in.readInt();
        this.head = in.readString();
        this.brand_id = in.readString();
        this.is_superuser = in.readByte() != 0;
    }
}
