package com.qingchengfit.fitcoach.fragment.statement.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/6 2016.
 */
public class SaleCardForm implements Parcelable {
    public int card_type;
    public int trade_count;
    public float charge;
    public float real_income;

    public SaleCardForm(int card_type, int trade_count, float charge, float real_income) {
        this.card_type = card_type;
        this.trade_count = trade_count;
        this.charge = charge;
        this.real_income = real_income;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.card_type);
        dest.writeInt(this.trade_count);
        dest.writeFloat(this.charge);
        dest.writeFloat(this.real_income);
    }

    public SaleCardForm() {
    }

    protected SaleCardForm(Parcel in) {
        this.card_type = in.readInt();
        this.trade_count = in.readInt();
        this.charge = in.readFloat();
        this.real_income = in.readFloat();
    }

    public static final Parcelable.Creator<SaleCardForm> CREATOR = new Parcelable.Creator<SaleCardForm>() {
        @Override public SaleCardForm createFromParcel(Parcel source) {
            return new SaleCardForm(source);
        }

        @Override public SaleCardForm[] newArray(int size) {
            return new SaleCardForm[size];
        }
    };
}
