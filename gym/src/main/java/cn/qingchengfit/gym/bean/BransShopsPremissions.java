package cn.qingchengfit.gym.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class BransShopsPremissions implements Parcelable {
  public List<BransShopsPremission> shops;

  protected BransShopsPremissions(Parcel in) {
    shops = in.createTypedArrayList(BransShopsPremission.CREATOR);
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(shops);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static final Creator<BransShopsPremissions> CREATOR =
      new Creator<BransShopsPremissions>() {
        @Override public BransShopsPremissions createFromParcel(Parcel in) {
          return new BransShopsPremissions(in);
        }

        @Override public BransShopsPremissions[] newArray(int size) {
          return new BransShopsPremissions[size];
        }
      };
}
