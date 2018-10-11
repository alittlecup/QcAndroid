package cn.qingchengfit.model.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
@Entity(tableName = "permissions",primaryKeys = {"key","shop_id"})
public class Permission implements Parcelable {
    @Ignore
    public static final Parcelable.Creator<Permission> CREATOR =
        new Parcelable.Creator<Permission>() {
            @Override public Permission createFromParcel(Parcel source) {
                return new Permission(source);
            }

            @Override public Permission[] newArray(int size) {
                return new Permission[size];
            }
        };
    @NonNull
    private String shop_id = "";
    private boolean value;
    @NonNull
    private String key = "";

    public Permission() {
    }

    public Permission(Buider buider) {
        setShop_id(buider.shop_id);
        setKey(buider.key);
        setValue(buider.value);
    }

    protected Permission(Parcel in) {
        this.shop_id = in.readString();
        this.value = in.readByte() != 0;
        this.key = in.readString();
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Ignore
    @Nullable  public String shop_id() {
        return shop_id;
    }
    @Ignore
    @Nullable  public Boolean value() {
        return value;
    }
    @Ignore
    @Nullable  public String key() {
        return key;
    }

    @Override public int describeContents() {
        return 0;
    }



    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shop_id);
        dest.writeByte(this.value ? (byte) 1 : (byte) 0);
        dest.writeString(this.key);
    }

    public static final class Buider {
        private String shop_id;
        private String key;
        private Boolean value;

        public Buider shop_id(String shop_id) {
            this.shop_id = shop_id;
            return this;
        }

        public Buider key(String key) {
            this.key = key;
            return this;
        }

        public Buider value(Boolean value) {
            this.value = value;
            return this;
        }

        public Permission buid() {
            return new Permission(this);
        }
    }

}
