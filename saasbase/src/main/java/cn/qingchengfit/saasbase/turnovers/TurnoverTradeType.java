package cn.qingchengfit.saasbase.turnovers;

import android.os.Parcel;
import android.os.Parcelable;

public class TurnoverTradeType implements Parcelable, ITurnoverFilterItemData {

  /**
   * color : 48D098
   * trade_type : 2
   * desc : 充值
   */

  private String color;
  private int trade_type;
  private String desc;

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(int trade_type) {
    this.trade_type = trade_type;
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
    dest.writeInt(this.trade_type);
    dest.writeString(this.desc);
  }

  public TurnoverTradeType() {
  }

  protected TurnoverTradeType(Parcel in) {
    this.color = in.readString();
    this.trade_type = in.readInt();
    this.desc = in.readString();
  }

  public static final Parcelable.Creator<TurnoverTradeType> CREATOR =
      new Parcelable.Creator<TurnoverTradeType>() {
        @Override public TurnoverTradeType createFromParcel(Parcel source) {
          return new TurnoverTradeType(source);
        }

        @Override public TurnoverTradeType[] newArray(int size) {
          return new TurnoverTradeType[size];
        }
      };

  @Override public String getText() {
    return desc;
  }

  @Override public String getSignature() {
    return String.valueOf(trade_type);
  }
}
