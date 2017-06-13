package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //工作人员 基本信息（staff） 职位信息（StaffPosition）
 * //Created by yangming on 16/11/18.
 */

public class StaffShip implements Parcelable {

    public static final Creator<StaffShip> CREATOR = new Creator<StaffShip>() {
        @Override public StaffShip createFromParcel(Parcel source) {
            return new StaffShip(source);
        }

        @Override public StaffShip[] newArray(int size) {
            return new StaffShip[size];
        }
    };
    @SerializedName("position") public StaffPosition position;
    @SerializedName("user") public Staff user;
    @SerializedName("id") public String id;
    public boolean is_superuser;

    public StaffShip() {
    }

    protected StaffShip(Parcel in) {
        this.position = in.readParcelable(StaffPosition.class.getClassLoader());
        this.user = in.readParcelable(Staff.class.getClassLoader());
        this.id = in.readString();
        this.is_superuser = in.readByte() != 0;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.position, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.id);
        dest.writeByte(this.is_superuser ? (byte) 1 : (byte) 0);
    }
}
