package cn.qingchengfit.saasbase.cards.network.body;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;

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
 * Created by Paper on 16/4/27 2016.
 */
public class CardBuyBody implements Parcelable, Cloneable {
  /**
   * 卡类型
   */
  Integer type;

  public int getType() {
    return type;
  }

  public String price;
  public Integer charge_type = 7; //支付类型
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
  public String app_id;
  public boolean is_auto_start;//是否自动开卡

  public String card_id;
  public String staff_id;
  public String version;
  public String signature;
  public String coupon_id;

  public int origin;
  public boolean customize_option;

  public int checkData() {
    if (!CmStringUtils.checkMoney(price)) return R.string.e_card_realpay_cannot_empty;

    if (CmStringUtils.isEmpty(card_tpl_id)) return R.string.e_cardtpl_empty;
    if (CmStringUtils.isEmpty(user_ids)) return R.string.e_member_empty;
    if (check_valid) {
      if (CmStringUtils.isEmpty(valid_from) || CmStringUtils.isEmpty(valid_to)) {
        return R.string.e_card_start_or_end_cannot_empty;
      }
      if (DateUtils.formatDateFromYYYYMMDD(valid_from).getTime() > DateUtils.formatDateFromYYYYMMDD(
          valid_to).getTime()) {
        return R.string.e_start_great_end;
      }
    }
    if (CmStringUtils.isEmpty(seller_id) && CmStringUtils.isEmpty(staff_id)) {
      return R.string.e_card_saler_cannot_empty;
    }
    switch (type) {
      case 1:
        if (!CmStringUtils.checkMoney(account)) return R.string.e_card_charge_money_cannot_empty;
        break;
      case 2:
        if (!CmStringUtils.checkMoney(times)) return R.string.e_card_charge_times_cannot_empty;
        break;
      case 3:
        if (CmStringUtils.isEmpty(start) || CmStringUtils.isEmpty(end)) {
          return R.string.e_card_charge_period_cannot_empty;
        }
        if (DateUtils.formatDateFromYYYYMMDD(start).getTime() > DateUtils.formatDateFromYYYYMMDD(
            end).getTime()) {
          return R.string.e_start_great_end;
        }
        break;
      default:
        return R.string.e_card_buy_cate;
    }
    return 0;
  }

  public int checkWithOutSeller() {
    if (!CmStringUtils.checkMoney(price)) return R.string.e_card_realpay_cannot_empty;
    if (CmStringUtils.isEmpty(card_tpl_id)) return R.string.e_cardtpl_empty;
    if (CmStringUtils.isEmpty(user_ids)) return R.string.e_member_empty;
    if (check_valid) {
      if (CmStringUtils.isEmpty(valid_from) || CmStringUtils.isEmpty(valid_to)) {
        return R.string.e_card_start_or_end_cannot_empty;
      }
      if (DateUtils.formatDateFromYYYYMMDD(valid_from).getTime() > DateUtils.formatDateFromYYYYMMDD(
          valid_to).getTime()) {
        return R.string.e_start_great_end;
      }
    }
    switch (type) {
      case 1:
        if (!CmStringUtils.checkMoney(account)) return R.string.e_card_charge_money_cannot_empty;
        break;
      case 2:
        if (!CmStringUtils.checkMoney(times)) return R.string.e_card_charge_times_cannot_empty;
        break;
      case 3:
        if (CmStringUtils.isEmpty(start) || CmStringUtils.isEmpty(end)) {
          return R.string.e_card_charge_period_cannot_empty;
        }
        if (DateUtils.formatDateFromYYYYMMDD(start).getTime() > DateUtils.formatDateFromYYYYMMDD(
            end).getTime()) {
          return R.string.e_start_great_end;
        }
        break;
      default:
        return R.string.e_card_buy_cate;
    }
    return 0;
  }

