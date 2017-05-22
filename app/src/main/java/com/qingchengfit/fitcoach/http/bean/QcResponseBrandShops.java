package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.Brand;
import java.util.List;

/**
 * Created by peggy on 16/5/28.
 */

public class QcResponseBrandShops extends QcResponse {

    @SerializedName("data") public Data data;

    public static class BrandShop implements Parcelable {
        public static final Creator<BrandShop> CREATOR = new Creator<BrandShop>() {
            @Override public BrandShop createFromParcel(Parcel source) {
                return new BrandShop(source);
            }

            @Override public BrandShop[] newArray(int size) {
                return new BrandShop[size];
            }
        };
        @SerializedName("name") public String name;
        @SerializedName("image") public String image;
        @SerializedName("photo") public String photo;
        @SerializedName("contact") public String contact;
        @SerializedName("phone") public String phone;
        @SerializedName("address") public String address;
        @SerializedName("id") public String id;
        @SerializedName("has_permission") public boolean has_permission;
        @SerializedName("system_end") public String system_end;
        @SerializedName("position") public String position;

        public BrandShop() {
        }

        protected BrandShop(Parcel in) {
            this.name = in.readString();
            this.image = in.readString();
            this.photo = in.readString();
            this.contact = in.readString();
            this.phone = in.readString();
            this.address = in.readString();
            this.id = in.readString();
            this.has_permission = in.readByte() != 0;
            this.system_end = in.readString();
            this.position = in.readString();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.image);
            dest.writeString(this.photo);
            dest.writeString(this.contact);
            dest.writeString(this.phone);
            dest.writeString(this.address);
            dest.writeString(this.id);
            dest.writeByte(this.has_permission ? (byte) 1 : (byte) 0);
            dest.writeString(this.system_end);
            dest.writeString(this.position);
        }
    }

    public class Data {
        @SerializedName("shops") public List<BrandShop> shops;
        @SerializedName("brand") public Brand brand;
    }
}
