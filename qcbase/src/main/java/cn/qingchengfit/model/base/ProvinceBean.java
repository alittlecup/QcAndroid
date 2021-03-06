package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
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
 * Created by Paper on 15/9/18 2015.
 *
 *  省信息
 */
public class ProvinceBean implements Parcelable {
    public static final Creator<ProvinceBean> CREATOR = new Creator<ProvinceBean>() {
      @Override public ProvinceBean createFromParcel(Parcel source) {
            return new ProvinceBean(source);
        }

      @Override public ProvinceBean[] newArray(int size) {
            return new ProvinceBean[size];
        }
    };
  @SerializedName("code") public String id;
    @SerializedName("name") public String name;
    @SerializedName("cities") public List<CityBean> cities;

    public ProvinceBean() {
    }

    protected ProvinceBean(Parcel in) {
      this.id = in.readString();
        this.name = in.readString();
      this.cities = in.createTypedArrayList(CityBean.CREATOR);
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.id);
        dest.writeString(this.name);
      dest.writeTypedList(this.cities);
    }
}
