package cn.qingchengfit.saasbase.turnovers;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.DateUtils;

public class TurOrderListData implements Parcelable, ITurnoverOrderItemData {
  private Shop shop;
  private int trade_type;
  private Staff created_by;
  private String remarks;
  private boolean is_refund;
  private String created_at;
  private String id;
  private float amount;
  private String channel;
  private Staff seller;

  public Shop getShop() {
    return shop;
  }

  public void setShop(Shop shop) {
    this.shop = shop;
  }

  public int getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(int trade_type) {
    this.trade_type = trade_type;
  }

  public Staff getCreated_by() {
    return created_by;
  }

  public void setCreated_by(Staff created_by) {
    this.created_by = created_by;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public boolean isIs_refund() {
    return is_refund;
  }

  public void setIs_refund(boolean is_refund) {
    this.is_refund = is_refund;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public Staff getSeller() {
    return seller;
  }

  public void setSeller(Staff seller) {
    this.seller = seller;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.shop, flags);
    dest.writeInt(this.trade_type);
    dest.writeParcelable(this.created_by, flags);
    dest.writeString(this.remarks);
    dest.writeByte(this.is_refund ? (byte) 1 : (byte) 0);
    dest.writeString(this.created_at);
    dest.writeString(this.id);
    dest.writeFloat(this.amount);
    dest.writeString(this.channel);
    dest.writeParcelable(this.seller, flags);
  }

  public TurOrderListData() {
  }

  protected TurOrderListData(Parcel in) {
    this.shop = in.readParcelable(Shop.class.getClassLoader());
    this.trade_type = in.readInt();
    this.created_by = in.readParcelable(Staff.class.getClassLoader());
    this.remarks = in.readString();
    this.is_refund = in.readByte() != 0;
    this.created_at = in.readString();
    this.id = in.readString();
    this.amount = in.readFloat();
    this.channel = in.readString();
    this.seller = in.readParcelable(Staff.class.getClassLoader());
  }

  public static final Parcelable.Creator<TurOrderListData> CREATOR =
      new Parcelable.Creator<TurOrderListData>() {
        @Override public TurOrderListData createFromParcel(Parcel source) {
          return new TurOrderListData(source);
        }

        @Override public TurOrderListData[] newArray(int size) {
          return new TurOrderListData[size];
        }
      };

  @Override public String getPayType() {
    return channel;
  }

  @Override public String getFeatureName() {
    return String.valueOf(trade_type);
  }

  @Override public String getMoney() {
    return String.valueOf(amount);
  }

  @Override public String getSellerName() {
    return seller != null ? seller.getUsername() : "";
  }

  @Override public String getCheckoutName() {
    return created_by != null ? created_by.getUsername() : "";
  }

  @Override public String getDate() {
    return DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(created_at));
  }

  @Override public String getID() {
    return id;
  }
}
