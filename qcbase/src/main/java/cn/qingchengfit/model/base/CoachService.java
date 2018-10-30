package cn.qingchengfit.model.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Created by Paper on 15/12/28 2015.
 */
@Entity(primaryKeys = {"id","model"},tableName = "coachservice")
public class CoachService implements Parcelable{

  @Ignore
  public static final Parcelable.Creator<CoachService> CREATOR =
      new Parcelable.Creator<CoachService>() {
        @Override public CoachService createFromParcel(Parcel source) {
          return new CoachService(source);
        }

        @Override public CoachService[] newArray(int size) {
          return new CoachService[size];
        }
      };
  //public static RowMapper<CoachService> SELECT_ALL_MAPPER = FACTORY.getAllCoachServiceMapper();
  @NonNull
  @SerializedName("model") public String model = "";
  @SerializedName("type") public int type;
  @NonNull
  @SerializedName("id") public String id = "";

  public void setCan_trial(boolean can_trial) {
    this.can_trial = can_trial;
  }

  public void setGd_lng(double gd_lng) {
    this.gd_lng = gd_lng;
  }

  public void setGd_lat(double gd_lat) {
    this.gd_lat = gd_lat;
  }

  @SerializedName("name") public String name;
  @SerializedName("color") public String color;
  @SerializedName("photo") public String photo;
  public String phone;
  @SerializedName("host") public String host;
  @SerializedName("brand_name") public String brand_name;
  @SerializedName("courses_count") public int courses_count;
  @SerializedName("users_count") public int users_count;
  public String brand_id;
  @SerializedName("has_permission") public boolean has_permission = true;
  @Ignore
  @SerializedName("gd_district") public DistrictEntity gd_district;
  public String system_end;
  public String gym_id;
  public String address;
  public String shop_id;
  public String position;
  public boolean can_trial;
  public double gd_lng;
  public double gd_lat;
  @Ignore
  public int gym_type;
  @Ignore
  public String description;
  @Ignore
  public int area;

  private CoachService(Builder builder) {
    setModel(builder.model);
    setType(builder.type);
    setId(builder.id);
    setName(builder.name);
    setColor(builder.color);
    setPhoto(builder.photo);
    setPhone(builder.phone);
    setHost(builder.host);
    setBrand_name(builder.brand_name);
    setCourses_count(builder.courses_count);
    setUsers_count(builder.users_count);
    setBrand_id(builder.brand_id);
    setHas_permission(builder.has_permission);
    setGd_district(builder.gd_district);
    setSystem_end(builder.system_end);
    setGym_id(builder.gym_id);
    setAddress(builder.address);
    setShop_id(builder.shop_id);
    setPosition(builder.position);
    setGym_type(builder.gym_type);
    setDescription(builder.description);
    setArea(builder.area);
    can_trial = builder.can_trial;
  }

  public CoachService() {
  }

  protected CoachService(Parcel in) {
    this.model = in.readString();
    this.type = in.readInt();
    this.id = in.readString();
    this.name = in.readString();
    this.color = in.readString();
    this.photo = in.readString();
    this.phone = in.readString();
    this.host = in.readString();
    this.brand_name = in.readString();
    this.courses_count = in.readInt();
    this.users_count = in.readInt();
    this.brand_id = in.readString();
    this.has_permission = in.readByte() != 0;
    this.gd_district = in.readParcelable(DistrictEntity.class.getClassLoader());
    this.system_end = in.readString();
    this.gym_id = in.readString();
    this.address = in.readString();
    this.shop_id = in.readString();
    this.position = in.readString();
    this.gym_type = in.readInt();
    this.description = in.readString();
    this.area = in.readInt();
    this.can_trial = in.readByte() != 0;
  }
  @Ignore
  @Nullable  public String id() {
    return id + "";
  }
  @Ignore
  @Nullable  public String model() {
    return model;
  }
  @Ignore
  @Nullable  public String gym_id() {
    return gym_id;
  }
  @Ignore
  @Nullable  public Integer type() {
    return type;
  }
  @Ignore
  @Nullable  public String name() {
    return name;
  }
  @Ignore
  @Nullable  public String color() {
    return color;
  }
  @Ignore
  @Nullable  public String photo() {
    return photo;
  }
  @Ignore
  @Nullable  public String host() {
    return host;
  }
  @Ignore
  @Nullable  public String brand_name() {
    return brand_name;
  }
  @Ignore
  @Nullable  public String shop_id() {
    return shop_id;
  }
  @Ignore
  @Nullable  public Integer courses_count() {
    return courses_count;
  }
  @Ignore
  @Nullable  public Integer users_count() {
    return users_count;
  }
  @Ignore
  @Nullable  public String brand_id() {
    return brand_id;
  }
  @Ignore
  public String system_end() {
    return system_end;
  }
  @Ignore
  @Nullable  public String phone() {
    return phone;
  }
  @Ignore
  @Nullable  public String address() {
    return address;
  }
  @Ignore
  @Nullable  public String position() {
    return position;
  }
  @Ignore
  @Nullable  public Boolean can_trial() {
    return can_trial;
  }
  @Ignore
  @Nullable  public DistrictEntity gd_district() {
    return null;
  }

