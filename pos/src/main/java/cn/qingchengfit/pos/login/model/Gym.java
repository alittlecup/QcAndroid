package cn.qingchengfit.pos.login.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.di.model.SuperUser;

/**
 * Created by fb on 2017/10/16.
 */

public class Gym implements Parcelable{

  public String id;
  public String name;
  public String photo;
  public String brand_name;
  public SuperUser superuser;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.photo);
    dest.writeString(this.brand_name);
    dest.writeParcelable(this.superuser, flags);
  }

  public Gym() {
  }

  protected Gym(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.photo = in.readString();
    this.brand_name = in.readString();
    this.superuser = in.readParcelable(SuperUser.class.getClassLoader());
  }

  public static final Creator<Gym> CREATOR = new Creator<Gym>() {
    @Override public Gym createFromParcel(Parcel source) {
      return new Gym(source);
    }

    @Override public Gym[] newArray(int size) {
      return new Gym[size];
    }
  };
}
