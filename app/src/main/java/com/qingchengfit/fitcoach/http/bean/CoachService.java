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
 * Created by Paper on 15/12/28 2015.
 */
public class CoachService implements Parcelable {
    @SerializedName("model")
    public String model;
    @SerializedName("type")
    public int type;
    @SerializedName("id")
    public long id;
    @SerializedName("name")
    public String name;
    @SerializedName("color")
    public String color;
    @SerializedName("photo")
    public String photo;
    @SerializedName("host")
    public String host;
     @SerializedName("brand_name")
    public String brand_name;
    @SerializedName("courses_count")
    public int courses_count;
    @SerializedName("users_count")
    public int users_count;

    private CoachService(Builder builder) {
        setModel(builder.model);
        setType(builder.type);
        setId(builder.id);
        setName(builder.name);
        setColor(builder.color);
        setPhoto(builder.photo);
        setHost(builder.host);
        setBrand_name(builder.brand_name);
        setCourses_count(builder.courses_count);
        setUsers_count(builder.users_count);
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public int getCourses_count() {
        return courses_count;
    }

    public void setCourses_count(int courses_count) {
        this.courses_count = courses_count;
    }

    public int getUsers_count() {
        return users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.model);
        dest.writeInt(this.type);
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.color);
        dest.writeString(this.photo);
        dest.writeString(this.host);
        dest.writeString(this.brand_name);
        dest.writeInt(this.courses_count);
        dest.writeInt(this.users_count);
    }

    public CoachService() {
    }

    protected CoachService(Parcel in) {
        this.model = in.readString();
        this.type = in.readInt();
        this.id = in.readLong();
        this.name = in.readString();
        this.color = in.readString();
        this.photo = in.readString();
        this.host = in.readString();
        this.brand_name = in.readString();
        this.courses_count = in.readInt();
        this.users_count = in.readInt();
    }

    public static final Creator<CoachService> CREATOR = new Creator<CoachService>() {
        @Override
        public CoachService createFromParcel(Parcel source) {
            return new CoachService(source);
        }

        @Override
        public CoachService[] newArray(int size) {
            return new CoachService[size];
        }
    };

    public static final class Builder {
        private String model;
        private int type;
        private long id;
        private String name;
        private String color;
        private String photo;
        private String host;
        private String brand_name;
        private int courses_count;
        private int users_count;

        public Builder() {
        }

        public Builder model(String val) {
            model = val;
            return this;
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder color(String val) {
            color = val;
            return this;
        }

        public Builder photo(String val) {
            photo = val;
            return this;
        }

        public Builder host(String val) {
            host = val;
            return this;
        }

        public Builder brand_name(String val) {
            brand_name = val;
            return this;
        }

        public Builder courses_count(int val) {
            courses_count = val;
            return this;
        }

        public Builder users_count(int val) {
            users_count = val;
            return this;
        }

        public CoachService build() {
            return new CoachService(this);
        }
    }
}
