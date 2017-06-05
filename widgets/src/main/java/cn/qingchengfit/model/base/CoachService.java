package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.qingcheng.model.base.CoachServiceModel;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.RowMapper;

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

public class CoachService implements Parcelable, CoachServiceModel {
    @SerializedName("model") public String model;
    @SerializedName("type") public int type;
    @SerializedName("id") public long id;
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
    @SerializedName("gd_district") public DistrictEntity gd_district;
    public String system_end;
    public String gym_id;

    private CoachService(Builder builder) {
        setModel(builder.model);
        setType(builder.type);
        setId(builder.id);
        setName(builder.name);
        setColor(builder.color);
        setPhoto(builder.photo);
        phone = builder.phone;
        setHost(builder.host);
        setBrand_name(builder.brand_name);
        setCourses_count(builder.courses_count);
        setUsers_count(builder.users_count);
        brand_id = builder.brand_id;
        has_permission = builder.has_permission;
        gd_district = builder.gd_district;
        system_end = builder.system_end;
        gym_id = builder.gym_id;
    }

    @Nullable @Override public String id() {
        return id + "";
    }

    @Nullable @Override public String model() {
        return model;
    }

    @Nullable @Override public String gym_id() {
        return gym_id;
    }

    @Nullable @Override public Integer type() {
        return type;
    }

    @Nullable @Override public String name() {
        return name;
    }

    @Nullable @Override public String color() {
        return color;
    }

    @Nullable @Override public String photo() {
        return photo;
    }

    @Nullable @Override public String host() {
        return host;
    }

    @Nullable @Override public String brand_name() {
        return brand_name;
    }

    @Nullable @Override public String shop_id() {
        return null;
    }

    @Nullable @Override public Integer courses_count() {
        return courses_count;
    }

    @Nullable @Override public Integer users_count() {
        return users_count;
    }

    @Nullable @Override public String brand_id() {
        return brand_id;
    }

    public String system_end() {
        return system_end;
    }

    @Nullable @Override public String phone() {
        return null;
    }

    @Nullable @Override public String address() {
        return null;
    }

    @Nullable @Override public Boolean can_trial() {
        return null;
    }

    @Nullable @Override public DistrictEntity gd_district() {
        return null;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public CoachService() {
    }

    public static final class Builder {
        private String model;
        private int type;
        private long id;
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

        public Builder id(long val) {
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

        public CoachService build() {
            return new CoachService(this);
        }
    }

    public static final ColumnAdapter<DistrictEntity, String> gd_districtAdapter =
        new ColumnAdapter<DistrictEntity, String>() {
            Gson gson = new Gson();

            @NonNull @Override public DistrictEntity decode(String s) {
                return gson.fromJson(s, DistrictEntity.class);
            }

            @Override public String encode(@NonNull DistrictEntity districtEntity) {
                return gson.toJson(districtEntity, DistrictEntity.class);
            }
        };

    public static final Factory<CoachService> FACTORY = new Factory<>(new CoachServiceModel.Creator<CoachService>() {
        @Override public CoachService create(@Nullable String id, @Nullable String model, @Nullable String gym_id, @Nullable Integer type,
            @Nullable String name, @Nullable String color, @Nullable String photo, @Nullable String host, @Nullable String brand_name,
            @Nullable String shop_id, @Nullable Integer courses_count, @Nullable Integer users_count, @Nullable String brand_id,
            @Nullable String system_end, @Nullable String phone, @Nullable String address, @Nullable Boolean can_trial,
            @Nullable DistrictEntity gd_district) {
            return new Builder().id(Long.parseLong(id))
                .model(model)
                .name(name)
                .gym_id(gym_id)
                .type(type == null ? 0 : type)
                .brand_id(brand_id)
                .brand_name(brand_name)
                .color(color)
                .gd_district(gd_district)
                .phone(phone)
                .photo(photo)
                .host(host)
                .system_end(system_end)
                .users_count(users_count == null ? 0 : users_count)
                .courses_count(courses_count == null ? 0 : courses_count)
                .build();
        }
    }, gd_districtAdapter);

    public static RowMapper<CoachService> SELECT_ALL_MAPPER = FACTORY.getAllCoachServiceMapper();

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.model);
        dest.writeInt(this.type);
        dest.writeLong(this.id);
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
    }

    protected CoachService(Parcel in) {
        this.model = in.readString();
        this.type = in.readInt();
        this.id = in.readLong();
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
    }

    public static final Parcelable.Creator<CoachService> CREATOR = new Parcelable.Creator<CoachService>() {
        @Override public CoachService createFromParcel(Parcel source) {
            return new CoachService(source);
        }

        @Override public CoachService[] newArray(int size) {
            return new CoachService[size];
        }
    };
}
