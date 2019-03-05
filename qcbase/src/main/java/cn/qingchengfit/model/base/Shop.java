package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

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
 * //Created by yangming on 16/11/19.
 */

public class Shop implements Parcelable {

  @SerializedName("id") public String id;
  @SerializedName("address") public String address;
  @SerializedName("phone") public String phone;
  @SerializedName("name") public String name;
  @SerializedName("photo") public String photo;
  public String gd_district_id;
  public double gd_lat;
  public double gd_lng;
  @SerializedName("system_end") public String system_end;
  @SerializedName("contact") public String contact;
  public Staff superuser;
  @SerializedName("position") public String position;

  @SerializedName("image") public String image;
  @SerializedName("has_permission") public boolean has_permission;
  @SerializedName("logo") public String logo;
  public String description;
  public String weixin_image;
  public boolean weixin_success;
  public String brand_id;
  public String weixin;

  public int area;
  public int gym_type;

  public Shop() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.address);
    dest.writeString(this.phone);
    dest.writeString(this.name);
    dest.writeString(this.photo);
    dest.writeString(this.gd_district_id);
    dest.writeDouble(this.gd_lat);
    dest.writeDouble(this.gd_lng);
    dest.writeString(this.system_end);
    dest.writeString(this.contact);
    dest.writeParcelable(this.superuser, flags);
    dest.writeString(this.position);
    dest.writeString(this.image);
    dest.writeByte(this.has_permission ? (byte) 1 : (byte) 0);
    dest.writeString(this.logo);
    dest.writeString(this.description);
    dest.writeString(this.weixin_image);
    dest.writeByte(this.weixin_success ? (byte) 1 : (byte) 0);
    dest.writeString(this.brand_id);
    dest.writeString(this.weixin);
    dest.writeInt(this.area);
    dest.writeInt(this.gym_type);
  }

  protected Shop(Parcel in) {
    this.id = in.readString();
    this.address = in.readString();
    this.phone = in.readString();
    this.name = in.readString();
    this.photo = in.readString();
    this.gd_district_id = in.readString();
    this.gd_lat = in.readDouble();
    this.gd_lng = in.readDouble();
    this.system_end = in.readString();
    this.contact = in.readString();
    this.superuser = in.readParcelable(Staff.class.getClassLoader());
    this.position = in.readString();
    this.image = in.readString();
    this.has_permission = in.readByte() != 0;
    this.logo = in.readString();
    this.description = in.readString();
    this.weixin_image = in.readString();
    this.weixin_success = in.readByte() != 0;
    this.brand_id = in.readString();
    this.weixin = in.readString();
    this.area = in.readInt();
    this.gym_type = in.readInt();
  }

  public static final Creator<Shop> CREATOR = new Creator<Shop>() {
    @Override public Shop createFromParcel(Parcel source) {
      return new Shop(source);
    }

    @Override public Shop[] newArray(int size) {
      return new Shop[size];
    }
  };
}

