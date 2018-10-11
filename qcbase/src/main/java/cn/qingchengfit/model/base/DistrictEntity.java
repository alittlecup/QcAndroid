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
 * Created by Paper on 2017/5/22.
 *
 *  地址信息
 */

public class DistrictEntity implements Parcelable {
    public static final Creator<DistrictEntity> CREATOR = new Creator<DistrictEntity>() {
        public DistrictEntity createFromParcel(Parcel source) {
            return new DistrictEntity(source);
        }

        public DistrictEntity[] newArray(int size) {
            return new DistrictEntity[size];
        }
    };
    @SerializedName("province") public ProvinceBean province;
    @SerializedName("city") public CityBean city;
    @SerializedName("code") public String id;
    @SerializedName("name") public String name;

    public DistrictEntity() {
    }

    protected DistrictEntity(Parcel in) {
        this.province = in.readParcelable(ProvinceBean.class.getClassLoader());
        this.city = in.readParcelable(CityBean.class.getClassLoader());
        this.id = in.readString();
        this.name = in.readString();
    }

  public String getName() {
    return name;
  }

  public int getId() {
    try {
      return Integer.parseInt(id);
    } catch (Exception e) {
      return 0;
    }
  }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.province, flags);
        dest.writeParcelable(this.city, flags);
        dest.writeString(this.id);
        dest.writeString(this.name);
    }
}
