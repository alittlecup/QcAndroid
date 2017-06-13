package cn.qingchengfit.staffkit.usecase.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import cn.qingchengfit.staffkit.model.db.PermissionModel;
import com.squareup.sqldelight.RowMapper;

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
public class Permission implements Parcelable, PermissionModel {
    public static final Parcelable.Creator<Permission> CREATOR = new Parcelable.Creator<Permission>() {
        @Override public Permission createFromParcel(Parcel in) {
            return new Permission(in);
        }

        @Override public Permission[] newArray(int size) {
            return new Permission[size];
        }
    };
    public static final Factory<Permission> FACTORY = new Factory<>(new PermissionModel.Creator<Permission>() {
        @Override public Permission create(@Nullable String shop_id, @Nullable Boolean value, @Nullable String key) {
            return new Buider().shop_id(shop_id).key(key).value(value).buid();
        }
    });
    public static final RowMapper<Permission> MAPPER = new Mapper<>(FACTORY);
    private String shop_id;
    private boolean value;
    private String key;

    public Permission() {
    }

    public Permission(Buider buider) {
        setShop_id(buider.shop_id);
        setKey(buider.key);
        setValue(buider.value);
    }

    protected Permission(Parcel in) {
        shop_id = in.readString();
        value = in.readByte() != 0;
        key = in.readString();
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

    @Nullable @Override public String shop_id() {
        return shop_id;
    }

    @Nullable @Override public Boolean value() {
        return value;
    }

    @Nullable @Override public String key() {
        return key;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shop_id);
        parcel.writeString(key);
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

        public cn.qingchengfit.staffkit.usecase.bean.Permission buid() {
            return new Permission(this);
        }
    }
}
