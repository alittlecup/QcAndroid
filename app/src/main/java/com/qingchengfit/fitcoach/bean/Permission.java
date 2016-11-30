package com.qingchengfit.fitcoach.bean;

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
 * Created by Paper on 16/8/10.
 */
public class Permission implements Parcelable {


    public String shop_id;
    public boolean value;
    public String key;

    private Permission(Builder builder) {
        shop_id = builder.shop_id;
        value = builder.value;
        key = builder.key;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shop_id);
        dest.writeByte(this.value ? (byte) 1 : (byte) 0);
        dest.writeString(this.key);
    }

    public Permission() {
    }

    protected Permission(Parcel in) {
        this.shop_id = in.readString();
        this.value = in.readByte() != 0;
        this.key = in.readString();
    }

    public static final Creator<Permission> CREATOR = new Creator<Permission>() {
        @Override public Permission createFromParcel(Parcel source) {
            return new Permission(source);
        }

        @Override public Permission[] newArray(int size) {
            return new Permission[size];
        }
    };

    public static final class Builder {
        private String shop_id;
        private boolean value;
        private String key;

        public Builder() {
        }

        public Builder shop_id(String val) {
            shop_id = val;
            return this;
        }

        public Builder value(boolean val) {
            value = val;
            return this;
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public Permission build() {
            return new Permission(this);
        }
    }
}
