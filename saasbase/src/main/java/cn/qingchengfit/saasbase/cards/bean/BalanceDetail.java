package cn.qingchengfit.saasbase.cards.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BalanceDetail implements Parcelable {

    public static final Creator<BalanceDetail> CREATOR = new Creator<BalanceDetail>() {
        @Override public BalanceDetail createFromParcel(Parcel parcel) {
            return new BalanceDetail(parcel);
        }

        @Override public BalanceDetail[] newArray(int i) {
            return new BalanceDetail[i];
        }
    };
    public int id;
    public int value;
    public String key;

    public BalanceDetail() {
    }

    public BalanceDetail(int id, int value, String key) {
        this.id = id;
        this.value = value;
        this.key = key;
    }

    protected BalanceDetail(Parcel in) {
        this.id = in.readInt();
        this.value = in.readInt();
        this.key = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.value);
        parcel.writeString(this.key);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
