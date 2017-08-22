package cn.qingchengfit.staffkit.train.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fb on 2017/3/22.
 */

public class SignUpDetailResponse {

    @SerializedName("user") public SignUpDetailBean signUpDetailBean;

    public static class SignUpDetailBean extends SignPersonalBean implements Parcelable {
        public static final Parcelable.Creator<SignUpDetailBean> CREATOR = new Parcelable.Creator<SignUpDetailBean>() {
            @Override public SignUpDetailBean createFromParcel(Parcel source) {
                return new SignUpDetailBean(source);
            }

            @Override public SignUpDetailBean[] newArray(int size) {
                return new SignUpDetailBean[size];
            }
        };
        public AttendanceBean attendance;

        public SignUpDetailBean() {
        }

        protected SignUpDetailBean(Parcel in) {
            super(in);
            this.attendance = in.readParcelable(AttendanceBean.class.getClassLoader());
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.attendance, flags);
        }
    }
}