  @Override public Object clone() {
    CardBuyBody stu = null;
    try {
      stu = (CardBuyBody) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return stu;
  }

  public void setBuyAccount(String account, String start, String end, CardTplOption cto) {
    switch (type) {
      case 1:
        this.account = account;
        if (cto != null && cto.limit_days) {
          this.check_valid = true;
          this.valid_from = start;
          this.valid_to = end;
        }
        break;
      case 2:
        this.times = account;
        if (cto != null && cto.limit_days) {
          this.check_valid = true;
          this.valid_from = start;
          this.valid_to = end;
        }
        break;
      case 3:
        this.start = start;
        this.end = end;
        //if (cto != null){
        //  try {
        //    this.end = DateUtils.addDay(start,(int)Float.parseFloat(cto.charge));
        //  }catch (Exception e){
        //
        //  }
        //
        //}
        break;
    }
  }

  public CardBuyBody() {
  }

  private CardBuyBody(Builder builder) {
    setType(builder.type);
    setPrice(builder.price);
    setCharge_type(builder.charge_type);
    setCard_no(builder.card_no);
    setAccount(builder.account);
    setSeller_id(builder.seller_id);
    setTimes(builder.times);
    setStart(builder.start);
    setEnd(builder.end);
    setCheck_valid(builder.check_valid);
    setValid_from(builder.valid_from);
    setValid_to(builder.valid_to);
    setOption_id(builder.option_id);
    setRemarks(builder.remarks);
    card_tpl_id = builder.card_tpl_id;
    user_ids = builder.user_ids;
    app_id = builder.app_id;
  }

  public void setCard_tpl_id(String card_tpl_id) {
    this.card_tpl_id = card_tpl_id;
  }

  public void setUser_ids(String user_ids) {
    this.user_ids = user_ids;
  }

  public void setIs_auto_start(boolean is_auto_start) {
    this.is_auto_start = is_auto_start;
  }

  public String getCard_no() {
    return card_no;
  }

  public void setCard_no(String card_no) {
    this.card_no = card_no;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public int getCharge_type() {
    return charge_type;
  }

  public void setCharge_type(int charge_type) {
    this.charge_type = charge_type;
  }

  public void setCustomize_option(boolean customize_option) {
    this.customize_option = customize_option;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getSeller_id() {
    return seller_id;
  }

  public void setSeller_id(String seller_id) {
    this.seller_id = seller_id;
  }

  public String getTimes() {
    return times;
  }

  public void setTimes(String times) {
    this.times = times;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public boolean isCheck_valid() {
    return check_valid;
  }

  public void setCheck_valid(boolean check_valid) {
    this.check_valid = check_valid;
  }

  public String getValid_from() {
    return valid_from;
  }

  public void setValid_from(String valid_from) {
    this.valid_from = valid_from;
  }

  public String getValid_to() {
    return valid_to;
  }

  public void setValid_to(String valid_to) {
    this.valid_to = valid_to;
  }

  public String getOption_id() {
    return option_id;
  }

  public void setOption_id(String option_id) {
    this.option_id = option_id;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public void setCard_id(String card_id) {
    this.card_id = card_id;
  }

  public static final class Builder {
    private String price;
    private int charge_type;
    private String shop_id;
    private String card_no;
    private String account;
    private String seller_id;
    private String times;
    private String start;
    private String end;
    private boolean check_valid;
    private String valid_from;
    private String valid_to;
    private String option_id;
    private String remarks;
    private String card_tpl_id;
    private String user_ids;
    private String app_id;
    private String card_id;
    private String id;
    private String model;
    private int type;

    public Builder() {
    }

    public Builder price(String val) {
      price = val;
      return this;
    }

    public Builder charge_type(int val) {
      charge_type = val;
      return this;
    }

    public Builder shop_id(String val) {
      shop_id = val;
      return this;
    }

    public Builder card_no(String val) {
      card_no = val;
      return this;
    }

    public Builder account(String val) {
      account = val;
      return this;
    }

    public Builder seller_id(String val) {
      seller_id = val;
      return this;
    }

    public Builder times(String val) {
      times = val;
      return this;
    }

    public Builder start(String val) {
      start = val;
      return this;
    }

    public Builder end(String val) {
      end = val;
      return this;
    }

    public Builder check_valid(boolean val) {
      check_valid = val;
      return this;
    }

    public Builder valid_from(String val) {
      valid_from = val;
      return this;
    }

    public Builder valid_to(String val) {
      valid_to = val;
      return this;
    }

    public Builder option_id(String val) {
      option_id = val;
      return this;
    }

    public Builder remarks(String val) {
      remarks = val;
      return this;
    }

    public Builder card_tpl_id(String val) {
      card_tpl_id = val;
      return this;
    }

    public Builder user_ids(String val) {
      user_ids = val;
      return this;
    }

    public Builder app_id(String val) {
      app_id = val;
      return this;
    }

    public Builder card_id(String val) {
      card_id = val;
      return this;
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder model(String val) {
      model = val;
      return this;
    }

    public CardBuyBody build() {
      return new CardBuyBody(this);
    }

    public Builder type(int val) {
      type = val;
      return this;
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.type);
    dest.writeString(this.price);
    dest.writeValue(this.charge_type);
    dest.writeString(this.card_no);
    dest.writeString(this.account);
    dest.writeString(this.seller_id);
    dest.writeString(this.times);
    dest.writeString(this.start);
    dest.writeString(this.end);
    dest.writeByte(this.check_valid ? (byte) 1 : (byte) 0);
    dest.writeString(this.valid_from);
    dest.writeString(this.valid_to);
    dest.writeString(this.option_id);
    dest.writeString(this.remarks);
    dest.writeString(this.card_tpl_id);
    dest.writeString(this.user_ids);
    dest.writeString(this.app_id);
    dest.writeByte(this.is_auto_start ? (byte) 1 : (byte) 0);
    dest.writeString(this.card_id);
    dest.writeString(this.staff_id);
    dest.writeString(this.version);
    dest.writeString(this.signature);
    dest.writeString(this.coupon_id);
    dest.writeInt(this.origin);
    dest.writeByte(this.customize_option ? (byte) 1 : (byte) 0);
  }

  protected CardBuyBody(Parcel in) {
    this.type = (Integer) in.readValue(Integer.class.getClassLoader());
    this.price = in.readString();
    this.charge_type = (Integer) in.readValue(Integer.class.getClassLoader());
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
    this.app_id = in.readString();
    this.is_auto_start = in.readByte() != 0;
    this.card_id = in.readString();
    this.staff_id = in.readString();
    this.version = in.readString();
    this.signature = in.readString();
    this.coupon_id = in.readString();
    this.origin = in.readInt();
    this.customize_option = in.readByte() != 0;
  }

  public static final Creator<CardBuyBody> CREATOR = new Creator<CardBuyBody>() {
    @Override public CardBuyBody createFromParcel(Parcel source) {
      return new CardBuyBody(source);
    }

    @Override public CardBuyBody[] newArray(int size) {
      return new CardBuyBody[size];
    }
  };
}
