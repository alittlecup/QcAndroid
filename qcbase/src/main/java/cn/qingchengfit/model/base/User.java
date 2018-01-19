package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

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
 * Created by Paper on 16/11/15.
 */

public class User extends Personage implements Parcelable {

  public static final Creator<User> CREATOR = new Creator<User>() {
        @Override public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("city") public String city;
    @SerializedName("description") public String desc;
    @SerializedName("address") public String address;
    @SerializedName("joined_at") public String joined_at;
    @SerializedName("hidden_phone") public String hidden_phone;
    @SerializedName("date_of_birth") public String date_of_birth;


    public User() {
    }

    protected User(Parcel in) {
      super(in);
        this.city = in.readString();
        this.desc = in.readString();
        this.address = in.readString();
        this.joined_at = in.readString();
        this.hidden_phone = in.readString();
        this.date_of_birth = in.readString();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(String joined_at) {
        this.joined_at = joined_at;
    }

    public String getHidden_phone() {
        return hidden_phone;
    }

    public void setHidden_phone(String hidden_phone) {
        this.hidden_phone = hidden_phone;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      super.writeToParcel(dest, flags);
        dest.writeString(this.city);
        dest.writeString(this.desc);
        dest.writeString(this.address);
        dest.writeString(this.joined_at);
        dest.writeString(this.hidden_phone);
        dest.writeString(this.date_of_birth);
    }

}
