package cn.qingchengfit.shop.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangbaole on 2017/12/19.
 */

public class Category implements Parcelable {
  public String getId() {
    return id;
  }

  String id;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
  }

  public Category() {
  }

  protected Category(Parcel in) {
    this.id = in.readString();
  }

  public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
    @Override public Category createFromParcel(Parcel source) {
      return new Category(source);
    }

    @Override public Category[] newArray(int size) {
      return new Category[size];
    }
  };
}
