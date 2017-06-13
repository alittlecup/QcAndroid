package cn.qingchengfit.model.responese;

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
 * 会员卡充值规格
 * <p/>
 * Created by Paper on 16/3/17 2016.
 */
public class CardStandard implements Parcelable {
    public static final Creator<CardStandard> CREATOR = new Creator<CardStandard>() {
        @Override public CardStandard createFromParcel(Parcel source) {
            return new CardStandard(source);
        }

        @Override public CardStandard[] newArray(int size) {
            return new CardStandard[size];
        }
    };
    public String id;
    public String content;
    public String real_income;
    public String support_type;
    public String valid_date;
    public boolean isSupportCreate;
    public boolean isSupportCharge;
    public String charge;
    public int limitDay;
    public float income;
    public int cardtype;
    public boolean for_staff = false;
    private boolean isAdd = false;

    public CardStandard(String content, String real_income, String support_type, String valid_date) {
        this.content = content;
        this.real_income = real_income;
        this.support_type = support_type;
        this.valid_date = valid_date;
        this.isAdd = false;
    }

    public CardStandard(boolean isAdd, String valid_date, String support_type, String real_income, String content) {
        this.isAdd = isAdd;
        this.valid_date = valid_date;
        this.support_type = support_type;
        this.real_income = real_income;
        this.content = content;
    }

    protected CardStandard(Parcel in) {
        this.id = in.readString();
        this.content = in.readString();
        this.real_income = in.readString();
        this.support_type = in.readString();
        this.valid_date = in.readString();
        this.isSupportCreate = in.readByte() != 0;
        this.isSupportCharge = in.readByte() != 0;
        this.charge = in.readString();
        this.limitDay = in.readInt();
        this.income = in.readFloat();
        this.cardtype = in.readInt();
        this.isAdd = in.readByte() != 0;
        this.for_staff = in.readByte() != 0;
    }

    public int getCardtype() {
        return cardtype;
    }

    public void setCardtype(int cardtype) {
        this.cardtype = cardtype;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }

    public boolean isSupportCreate() {
        return isSupportCreate;
    }

    public void setSupportCreate(boolean supportCreate) {
        isSupportCreate = supportCreate;
    }

    public boolean isSupportCharge() {
        return isSupportCharge;
    }

    public void setSupportCharge(boolean supportCharge) {
        isSupportCharge = supportCharge;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReal_income() {
        return real_income;
    }

    public void setReal_income(String real_income) {
        this.real_income = real_income;
    }

    public String getSupport_type() {
        return support_type;
    }

    public void setSupport_type(String support_type) {
        this.support_type = support_type;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.real_income);
        dest.writeString(this.support_type);
        dest.writeString(this.valid_date);
        dest.writeByte(this.isSupportCreate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSupportCharge ? (byte) 1 : (byte) 0);
        dest.writeString(this.charge);
        dest.writeInt(this.limitDay);
        dest.writeFloat(this.income);
        dest.writeInt(this.cardtype);
        dest.writeByte(this.isAdd ? (byte) 1 : (byte) 0);
        dest.writeByte(this.for_staff ? (byte) 1 : (byte) 0);
    }
}
