package cn.qingcheng.gym.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Personage;

public class SuperUser implements Parcelable {
  public String id;
  public Personage user;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeParcelable(this.user, flags);
  }

  public SuperUser() {
  }

  protected SuperUser(Parcel in) {
    this.id = in.readString();
    this.user = in.readParcelable(Personage.class.getClassLoader());
  }

  public static final Parcelable.Creator<cn.qingcheng.gym.bean.SuperUser> CREATOR =
      new Parcelable.Creator<cn.qingcheng.gym.bean.SuperUser>() {
        @Override public cn.qingcheng.gym.bean.SuperUser createFromParcel(Parcel source) {
          return new cn.qingcheng.gym.bean.SuperUser(source);
        }

        @Override public cn.qingcheng.gym.bean.SuperUser[] newArray(int size) {
          return new cn.qingcheng.gym.bean.SuperUser[size];
        }
      };
}
