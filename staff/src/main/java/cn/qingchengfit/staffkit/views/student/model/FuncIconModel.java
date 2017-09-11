package cn.qingchengfit.staffkit.views.student.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by fb on 2017/9/6.
 */

public class FuncIconModel implements Parcelable {

  @DrawableRes
  public int resId;
  @StringRes
  public int strId;
  public boolean isPro;
  public boolean isDone;

  public FuncIconModel(int strId, int resId, boolean isPro, boolean isDone) {
    this.strId = strId;
    this.resId = resId;
    this.isPro = isPro;
    this.isDone = isDone;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.resId);
    dest.writeInt(this.strId);
    dest.writeByte(this.isPro ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isDone ? (byte) 1 : (byte) 0);
  }

  protected FuncIconModel(Parcel in) {
    this.resId = in.readInt();
    this.strId = in.readInt();
    this.isPro = in.readByte() != 0;
    this.isDone = in.readByte() != 0;
  }

  public static final Creator<FuncIconModel> CREATOR = new Creator<FuncIconModel>() {
    @Override public FuncIconModel createFromParcel(Parcel source) {
      return new FuncIconModel(source);
    }

    @Override public FuncIconModel[] newArray(int size) {
      return new FuncIconModel[size];
    }
  };
}
