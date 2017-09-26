package cn.qingchengfit.saasbase.cards.cardtypes.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/4/28 2016.
 */
public class CreateCardBody implements Parcelable {
  public static final Creator<CreateCardBody> CREATOR = new Creator<CreateCardBody>() {
    @Override public CreateCardBody createFromParcel(Parcel source) {
      return new CreateCardBody(source);
    }

    @Override public CreateCardBody[] newArray(int size) {
      return new CreateCardBody[size];
    }
  };
  public String price;
  public int charge_type; //支付类型
  public String shop_id;
  public String card_no;
  public String account; //储值卡充值
  public String seller_id;
  public String times; //次卡充值
  public String start;
  public String end;
  public boolean check_valid;//是否设置有效期
  public String valid_from;
  public String valid_to;
  public String option_id;
  public String remarks;
  public String card_tpl_id;
  public String user_ids;
  public String user_name;
  public String app_id;

  public CreateCardBody() {
  }

  protected CreateCardBody(Parcel in) {
    this.price = in.readString();
    this.charge_type = in.readInt();
    this.shop_id = in.readString();
    this.card_no = in.readString();
    this.account = in.readString();
    this.seller_id = in.readString();
    this.times = in.readString();
    this.start = in.readString();
    this.end = in.readString();
    this.check_valid = in.readByte() != 0;
    this.valid_from = in.readString();
    this.valid_to = in.readString();
    this.option_id = in.readString();
    this.remarks = in.readString();
    this.card_tpl_id = in.readString();
    this.user_ids = in.readString();
    this.user_name = in.readString();
  }

  @Override protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public CreateCardBody cloneSelf() {
    try {
      return (CreateCardBody) this.clone();
    } catch (CloneNotSupportedException e) {
      CreateCardBody ret = new CreateCardBody();

      return ret;
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.price);
    dest.writeInt(this.charge_type);
    dest.writeString(this.shop_id);
    dest.writeString(this.card_no);
    dest.writeString(this.account);
    dest.writeString(this.seller_id);
    dest.writeString(this.times);
    dest.writeString(this.start);
    dest.writeString(this.end);
    dest.writeByte(check_valid ? (byte) 1 : (byte) 0);
    dest.writeString(this.valid_from);
    dest.writeString(this.valid_to);
    dest.writeString(this.option_id);
    dest.writeString(this.remarks);
    dest.writeString(this.card_tpl_id);
    dest.writeString(this.user_ids);
    dest.writeString(this.user_name);
  }
}
