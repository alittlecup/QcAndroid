package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fb on 2017/2/26.
 */

public class BalanceNotify implements Parcelable {

    public static final Creator<BalanceNotify> CREATOR = new Creator<BalanceNotify>() {
        @Override public BalanceNotify createFromParcel(Parcel in) {
            return new BalanceNotify(in);
        }

        @Override public BalanceNotify[] newArray(int size) {
            return new BalanceNotify[size];
        }
    };
    private int id;
    private boolean value;
    private boolean editable;
    private boolean readable;

    protected BalanceNotify(Parcel in) {
        this.id = in.readInt();
        this.value = in.readByte() != 0;
        this.editable = in.readByte() != 0;
        this.readable = in.readByte() != 0;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeByte((byte) (value ? 1 : 0));
        parcel.writeByte((byte) (editable ? 1 : 0));
        parcel.writeByte((byte) (readable ? 1 : 0));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

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
}
