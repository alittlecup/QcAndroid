package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.List;

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
  public static final Creator<Gym> CREATOR = new Creator<Gym>() {
    @Override public Gym createFromParcel(Parcel source) {
      return new Gym(source);
    }

    @Override public Gym[] newArray(int size) {
      return new Gym[size];
    }
  };
  public String id;
  public String photo;
  public String phone;
  public String name;
  public String brand_name;
  public Brand brand;
  public String description;
  public String weixin;
  public String address;
  public String system_end;
  public CityBean gd_city;
  public DistrictEntity gd_district;
  public Double gd_lng;
  public Double gd_lat;
  public List<String> facilities;
  public Integer staff_count;
  public Integer member_count;
  public Integer coach_count;
  public String area;
  public String detail_description;
  public Integer gd_district_id;//just for upload

  public Gym() {
  }

  private Gym(Builder builder) {
    setId(builder.id);
    setPhoto(builder.photo);
    phone = builder.phone;
    setName(builder.name);
    setBrand_name(builder.brand_name);
    brand = builder.brand;
    setDescription(builder.description);
    setWeixin(builder.weixin);
    setAddress(builder.address);
    setSystem_end(builder.system_end);
    setGd_city(builder.gd_city);
    setGd_district(builder.gd_district);
    setGd_lng(builder.gd_lng);
    setGd_lat(builder.gd_lat);
    setFacilities(builder.facilities);
    staff_count = builder.staff_count;
    member_count = builder.member_count;
    coach_count = builder.coach_count;
    setArea(builder.area);
    detail_description = builder.detail_description;
    gd_district_id = builder.gd_district_id;
  }

  protected Gym(Parcel in) {
    this.id = in.readString();
    this.photo = in.readString();
    this.phone = in.readString();
    this.name = in.readString();
    this.brand_name = in.readString();
    this.brand = in.readParcelable(Brand.class.getClassLoader());
    this.description = in.readString();
    this.weixin = in.readString();
    this.address = in.readString();
    this.system_end = in.readString();
    this.gd_city = in.readParcelable(CityBean.class.getClassLoader());
    this.gd_district = in.readParcelable(DistrictEntity.class.getClassLoader());
    this.gd_lng = (Double) in.readValue(Double.class.getClassLoader());
    this.gd_lat = (Double) in.readValue(Double.class.getClassLoader());
    this.facilities = in.createStringArrayList();
    this.staff_count = (Integer) in.readValue(Integer.class.getClassLoader());
    this.member_count = (Integer) in.readValue(Integer.class.getClassLoader());
    this.coach_count = (Integer) in.readValue(Integer.class.getClassLoader());
    this.area = in.readString();
    this.detail_description = in.readString();
    this.gd_district_id = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public CityBean getGd_city() {
    return gd_city;
  }

  public void setGd_city(CityBean gd_city) {
    this.gd_city = gd_city;
  }

  public Double getGd_lng() {
    return gd_lng;
  }

  public void setGd_lng(Double gd_lng) {
    this.gd_lng = gd_lng;
  }

  public Double getGd_lat() {
    return gd_lat;
  }

  public void setGd_lat(Double gd_lat) {
    this.gd_lat = gd_lat;
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

  public String getCityDistrict() {
    String ret = "";
    if (gd_district != null && gd_district.city != null) {
      ret = ret + gd_district.city.getName() + gd_district.getName();
    }
    return ret;
  }

  public String getCityName() {
    if (gd_city != null) {
      if (TextUtils.isEmpty(gd_city.name)) {
        return "";
      } else {
        return gd_city.name;
      }
    } else if (gd_district != null) {
      if (gd_district.city != null) {
        return gd_district.city.name;
      }
    }
    return "";
  }

  public int getcityCode() {
    if (gd_district != null && gd_district.city != null) {
      return gd_district.city.getId();
    } else {
      return 0;
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBrand_name() {
    if (!TextUtils.isEmpty(brand_name)) return brand_name;
    if (brand != null) return brand.getName();
    return "";
  }

  public void setBrand_name(String brand_name) {
    this.brand_name = brand_name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getWeixin() {
    return weixin;
  }

  public void setWeixin(String weixin) {
    this.weixin = weixin;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getSystem_end() {
    return system_end;
  }

  public void setSystem_end(String system_end) {
    this.system_end = system_end;
  }

  public DistrictEntity getGd_district() {
    return gd_district;
  }

  public void setGd_district(DistrictEntity gd_district) {
    this.gd_district = gd_district;
  }

  public List<String> getFacilities() {
    return facilities;
  }

  public void setFacilities(List<String> facilities) {
    this.facilities = facilities;
  }

  public int getStaff_count() {
    return staff_count;
  }

  public void setStaff_count(int staff_count) {
    this.staff_count = staff_count;
  }

  public int getMember_count() {
    return member_count;
  }

  public void setMember_count(int member_count) {
    this.member_count = member_count;
  }

  public int getCoach_count() {
    return coach_count;
  }

  public void setCoach_count(int coach_count) {
    this.coach_count = coach_count;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.photo);
    dest.writeString(this.phone);
    dest.writeString(this.name);
    dest.writeString(this.brand_name);
    dest.writeParcelable(this.brand, flags);
    dest.writeString(this.description);
    dest.writeString(this.weixin);
    dest.writeString(this.address);
    dest.writeString(this.system_end);
    dest.writeParcelable(this.gd_city, flags);
    dest.writeParcelable(this.gd_district, flags);
    dest.writeValue(this.gd_lng);
    dest.writeValue(this.gd_lat);
    dest.writeStringList(this.facilities);
    dest.writeValue(this.staff_count);
    dest.writeValue(this.member_count);
    dest.writeValue(this.coach_count);
    dest.writeString(this.area);
    dest.writeString(this.detail_description);
    dest.writeValue(this.gd_district_id);
  }

  public static final class Builder {
    private String id;
    private String photo;
    private String phone;
    private String name;
    private String brand_name;
    private Brand brand;
    private String description;
    private String weixin;
    private String address;
    private String system_end;
    private CityBean gd_city;
    private DistrictEntity gd_district;
    private Double gd_lng;
    private Double gd_lat;
    private List<String> facilities;
    private Integer staff_count;
    private Integer member_count;
    private Integer coach_count;
    private String area;
    private String detail_description;
    private Integer gd_district_id;

    public Builder() {
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder photo(String val) {
      photo = val;
      return this;
    }

    public Builder phone(String val) {
      phone = val;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder brand_name(String val) {
      brand_name = val;
      return this;
    }

    public Builder brand(Brand val) {
      brand = val;
      return this;
    }

    public Builder description(String val) {
      description = val;
      return this;
    }

    public Builder weixin(String val) {
      weixin = val;
      return this;
    }

    public Builder address(String val) {
      address = val;
      return this;
    }

    public Builder system_end(String val) {
      system_end = val;
      return this;
    }

    public Builder gd_city(CityBean val) {
      gd_city = val;
      return this;
    }

    public Builder gd_district(DistrictEntity val) {
      gd_district = val;
      return this;
    }

    public Builder gd_lng(Double val) {
      gd_lng = val;
      return this;
    }

    public Builder gd_lat(Double val) {
      gd_lat = val;
      return this;
    }

    public Builder facilities(List<String> val) {
      facilities = val;
      return this;
    }

    public Builder staff_count(Integer val) {
      staff_count = val;
      return this;
    }

    public Builder member_count(Integer val) {
      member_count = val;
      return this;
    }

    public Builder coach_count(Integer val) {
      coach_count = val;
      return this;
    }

    public Builder staff_count(int val) {
      staff_count = val;
      return this;
    }

    public Builder member_count(int val) {
      member_count = val;
      return this;
    }

    public Builder coach_count(int val) {
      coach_count = val;
      return this;
    }

    public Builder area(String val) {
      area = val;
      return this;
    }

    public Builder detail_description(String val) {
      detail_description = val;
      return this;
    }

    public Builder gd_district_id(Integer val) {
      gd_district_id = val;
      return this;
    }

    public Gym build() {
      return new Gym(this);
    }
  }
}
