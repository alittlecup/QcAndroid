package cn.qingchengfit.saasbase.cards.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NotifyIsOpen implements Parcelable {

    public static final Parcelable.Creator<BalanceNotify> CREATOR = new Parcelable.Creator<BalanceNotify>() {
        @Override public BalanceNotify createFromParcel(Parcel in) {
            return new BalanceNotify(in);
        }

        @Override public BalanceNotify[] newArray(int size) {
            return new BalanceNotify[size];
        }
    };
    public int id;
    public String value;
    public boolean editable;
    public boolean readable;
    public String key;

    protected NotifyIsOpen(Parcel in) {
        this.id = in.readInt();
        this.value = in.readString();
        this.editable = in.readByte() != 0;
        this.readable = in.readByte() != 0;
        this.key = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        //        parcel.writeByte((byte)(value ? 1 : 0));
        parcel.writeString(this.value);
        parcel.writeByte((byte) (editable ? 1 : 0));
        parcel.writeByte((byte) (readable ? 1 : 0));
        parcel.writeString(this.key);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //    public void setValue(boolean value) {
    //        this.value = value;
    //    }

    //    public boolean isValue() {
    //        return value;
    //    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
