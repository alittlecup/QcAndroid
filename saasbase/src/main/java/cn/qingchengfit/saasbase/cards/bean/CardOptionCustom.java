package cn.qingchengfit.saasbase.cards.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/12/15.
 */

public class CardOptionCustom implements Parcelable {
  private int days;
  private long price;
  private boolean isValidate;
  private long validate;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.days);
    dest.writeLong(this.price);
    dest.writeByte(this.isValidate ? (byte) 1 : (byte) 0);
    dest.writeLong(this.validate);
  }

  public void setDays(int days) {
    this.days = days;
  }

  public void setIsValidate(boolean validate) {
    isValidate = validate;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  public void setValidate(long validate) {
    this.validate = validate;
  }

  public int getDays() {
    return days;
  }

  public long getPrice() {
    return price;
  }

  public boolean isValidate() {
    return isValidate;
  }

  public long getValidate() {
    return validate;
  }

  public CardOptionCustom() {
  }

  protected CardOptionCustom(Parcel in) {
    this.days = in.readInt();
    this.price = in.readLong();
    this.isValidate = in.readByte() != 0;
    this.validate = in.readLong();
  }

  public static final Creator<CardOptionCustom> CREATOR = new Creator<CardOptionCustom>() {
    @Override public CardOptionCustom createFromParcel(Parcel source) {
      return new CardOptionCustom(source);
    }

    @Override public CardOptionCustom[] newArray(int size) {
      return new CardOptionCustom[size];
    }
  };
}
