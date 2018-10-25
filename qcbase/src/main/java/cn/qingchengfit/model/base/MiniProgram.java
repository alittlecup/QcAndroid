package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;

public class MiniProgram implements Parcelable {
  public int status;
  public int authentication_status;
  public String nick_name;
  public String introduction;
  public String qrcode_url;
  public String logo_url;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.status);
    dest.writeInt(this.authentication_status);
    dest.writeString(this.nick_name);
    dest.writeString(this.introduction);
    dest.writeString(this.qrcode_url);
    dest.writeString(this.logo_url);
  }

  public MiniProgram() {
  }

  protected MiniProgram(Parcel in) {
    this.status = in.readInt();
    this.authentication_status = in.readInt();
    this.nick_name = in.readString();
    this.introduction = in.readString();
    this.qrcode_url = in.readString();
    this.logo_url = in.readString();
  }

  public static final Parcelable.Creator<MiniProgram> CREATOR =
      new Parcelable.Creator<MiniProgram>() {
        @Override public MiniProgram createFromParcel(Parcel source) {
          return new MiniProgram(source);
        }

        @Override public MiniProgram[] newArray(int size) {
          return new MiniProgram[size];
        }
      };
}
