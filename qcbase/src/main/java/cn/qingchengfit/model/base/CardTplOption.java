package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/31 2016.
 */
public class CardTplOption implements Parcelable {

    @SerializedName("id") public String id;
    @SerializedName("created_at") public String created_at;
    @SerializedName("description") public String description;
    @SerializedName("price") public float price;  //充值金额
    @SerializedName("days") public int days;
    @SerializedName("charge") public String charge;  //实收金额
    @SerializedName("can_charge") public boolean can_charge;
    @SerializedName("limit_days") public boolean limit_days;
    @SerializedName("can_create") public boolean can_create;
    @SerializedName("card_tpl") public Card_tpl card_tpl;
    public boolean for_staff;

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public void setCan_charge(boolean can_charge) {
        this.can_charge = can_charge;
    }

    public void setLimit_days(boolean limit_days) {
        this.limit_days = limit_days;
    }

    public void setCan_create(boolean can_create) {
        this.can_create = can_create;
    }

    public void setCard_tpl(Card_tpl card_tpl) {
        this.card_tpl = card_tpl;
    }

    public void setFor_staff(boolean for_staff) {
        this.for_staff = for_staff;
    }

    public String getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getDays() {
        return days;
    }

    public String getCharge() {
        return charge;
    }

    public boolean isCan_charge() {
        return can_charge;
    }

    public boolean isLimit_days() {
        return limit_days;
    }

    public boolean isCan_create() {
        return can_create;
    }

    public Card_tpl getCard_tpl() {
        return card_tpl;
    }

    public boolean isFor_staff() {
        return for_staff;
    }

    public CardTplOption() {
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.description);
        dest.writeFloat(this.price);
        dest.writeInt(this.days);
        dest.writeString(this.charge);
        dest.writeByte(this.can_charge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.limit_days ? (byte) 1 : (byte) 0);
        dest.writeByte(this.can_create ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.card_tpl, flags);
        dest.writeByte(this.for_staff ? (byte) 1 : (byte) 0);
    }

    protected CardTplOption(Parcel in) {
        this.id = in.readString();
        this.created_at = in.readString();
        this.description = in.readString();
        this.price = in.readFloat();
        this.days = in.readInt();
        this.charge = in.readString();
        this.can_charge = in.readByte() != 0;
        this.limit_days = in.readByte() != 0;
        this.can_create = in.readByte() != 0;
        this.card_tpl = in.readParcelable(Card_tpl.class.getClassLoader());
        this.for_staff = in.readByte() != 0;
    }

    public static final Creator<CardTplOption> CREATOR = new Creator<CardTplOption>() {
        @Override public CardTplOption createFromParcel(Parcel source) {
            return new CardTplOption(source);
        }

        @Override public CardTplOption[] newArray(int size) {
            return new CardTplOption[size];
        }
    };
}
