package cn.qingchengfit.writeoff.vo.impl;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleSuccessResponse implements Parcelable {
  public boolean isSuccessful() {
    return successful;
  }

  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  boolean successful;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.successful ? (byte) 1 : (byte) 0);
  }

  public SimpleSuccessResponse() {
  }

  protected SimpleSuccessResponse(Parcel in) {
    this.successful = in.readByte() != 0;
  }

  public static final Parcelable.Creator<SimpleSuccessResponse> CREATOR =
      new Parcelable.Creator<SimpleSuccessResponse>() {
        @Override public SimpleSuccessResponse createFromParcel(Parcel source) {
          return new SimpleSuccessResponse(source);
        }

        @Override public SimpleSuccessResponse[] newArray(int size) {
          return new SimpleSuccessResponse[size];
        }
      };
}
