package cn.qingchengfit.gym.bean;

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

  public static final Parcelable.Creator<SuperUser> CREATOR =
      new Parcelable.Creator<SuperUser>() {
        @Override public SuperUser createFromParcel(Parcel source) {
          return new SuperUser(source);
        }

        @Override public SuperUser[] newArray(int size) {
          return new SuperUser[size];
        }
      };
}
