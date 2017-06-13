package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/5/6 2016.
 */
public class QcResponseStaffs extends QcResponse {

    @SerializedName("data") public Data data;

    public static class StaffShip implements Parcelable {
        public static final Creator<StaffShip> CREATOR = new Creator<StaffShip>() {
            @Override public StaffShip createFromParcel(Parcel source) {
                return new StaffShip(source);
            }

            @Override public StaffShip[] newArray(int size) {
                return new StaffShip[size];
            }
        };
        @SerializedName("position") public Position position;
        @SerializedName("user") public User_Student user;
        @SerializedName("id") public String id;

        public StaffShip() {
        }

        protected StaffShip(Parcel in) {
            this.position = in.readParcelable(Position.class.getClassLoader());
            this.user = in.readParcelable(User_Student.class.getClassLoader());
            this.id = in.readString();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.position, flags);
            dest.writeParcelable(this.user, flags);
            dest.writeString(this.id);
        }
    }

    public static class Position implements Parcelable {
        public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
            @Override public Position createFromParcel(Parcel source) {
                return new Position(source);
            }

            @Override public Position[] newArray(int size) {
                return new Position[size];
            }
        };
        @SerializedName("name") public String name;
        @SerializedName("id") public String id;

        public Position() {
        }

        protected Position(Parcel in) {
            this.name = in.readString();
            this.id = in.readString();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.id);
        }
    }

    public class Data {
        @SerializedName("total_count") public int total_count;
        @SerializedName("current_page") public int current_page;
        @SerializedName("pages") public int pages;

        @SerializedName("ships") public List<StaffShip> ships;
    }
}
