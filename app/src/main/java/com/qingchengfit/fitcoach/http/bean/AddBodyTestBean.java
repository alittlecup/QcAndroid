package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
public class AddBodyTestBean {
    public String model;
    public String id;
    public String user_id;
    public String height;
    public String weight;
    public String bmi;
    public String body_fat_rate;
    public String circumference_of_left_upper;
    public String circumference_of_right_upper;
    public String circumference_of_chest;
    public String waistline;
    public String hipline;
    public String circumference_of_left_thigh;
    public String circumference_of_right_thigh;
    public String circumference_of_left_calf;
    public String circumference_of_right_calf;
    public List<Photo> photos;
    public String extra;
    public String created_at;

    public static class Photo implements Parcelable {
        public String photo;
        public boolean isLoading;

        public Photo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.photo);
            dest.writeByte(isLoading ? (byte) 1 : (byte) 0);
        }

        protected Photo(Parcel in) {
            this.photo = in.readString();
            this.isLoading = in.readByte() != 0;
        }

        public static final Creator<Photo> CREATOR = new Creator<Photo>() {
            public Photo createFromParcel(Parcel source) {
                return new Photo(source);
            }

            public Photo[] newArray(int size) {
                return new Photo[size];
            }
        };
    }
}
