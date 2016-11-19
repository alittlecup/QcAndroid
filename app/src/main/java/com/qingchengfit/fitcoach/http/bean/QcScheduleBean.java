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
 * Created by Paper on 15/10/13 2015.
 */
public class QcScheduleBean {
    @SerializedName("count")
    public int count;
    @SerializedName("end")
    public String end;
    @SerializedName("start")
    public String start;
    @SerializedName("id")
    public int id;
    @SerializedName("url")
    public String url;
    @SerializedName("shop")
    public Shop shop;
    @SerializedName("course")
    public Course course;
    @SerializedName("users")
    public String users;
    @SerializedName("orders")
    public List<Order> orders;

    public static class Order {
        @SerializedName("count")
        public int count;
        @SerializedName("username")
        public String username;
    }

    public static class Shop implements Parcelable {
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.id);
        }

        public Shop() {
        }

        protected Shop(Parcel in) {
            this.name = in.readString();
            this.id = in.readString();
        }

        public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
            @Override
            public Shop createFromParcel(Parcel source) {
                return new Shop(source);
            }

            @Override
            public Shop[] newArray(int size) {
                return new Shop[size];
            }
        };
    }

    public static class Course {
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;

    }
}
