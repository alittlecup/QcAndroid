package cn.qingchengfit.saasbase.turnovers;

import android.os.Parcel;
import android.os.Parcelable;

public class TurFilterData implements Parcelable ,ITurnoverFilterItemData{

  /**
   * color : 27c38c
   * short_text : 微
   * channel : WEIXIN
   * desc : 微信支付
   */

  private String color;
  private String short_text;
  private String channel;
  private String desc;

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getShort_text() {
    return short_text;
  }

  public void setShort_text(String short_text) {
    this.short_text = short_text;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.color);
    dest.writeString(this.short_text);
    dest.writeString(this.channel);
    dest.writeString(this.desc);
  }

  public TurFilterData() {
  }

  protected TurFilterData(Parcel in) {
    this.color = in.readString();
    this.short_text = in.readString();
    this.channel = in.readString();
    this.desc = in.readString();
  }

  public static final Parcelable.Creator<TurFilterData> CREATOR =
      new Parcelable.Creator<TurFilterData>() {
        @Override public TurFilterData createFromParcel(Parcel source) {
          return new TurFilterData(source);
        }

        @Override public TurFilterData[] newArray(int size) {
          return new TurFilterData[size];
        }
      };

  @Override public String getText() {
    return desc;
  }

  @Override public String getSignature() {
    return channel;
  }
}
