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
 * 市信息
 */
public class CityBean implements Parcelable {
  public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
    @Override public CityBean createFromParcel(Parcel source) {
      return new CityBean(source);
    }

    @Override public CityBean[] newArray(int size) {
      return new CityBean[size];
    }
  };
  @SerializedName("code") public String id;
  @SerializedName("gd_province_id") public String province_id;
  @SerializedName("name") public String name;
  @SerializedName("districts") public List<DistrictBean> districts;

  public CityBean(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public CityBean(int id, String name) {
    this.id = id + "";
    this.name = name;
  }

  public CityBean() {
  }

  protected CityBean(Parcel in) {
    this.id = in.readString();
    this.province_id = in.readString();
    this.name = in.readString();
    this.districts = in.createTypedArrayList(DistrictBean.CREATOR);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.province_id);
    dest.writeString(this.name);
    dest.writeTypedList(this.districts);
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
}
