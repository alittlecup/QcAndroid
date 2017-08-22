package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.QcStudentBean;
import java.util.List;

/**
 * Created by fb on 2017/3/22.
 */

public class SignPersonalBean extends QcStudentBean implements Parcelable {

    public static final Parcelable.Creator<SignPersonalBean> CREATOR = new Parcelable.Creator<SignPersonalBean>() {
        @Override public SignPersonalBean createFromParcel(Parcel source) {
            return new SignPersonalBean(source);
        }

        @Override public SignPersonalBean[] newArray(int size) {
            return new SignPersonalBean[size];
        }
    };
    public CompetitionOrder order;
    public String created_at;
    public List<GroupBean> teams;

    public SignPersonalBean() {
    }

    protected SignPersonalBean(Parcel in) {
        super(in);
        this.order = in.readParcelable(CompetitionOrder.class.getClassLoader());
        this.created_at = in.readString();
        this.teams = in.createTypedArrayList(GroupBean.CREATOR);
    }

    public String getOrderId() {
        return order == null ? "" : order.getId();
    }

    public String getOrderPrice() {
        return order == null ? "" : order.getPrice();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.order, flags);
        dest.writeString(this.created_at);
        dest.writeTypedList(this.teams);
    }
}
