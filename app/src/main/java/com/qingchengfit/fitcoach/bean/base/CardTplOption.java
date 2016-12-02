package com.qingchengfit.fitcoach.bean.base;

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
    @SerializedName("id")
    public String id;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("description")
    public String description;
    @SerializedName("price")
    public String price;
    @SerializedName("days")
    public int days;
    @SerializedName("charge")
    public int charge;
    @SerializedName("can_charge")
    public boolean can_charge;
    @SerializedName("limit_days")
    public boolean limit_days;
    @SerializedName("can_create")
    public boolean can_create;
    @SerializedName("card_tpl")
    public Card_Tpl card_tpl;


    public static class Card_Tpl implements Parcelable {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public int type;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeInt(this.type);
        }

        public Card_Tpl() {
        }

        protected Card_Tpl(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.type = in.readInt();
        }

        public static final Creator<Card_Tpl> CREATOR = new Creator<Card_Tpl>() {
            @Override
            public Card_Tpl createFromParcel(Parcel source) {
                return new Card_Tpl(source);
            }

            @Override
            public Card_Tpl[] newArray(int size) {
                return new Card_Tpl[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.description);
        dest.writeString(this.price);
        dest.writeInt(this.days);
        dest.writeInt(this.charge);
        dest.writeByte(can_charge ? (byte) 1 : (byte) 0);
        dest.writeByte(limit_days ? (byte) 1 : (byte) 0);
        dest.writeByte(can_create ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.card_tpl, flags);
    }

    public CardTplOption() {
    }

    protected CardTplOption(Parcel in) {
        this.id = in.readString();
        this.created_at = in.readString();
        this.description = in.readString();
        this.price = in.readString();
        this.days = in.readInt();
        this.charge = in.readInt();
        this.can_charge = in.readByte() != 0;
        this.limit_days = in.readByte() != 0;
        this.can_create = in.readByte() != 0;
        this.card_tpl = in.readParcelable(Card_Tpl.class.getClassLoader());
    }

    public static final Creator<CardTplOption> CREATOR = new Creator<CardTplOption>() {
        @Override
        public CardTplOption createFromParcel(Parcel source) {
            return new CardTplOption(source);
        }

        @Override
        public CardTplOption[] newArray(int size) {
            return new CardTplOption[size];
        }
    };
}
