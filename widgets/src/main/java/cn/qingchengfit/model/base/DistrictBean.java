package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

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
 */
public class DistrictBean implements Parcelable {
  public static final Parcelable.Creator<DistrictBean> CREATOR =
      new Parcelable.Creator<DistrictBean>() {
        @Override public DistrictBean createFromParcel(Parcel source) {
          return new DistrictBean(source);
        }

        @Override public DistrictBean[] newArray(int size) {
          return new DistrictBean[size];
        }
      };
  @SerializedName("gd_city_id") public String city_id;
  @SerializedName("code") public String id;
    @SerializedName("name") public String name;

  public DistrictBean() {
  }

  protected DistrictBean(Parcel in) {
    this.city_id = in.readString();
    this.id = in.readString();
    this.name = in.readString();
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
    dest.writeString(this.city_id);
    dest.writeString(this.id);
    dest.writeString(this.name);
  }
}
