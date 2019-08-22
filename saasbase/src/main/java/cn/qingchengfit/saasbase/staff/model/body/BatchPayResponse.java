package cn.qingchengfit.saasbase.staff.model.body;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class BatchPayResponse implements Parcelable {
  public boolean alisp;
  @SerializedName("corp_card") public boolean corpCard;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.alisp ? (byte) 1 : (byte) 0);
    dest.writeByte(this.corpCard ? (byte) 1 : (byte) 0);
  }

  public BatchPayResponse() {
  }

  protected BatchPayResponse(Parcel in) {
    this.alisp = in.readByte() != 0;
    this.corpCard = in.readByte() != 0;
  }

  public static final Parcelable.Creator<BatchPayResponse> CREATOR =
      new Parcelable.Creator<BatchPayResponse>() {
        @Override public BatchPayResponse createFromParcel(Parcel source) {
          return new BatchPayResponse(source);
        }

        @Override public BatchPayResponse[] newArray(int size) {
          return new BatchPayResponse[size];
        }
      };
}
