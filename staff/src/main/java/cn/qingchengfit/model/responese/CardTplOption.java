package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
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
    public static final Creator<CardTplOption> CREATOR = new Creator<CardTplOption>() {
        @Override public CardTplOption createFromParcel(Parcel source) {
            return new CardTplOption(source);
        }

        @Override public CardTplOption[] newArray(int size) {
            return new CardTplOption[size];
        }
    };
    @SerializedName("id") public String id;
    @SerializedName("created_at") public String created_at;
    @SerializedName("description") public String description;
    @SerializedName("price") public String price;
    @SerializedName("days") public int days;
    @SerializedName("charge") public String charge;
    @SerializedName("can_charge") public boolean can_charge;
    @SerializedName("limit_days") public boolean limit_days;
    @SerializedName("can_create") public boolean can_create;
    @SerializedName("card_tpl") public CardTpl card_tpl;
    public boolean for_staff;

    public CardTplOption() {
    }

    protected CardTplOption(Parcel in) {
        this.id = in.readString();
        this.created_at = in.readString();
        this.description = in.readString();
        this.price = in.readString();
        this.days = in.readInt();
        this.charge = in.readString();
        this.can_charge = in.readByte() != 0;
        this.limit_days = in.readByte() != 0;
        this.can_create = in.readByte() != 0;
        this.card_tpl = in.readParcelable(CardTpl.class.getClassLoader());
        this.for_staff = in.readByte() != 0;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.description);
        dest.writeString(this.price);
        dest.writeInt(this.days);
        dest.writeString(this.charge);
        dest.writeByte(this.can_charge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.limit_days ? (byte) 1 : (byte) 0);
        dest.writeByte(this.can_create ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.card_tpl, flags);
        dest.writeByte(this.for_staff ? (byte) 1 : (byte) 0);
    }
}
