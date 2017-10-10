package cn.qingchengfit.saasbase.course.batch.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;

public class CardTplBatchShip extends CardTpl implements Parcelable {
    public static final Creator<CardTplBatchShip> CREATOR = new Creator<CardTplBatchShip>() {
        @Override public CardTplBatchShip createFromParcel(Parcel source) {
            return new CardTplBatchShip(source);
        }

        @Override public CardTplBatchShip[] newArray(int size) {
            return new CardTplBatchShip[size];
        }
    };
    public int status; //1，已经设置过结算 没约课 2、已设置结算 已约课

    public CardTplBatchShip() {
    }

    protected CardTplBatchShip(Parcel in) {
        super(in);
        this.status = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.status);
    }

    @Override public int hashCode() {
        return id.hashCode();
    }

    @Override public boolean equals(Object o) {
        if (o instanceof CardTpl) {
            return ((CardTpl) o).getId().equals(id);
        } else {
            return false;
        }
    }
}