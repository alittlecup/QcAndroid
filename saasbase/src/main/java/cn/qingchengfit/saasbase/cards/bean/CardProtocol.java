package cn.qingchengfit.saasbase.cards.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/11/21.
 */

public class CardProtocol implements Parcelable{

  public String id;
  public String content_link;
  public Create_By created_by;
  public String created_at;
  public boolean is_read;
  public boolean is_sign;
  public String read_time;
  public int service_term_version;

  public CardProtocol() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.content_link);
    dest.writeParcelable(this.created_by, flags);
    dest.writeString(this.created_at);
    dest.writeByte(this.is_read ? (byte) 1 : (byte) 0);
    dest.writeByte(this.is_sign ? (byte) 1 : (byte) 0);
    dest.writeString(this.read_time);
    dest.writeInt(this.service_term_version);
  }

  protected CardProtocol(Parcel in) {
    this.id = in.readString();
    this.content_link = in.readString();
    this.created_by = in.readParcelable(Create_By.class.getClassLoader());
    this.created_at = in.readString();
    this.is_read = in.readByte() != 0;
    this.is_sign = in.readByte() != 0;
    this.read_time = in.readString();
    this.service_term_version = in.readInt();
  }

  public static final Creator<CardProtocol> CREATOR = new Creator<CardProtocol>() {
    @Override public CardProtocol createFromParcel(Parcel source) {
      return new CardProtocol(source);
    }

    @Override public CardProtocol[] newArray(int size) {
      return new CardProtocol[size];
    }
  };
}
