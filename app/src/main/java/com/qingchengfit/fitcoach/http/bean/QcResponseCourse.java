package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
 * Created by Paper on 15/10/14 2015.
 */
public class QcResponseCourse extends QcResponse {

    @SerializedName("data")
    public Courses data;

    public class Courses {
        @SerializedName("shop")
        public Shop shop;
        @SerializedName("service")
        public CoachService service;
    }

    public class Shop {
        @SerializedName("private_url")
        public String private_url;
        @SerializedName("team_url")
        public String team_url;
        @SerializedName("user_count")
        public int user_count;
        @SerializedName("courses_count")
        public int courses_count;
        @SerializedName("team_count")
        public int team_count;
        @SerializedName("courses")
        public List<Course> courses;
    }


    public static class Course implements Parcelable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_url")
        public String image_url;
        @SerializedName("photo")
        public String photo;
        @SerializedName("is_private")
        public boolean is_private;
        @SerializedName("course_count")
        public int course_count;
        @SerializedName("length")
        public int length;

        @SerializedName("service_count")
        public int service_count;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.photo);
            dest.writeByte(is_private ? (byte) 1 : (byte) 0);
        }

        public Course() {
        }

        protected Course(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.photo = in.readString();
            this.is_private = in.readByte() != 0;
        }

        public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
            @Override
            public Course createFromParcel(Parcel source) {
                return new Course(source);
            }

            @Override
            public Course[] newArray(int size) {
                return new Course[size];
            }
        };
    }
}
