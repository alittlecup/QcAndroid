package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Shop;

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
 * Created by Paper on 16/8/25.
 *
 * 更衣柜
 */
public class LockerRegion implements Parcelable {
    public static final Creator<LockerRegion> CREATOR = new Creator<LockerRegion>() {
        @Override public LockerRegion createFromParcel(Parcel source) {
            return new LockerRegion(source);
        }

        @Override public LockerRegion[] newArray(int size) {
            return new LockerRegion[size];
        }
    };
    public Shop shop;
    public Long id;
    public String name;
    public int lockers_count;
    public boolean has_used_lockers;

    private LockerRegion(Builder builder) {
        shop = builder.shop;
        id = builder.id;
        name = builder.name;
        lockers_count = builder.lockers_count;
        has_used_lockers = builder.has_used_lockers;
    }

    public LockerRegion() {
    }

    protected LockerRegion(Parcel in) {
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.lockers_count = in.readInt();
        this.has_used_lockers = in.readByte() != 0;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shop, flags);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.lockers_count);
        dest.writeByte(this.has_used_lockers ? (byte) 1 : (byte) 0);
    }

    public static final class Builder {
        private Shop shop;
        private Long id;
        private String name;
        private int lockers_count;
        private boolean has_used_lockers;

        public Builder() {
        }

        public Builder shop(Shop val) {
            shop = val;
            return this;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder lockers_count(int val) {
            lockers_count = val;
            return this;
        }

        public Builder has_used_lockers(boolean val) {
            has_used_lockers = val;
            return this;
        }

        public LockerRegion build() {
            return new LockerRegion(this);
        }
    }
}