  public boolean isCan_trial() {
    return can_trial;
  }

  public double getGd_lng() {
    return gd_lng;
  }

  public double getGd_lat() {
    return gd_lat;
  }

  public String getDistrictStr() {
    if (gd_district != null && gd_district.province != null && gd_district.city != null) {
      if (gd_district.city.name.startsWith(gd_district.province.name)) {
        return gd_district.city.name;
      } else {
        return gd_district.province.name + gd_district.city.name;
      }
    } else {
      return "";
    }
  }

  public int getcityCode() {
    if (gd_district != null && gd_district.city != null) {
      return gd_district.city.getId();
    } else {
      return 0;
    }
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getcityName() {
    if (gd_district != null && gd_district.city != null) {
      return gd_district.city.name;
    } else {
      return "";
    }
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public boolean isHas_permission() {
    return has_permission;
  }

  public void setHas_permission(boolean has_permission) {
    this.has_permission = has_permission;
  }

  public String getShop_id() {
    return shop_id;
  }

  public void setShop_id(String shop_id) {
    this.shop_id = shop_id;
  }

  public DistrictEntity getGd_district() {
    return gd_district;
  }

  public void setGd_district(DistrictEntity gd_district) {
    this.gd_district = gd_district;
  }

  public String getSystem_end() {
    return system_end;
  }

  public void setSystem_end(String system_end) {
    this.system_end = system_end;
  }

  public String getGym_id() {
    return gym_id;
  }

  public void setGym_id(String gym_id) {
    this.gym_id = gym_id;
  }

  public String getBrand_id() {
    return brand_id;
  }

  public void setBrand_id(String brand_id) {
    this.brand_id = brand_id;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getBrand_name() {
    return brand_name;
  }

  public void setBrand_name(String brand_name) {
    this.brand_name = brand_name;
  }

  public int getCourses_count() {
    return courses_count;
  }

  public void setCourses_count(int courses_count) {
    this.courses_count = courses_count;
  }

  public int getUsers_count() {
    return users_count;
  }

  public void setUsers_count(int users_count) {
    this.users_count = users_count;
  }

  public void setArea(int area) {
    this.area = area;
  }

  public int getArea() {
    return area;
  }

  public void setGym_type(int gym_type) {
    this.gym_type = gym_type;
  }

  public int getGym_type() {
    return gym_type;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.model);
    dest.writeInt(this.type);
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.color);
    dest.writeString(this.photo);
    dest.writeString(this.phone);
    dest.writeString(this.host);
    dest.writeString(this.brand_name);
    dest.writeInt(this.courses_count);
    dest.writeInt(this.users_count);
    dest.writeString(this.brand_id);
    dest.writeByte(this.has_permission ? (byte) 1 : (byte) 0);
    dest.writeParcelable(this.gd_district, flags);
    dest.writeString(this.system_end);
    dest.writeString(this.gym_id);
    dest.writeString(this.address);
    dest.writeString(this.shop_id);
    dest.writeString(this.position);
    dest.writeString(this.description);
    dest.writeInt(this.area);
    dest.writeInt(this.gym_type);
    dest.writeByte(this.can_trial ? (byte) 1 : (byte) 0);
  }

  public static final class Builder {
    private String model;
    private int type;
    private String id;
    private String name;
    private String color;
    private String photo;
    private String phone;
    private String host;
    private String brand_name;
    private int courses_count;
    private int users_count;
    private String brand_id;
    private boolean has_permission;
    private DistrictEntity gd_district;
    private String system_end;
    private String gym_id;
    private String address;
    private String shop_id;
    private String position;
    private boolean can_trial;
    private int area;
    private String description;
    private int gym_type;

    public Builder() {
    }

    public Builder model(String val) {
      model = val;
      return this;
    }

    public Builder type(int val) {
      type = val;
      return this;
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder color(String val) {
      color = val;
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

    public Builder host(String val) {
      host = val;
      return this;
    }

    public Builder brand_name(String val) {
      brand_name = val;
      return this;
    }

    public Builder courses_count(int val) {
      courses_count = val;
      return this;
    }

    public Builder users_count(int val) {
      users_count = val;
      return this;
    }

    public Builder brand_id(String val) {
      brand_id = val;
      return this;
    }

    public Builder has_permission(boolean val) {
      has_permission = val;
      return this;
    }

    public Builder gd_district(DistrictEntity val) {
      gd_district = val;
      return this;
    }

    public Builder system_end(String val) {
      system_end = val;
      return this;
    }

    public Builder gym_id(String val) {
      gym_id = val;
      return this;
    }

    public Builder address(String val) {
      address = val;
      return this;
    }

    public Builder shop_id(String val) {
      shop_id = val;
      return this;
    }

    public Builder position(String val) {
      position = val;
      return this;
    }

    public Builder can_trial(boolean val) {
      can_trial = val;
      return this;
    }

    public Builder area(int val) {
      area = val;
      return this;
    }

    public Builder description(String val) {
      description = val;
      return this;
    }

    public Builder gym_type(int val) {
      gym_type = val;
      return this;
    }

    public CoachService build() {
      return new CoachService(this);
    }
  }
}
