package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import cn.qingchengfit.bean.Permission;
import java.util.List;

public class QcResponsePermission extends QcResponse {
    @SerializedName("data") public Data data;

    public static class Data implements Parcelable {

        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override public Data[] newArray(int size) {
                return new Data[size];
            }
        };
        @SerializedName("permissions") public List<Permission> permissions;

        public Data() {
        }

        protected Data(Parcel in) {
            this.permissions = in.createTypedArrayList(Permission.CREATOR);
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.permissions);
        }
    }
}