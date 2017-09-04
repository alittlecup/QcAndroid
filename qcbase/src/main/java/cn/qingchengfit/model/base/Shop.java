package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/11/15.
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
    public String description;
    public String address;
    public String phone;
    public String name;
    public String photo;
    public String gd_district_id;
    public double gd_lat;
    public double gd_lng;
    public String system_end;
    public String contact;
    public User superuser;
    public String position;

    private Shop(Builder builder) {
        id = builder.id;
        description = builder.description;
        address = builder.address;
        phone = builder.phone;
        name = builder.name;
        photo = builder.photo;
        gd_district_id = builder.gd_district_id;
        gd_lat = builder.gd_lat;
        gd_lng = builder.gd_lng;
    }

    protected Shop(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.name = in.readString();
        this.photo = in.readString();
        this.gd_district_id = in.readString();
        this.gd_lat = in.readDouble();
        this.gd_lng = in.readDouble();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.gd_district_id);
        dest.writeDouble(this.gd_lat);
        dest.writeDouble(this.gd_lng);
    }

    public static final class Builder {
        private String id;
        private String description;
        private String address;
        private String phone;
        private String name;
        private String photo;
        private String gd_district_id;
        private double gd_lat;
        private double gd_lng;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
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

        public Builder name(String val) {
            name = val;
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

        public Shop build() {
            return new Shop(this);
        }
    }
}
