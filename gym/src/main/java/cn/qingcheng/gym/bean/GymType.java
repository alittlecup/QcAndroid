package cn.qingcheng.gym.bean;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bob Du on 2018/10/25 16:30
 */
@Entity(tableName = "gymtype",primaryKeys = "gym_type")
public class GymType implements Parcelable {
    public String gym_type_value;
    public int gym_type;

    public static final Creator<GymType> CREATOR = new Creator<GymType>() {
        @Override
        public GymType createFromParcel(Parcel in) {
            return new GymType(in);
        }

        @Override
        public GymType[] newArray(int size) {
            return new GymType[size];
        }
    };

    protected GymType(Parcel in) {
        this.gym_type_value = in.readString();
        this.gym_type = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gym_type_value);
        dest.writeInt(this.gym_type);
    }
}
