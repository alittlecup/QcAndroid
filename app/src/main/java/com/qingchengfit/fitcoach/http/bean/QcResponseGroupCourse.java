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
 * Created by Paper on 16/4/30 2016.
 */
public class QcResponseGroupCourse extends QcResponse {

    @SerializedName("data")
    public Data data;


    public class Data {
        @SerializedName("courses")
        public List<GroupClass> courses;

        @SerializedName("order_url")
        public String order_url;
        @SerializedName("total_count")
        public int total_count;

    }

    public static class GroupClass implements Parcelable {

        @SerializedName("count")
        public int count;

        @SerializedName("name")
        public String name;

        @SerializedName("photo")
        public String photo;

        @SerializedName("from_date")
        public String from_date;

        @SerializedName("to_date")
        public String to_date;

        @SerializedName("id")
        public String id;
        @SerializedName("length")
        public String length;

        public GroupClass() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.count);
            dest.writeString(this.name);
            dest.writeString(this.photo);
            dest.writeString(this.from_date);
            dest.writeString(this.to_date);
            dest.writeString(this.id);
            dest.writeString(this.length);
        }

        protected GroupClass(Parcel in) {
            this.count = in.readInt();
            this.name = in.readString();
            this.photo = in.readString();
            this.from_date = in.readString();
            this.to_date = in.readString();
            this.id = in.readString();
            this.length = in.readString();
        }

        public static final Creator<GroupClass> CREATOR = new Creator<GroupClass>() {
            @Override
            public GroupClass createFromParcel(Parcel source) {
                return new GroupClass(source);
            }

            @Override
            public GroupClass[] newArray(int size) {
                return new GroupClass[size];
            }
        };
    }

}
