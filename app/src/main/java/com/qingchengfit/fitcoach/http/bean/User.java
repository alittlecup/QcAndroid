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
 * Created by Paper on 15/8/13 2015.
 */
public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("username") public String username;
    @SerializedName("phone") public String phone;
    @SerializedName("id") public String id;
    @SerializedName("city") public String city;
    @SerializedName("description") public String desc;
    @SerializedName("avatar") public String avatar;
    @SerializedName("address") public String address;
    @SerializedName("joined_at") public String joined_at;
    @SerializedName("hidden_phone") public String hidden_phone;
    @SerializedName("date_of_birth") public String date_of_birth;
    @SerializedName("gender") public int gender;

    public User() {
    }

    protected User(Parcel in) {
        this.username = in.readString();
        this.phone = in.readString();
        this.id = in.readString();
        this.city = in.readString();
        this.desc = in.readString();
        this.avatar = in.readString();
        this.address = in.readString();
        this.joined_at = in.readString();
        this.hidden_phone = in.readString();
        this.date_of_birth = in.readString();
        this.gender = in.readInt();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(String joined_at) {
        this.joined_at = joined_at;
    }

    public String getHidden_phone() {
        return hidden_phone;
    }

    public void setHidden_phone(String hidden_phone) {
        this.hidden_phone = hidden_phone;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.phone);
        dest.writeString(this.id);
        dest.writeString(this.city);
        dest.writeString(this.desc);
        dest.writeString(this.avatar);
        dest.writeString(this.address);
        dest.writeString(this.joined_at);
        dest.writeString(this.hidden_phone);
        dest.writeString(this.date_of_birth);
        dest.writeInt(this.gender);
    }
}
