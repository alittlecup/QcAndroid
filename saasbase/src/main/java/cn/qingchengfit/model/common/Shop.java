package cn.qingchengfit.model.common;

import android.os.Parcel;
import android.os.Parcelable;

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
 * Created by Paper on 16/2/23 2016.
 */
public class Shop implements Parcelable {
    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override public Shop createFromParcel(Parcel source) {
            return new Shop(source);
        }

        @Override public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };
    public String id;
    public String name;
    public String address;
    public String phone;
    public String description;
    public String photo;
    public String gd_district_id;
    public double gd_lat;
    public double gd_lng;
    public String system_end;
    public String contact;

    public Shop() {
    }

    private Shop(Builder builder) {
        id = builder.id;
        name = builder.name;
        address = builder.address;
        phone = builder.phone;
        description = builder.description;
        photo = builder.photo;
        gd_district_id = builder.gd_district_id;
        gd_lat = builder.gd_lat;
        gd_lng = builder.gd_lng;
        system_end = builder.system_end;
        contact = builder.contact;
    }

    protected Shop(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.description = in.readString();
        this.photo = in.readString();
        this.gd_district_id = in.readString();
        this.gd_lat = in.readDouble();
        this.gd_lng = in.readDouble();
        this.system_end = in.readString();
        this.contact = in.readString();
    }

    @Override public boolean equals(Object obj) {
        return ((Shop) obj).id.equalsIgnoreCase(id);
    }

    @Override public int hashCode() {
        return id.hashCode();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.description);
        dest.writeString(this.photo);
        dest.writeString(this.gd_district_id);
        dest.writeDouble(this.gd_lat);
        dest.writeDouble(this.gd_lng);
        dest.writeString(this.system_end);
        dest.writeString(this.contact);
    }

    public static final class Builder {
        private String id;
        private String name;
        private String address;
        private String phone;
        private String description;
        private String photo;
        private String gd_district_id;
        private double gd_lat;
        private double gd_lng;
        private String system_end;
        private String contact;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder photo(String val) {
            photo = val;
            return this;
        }

        public Builder gd_district_id(String val) {
            gd_district_id = val;
            return this;
        }

        public Builder gd_lat(double val) {
            gd_lat = val;
            return this;
        }

        public Builder gd_lng(double val) {
            gd_lng = val;
            return this;
        }

        public Builder system_end(String val) {
            system_end = val;
            return this;
        }

        public Builder contact(String val) {
            contact = val;
            return this;
        }

        public Shop build() {
            return new Shop(this);
        }
    }
}
