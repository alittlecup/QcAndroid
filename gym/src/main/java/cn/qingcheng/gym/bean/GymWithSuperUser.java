package cn.qingcheng.gym.bean;

import android.os.Parcel;
import cn.qingchengfit.model.base.Gym;

public class GymWithSuperUser extends Gym {
  public SuperUser superuser;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.superuser, flags);
  }

  public GymWithSuperUser() {
  }

  protected GymWithSuperUser(Parcel in) {
    super(in);
    this.superuser = in.readParcelable(SuperUser.class.getClassLoader());
  }

  public static final Creator<GymWithSuperUser> CREATOR = new Creator<GymWithSuperUser>() {
    @Override public GymWithSuperUser createFromParcel(Parcel source) {
      return new GymWithSuperUser(source);
    }

    @Override public GymWithSuperUser[] newArray(int size) {
      return new GymWithSuperUser[size];
    }
  };
}


