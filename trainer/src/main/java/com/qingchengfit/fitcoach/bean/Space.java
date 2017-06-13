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
 * Created by Paper on 16/11/23.
 */

public class Space implements Parcelable {
    public static final Parcelable.Creator<Space> CREATOR = new Parcelable.Creator<Space>() {
        @Override public Space createFromParcel(Parcel source) {
            return new Space(source);
        }

        @Override public Space[] newArray(int size) {
            return new Space[size];
        }
    };
    public int capacity;
    public String name;
    public String id;
    public String shop_id;
    public boolean is_support_team;
    public boolean is_support_private;

    public Space() {
    }

    protected Space(Parcel in) {
        this.capacity = in.readInt();
        this.name = in.readString();
        this.id = in.readString();
        this.shop_id = in.readString();
        this.is_support_team = in.readByte() != 0;
        this.is_support_private = in.readByte() != 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public boolean is_support_team() {
        return is_support_team;
    }

    public void setIs_support_team(boolean is_support_team) {
        this.is_support_team = is_support_team;
    }

    public boolean is_support_private() {
        return is_support_private;
    }

    public void setIs_support_private(boolean is_support_private) {
        this.is_support_private = is_support_private;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.capacity);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.shop_id);
        dest.writeByte(this.is_support_team ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_support_private ? (byte) 1 : (byte) 0);
    }
}
