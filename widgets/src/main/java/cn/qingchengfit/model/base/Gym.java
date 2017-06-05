package cn.qingchengfit.model.base;

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
 * Created by Paper on 2017/6/1.
 *
 * Could 上的gym结构
 */
public class Gym implements Parcelable {
    public static final Parcelable.Creator<Gym> CREATOR = new Parcelable.Creator<Gym>() {
        @Override public Gym createFromParcel(Parcel source) {
            return new Gym(source);
        }

        @Override public Gym[] newArray(int size) {
            return new Gym[size];
        }
    };
    public String id;
    public String photo;
    public String name;
    public String brand_name;
    public String description;
    public String weixin;
    public String address;
    public String system_end;
    public DistrictEntity gd_district;
    public Long gd_lng;
    public Long gd_lat;
    public String facilities;

    public Gym() {
    }

    protected Gym(Parcel in) {
        this.id = in.readString();
        this.photo = in.readString();
        this.name = in.readString();
        this.brand_name = in.readString();
        this.description = in.readString();
        this.weixin = in.readString();
        this.address = in.readString();
        this.system_end = in.readString();
        this.gd_district = in.readParcelable(DistrictEntity.class.getClassLoader());
        this.gd_lng = (Long) in.readValue(Long.class.getClassLoader());
        this.gd_lat = (Long) in.readValue(Long.class.getClassLoader());
        this.facilities = in.readString();
    }

    public String getAddressStr() {
        String plus = address == null ? "" : address;

        if (gd_district != null && gd_district.province != null && gd_district.city != null) {
            if (gd_district.city.name.startsWith(gd_district.province.name)) {
                return gd_district.city.name + plus;
            } else {
                return gd_district.province.name + gd_district.city.name + plus;
            }
        } else {
            return "" + plus;
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.photo);
        dest.writeString(this.name);
        dest.writeString(this.brand_name);
        dest.writeString(this.description);
        dest.writeString(this.weixin);
        dest.writeString(this.address);
        dest.writeString(this.system_end);
        dest.writeParcelable(this.gd_district, flags);
        dest.writeValue(this.gd_lng);
        dest.writeValue(this.gd_lat);
        dest.writeString(this.facilities);
    }
}
