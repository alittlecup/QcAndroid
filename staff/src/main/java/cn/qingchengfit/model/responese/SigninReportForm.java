package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;

public class SigninReportForm implements Parcelable {
    public static final Creator<SigninReportForm> CREATOR = new Creator<SigninReportForm>() {
        @Override public SigninReportForm createFromParcel(Parcel source) {
            return new SigninReportForm(source);
        }

        @Override public SigninReportForm[] newArray(int size) {
            return new SigninReportForm[size];
        }
    };
    public int card_type;
    public int signin_count;
    public float real_income;

    public SigninReportForm(int trade_type, int trade_count, float real_income) {
        this.card_type = trade_type;
        this.signin_count = trade_count;
        this.real_income = real_income;
    }

    public SigninReportForm() {
    }

    protected SigninReportForm(Parcel in) {
        this.card_type = in.readInt();
        this.signin_count = in.readInt();
        this.real_income = in.readFloat();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.card_type);
        dest.writeInt(this.signin_count);
        dest.writeFloat(this.real_income);
    }
}
