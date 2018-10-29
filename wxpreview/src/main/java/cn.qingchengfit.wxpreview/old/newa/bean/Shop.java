package cn.qingchengfit.wxpreview.old.newa.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.MiniProgram;
import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;

public class Shop implements Parcelable {

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
  @SerializedName("logo") public String logo;
  public Staff superuser;
  public String weixin_image;
  public boolean weixin_success;
  public String weixin;

  public Shop() {
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
    dest.writeString(this.logo);
    dest.writeParcelable(this.superuser, flags);
    dest.writeString(this.weixin_image);
    dest.writeByte(this.weixin_success ? (byte) 1 : (byte) 0);
    dest.writeString(this.weixin);
  }

  protected Shop(Parcel in) {
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
    this.logo = in.readString();
    this.superuser = in.readParcelable(Staff.class.getClassLoader());
    this.weixin_image = in.readString();
    this.weixin_success = in.readByte() != 0;
    this.weixin = in.readString();
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