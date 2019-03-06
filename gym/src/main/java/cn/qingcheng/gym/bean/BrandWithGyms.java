package cn.qingcheng.gym.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Brand;
import java.util.List;

public class BrandWithGyms implements Parcelable {
  public Brand brand;
  public List<GymWithSuperUser> gyms;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.brand, flags);
    dest.writeTypedList(this.gyms);
  }

  public BrandWithGyms() {
  }

  protected BrandWithGyms(Parcel in) {
    this.brand = in.readParcelable(Brand.class.getClassLoader());
    this.gyms = in.createTypedArrayList(GymWithSuperUser.CREATOR);
  }

  public static final Parcelable.Creator<BrandWithGyms> CREATOR =
      new Parcelable.Creator<BrandWithGyms>() {
        @Override public BrandWithGyms createFromParcel(Parcel source) {
          return new BrandWithGyms(source);
        }

        @Override public BrandWithGyms[] newArray(int size) {
          return new BrandWithGyms[size];
        }
      };
}

