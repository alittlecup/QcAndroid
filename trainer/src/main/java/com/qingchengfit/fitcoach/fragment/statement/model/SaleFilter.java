package com.qingchengfit.fitcoach.fragment.statement.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.bean.StudentBean;

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
 * Created by Paper on 16/7/4 2016.
 */
public class SaleFilter implements Parcelable {
    public static final Creator<SaleFilter> CREATOR = new Creator<SaleFilter>() {
        @Override public SaleFilter createFromParcel(Parcel source) {
            return new SaleFilter(source);
        }

        @Override public SaleFilter[] newArray(int size) {
            return new SaleFilter[size];
        }
    };
    public String startDay;
    public String endDay;
    public int tradeType;
    public QcResponseSaleDetail.Card card;
    public Staff saler;
    public int payMethod;
    public StudentBean student;
    public int card_category;

    public SaleFilter() {
    }

    protected SaleFilter(Parcel in) {
        this.startDay = in.readString();
        this.endDay = in.readString();
        this.tradeType = in.readInt();
        this.card = in.readParcelable(QcResponseSaleDetail.Card.class.getClassLoader());
        this.saler = in.readParcelable(Staff.class.getClassLoader());
        this.payMethod = in.readInt();
        this.student = in.readParcelable(StudentBean.class.getClassLoader());
        this.card_category = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDay);
        dest.writeString(this.endDay);
        dest.writeInt(this.tradeType);
        dest.writeParcelable(this.card, flags);
        dest.writeParcelable(this.saler, flags);
        dest.writeInt(this.payMethod);
        dest.writeParcelable(this.student, flags);
        dest.writeInt(this.card_category);
    }
}
