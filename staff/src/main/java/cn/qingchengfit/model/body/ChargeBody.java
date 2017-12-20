package cn.qingchengfit.model.body;

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
 * Created by Paper on 16/4/27 2016.
 */
public class ChargeBody implements Parcelable {
    String price;
    int charge_type; //支付类型
    String shop_id;
    String card_no;
    String account; //储值卡充值
    String seller_id;
    String times; //次卡充值
    String start;
    String end;
    boolean check_valid;//是否设置有效期
    String valid_from;
    String valid_to;
    String option_id;
    String remarks;
    String type;
    String card_id;
    String id;

    //    String app_id;
    String model;
    int origin;



    public ChargeBody() {
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
        dest.writeByte(this.check_valid ? (byte) 1 : (byte) 0);
        dest.writeString(this.valid_from);
        dest.writeString(this.valid_to);
        dest.writeString(this.option_id);
        dest.writeString(this.remarks);
        dest.writeString(this.type);
        dest.writeString(this.card_id);
        dest.writeString(this.id);
        dest.writeString(this.model);
        dest.writeInt(this.origin);
    }

    protected ChargeBody(Parcel in) {
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
        this.type = in.readString();
        this.card_id = in.readString();
        this.id = in.readString();
        this.model = in.readString();
        this.origin = in.readInt();
    }

    public static final Creator<ChargeBody> CREATOR = new Creator<ChargeBody>() {
        @Override public ChargeBody createFromParcel(Parcel source) {
            return new ChargeBody(source);
        }

        @Override public ChargeBody[] newArray(int size) {
            return new ChargeBody[size];
        }
    };
}
