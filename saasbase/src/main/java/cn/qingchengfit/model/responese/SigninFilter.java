package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class SigninFilter implements Parcelable {

    public String startDay;
    public String endDay;
    public SigninReportDetail.CheckinsBean.CardBean card;//卡
    public SigninReportDetail.CheckinsBean.UserBean student;//学员
    public int card_category;//卡类别
    public int status = -1;//签到类型
    public String order_extra;//ALL，WEIXIN

    public SigninFilter() {
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDay);
        dest.writeString(this.endDay);
        dest.writeParcelable(this.card, flags);
        dest.writeParcelable(this.student, flags);
        dest.writeInt(this.card_category);
        dest.writeInt(this.status);
        dest.writeString(this.order_extra);
    }

    protected SigninFilter(Parcel in) {
        this.startDay = in.readString();
        this.endDay = in.readString();
        this.card =
            in.readParcelable(SigninReportDetail.CheckinsBean.CardBean.class.getClassLoader());
        this.student =
            in.readParcelable(SigninReportDetail.CheckinsBean.UserBean.class.getClassLoader());
        this.card_category = in.readInt();
        this.status = in.readInt();
        this.order_extra = in.readString();
    }

    public static final Creator<SigninFilter> CREATOR = new Creator<SigninFilter>() {
        @Override public SigninFilter createFromParcel(Parcel source) {
            return new SigninFilter(source);
        }

        @Override public SigninFilter[] newArray(int size) {
            return new SigninFilter[size];
        }
    };
}
