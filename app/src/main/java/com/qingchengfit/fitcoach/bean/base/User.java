package com.qingchengfit.fitcoach.bean.base;

import android.os.Parcel;
import android.os.Parcelable;

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

public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override public User[] newArray(int size) {
            return new User[size];
        }
    };
    public String username;
    public String phone;
    public int gender;
    public String area_code;
    public String avatar;
    public String id;

    private User(Builder builder) {
        username = builder.username;
        phone = builder.phone;
        gender = builder.gender;
        area_code = builder.area_code;
        avatar = builder.avatar;
        id = builder.id;
    }

    public User() {
    }

    protected User(Parcel in) {
        this.username = in.readString();
        this.phone = in.readString();
        this.gender = in.readInt();
        this.area_code = in.readString();
        this.avatar = in.readString();
        this.id = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.phone);
        dest.writeInt(this.gender);
        dest.writeString(this.area_code);
        dest.writeString(this.avatar);
        dest.writeString(this.id);
    }

    public static final class Builder {
        private String username;
        private String phone;
        private int gender;
        private String area_code;
        private String avatar;
        private String id;

        public Builder() {
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder gender(int val) {
            gender = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
