//package cn.qingchengfit.model.base;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.support.annotation.Nullable;
//import cn.qingchengfit.model.responese.District;
//import cn.qingchengfit.staffkit.model.db.CoachServiceModel;
//import com.google.gson.Gson;
//import com.google.gson.annotations.SerializedName;
//import com.squareup.sqldelight.ColumnAdapter;
//import com.squareup.sqldelight.RowMapper;
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * <p/>
// * Created by Paper on 15/12/28 2015.
// */
//public class CoachService implements Parcelable, CoachServiceModel { //extends RealmObject
//
//    public static final ColumnAdapter<Integer,> CALENDAR_ADAPTER_INTEGER = new ColumnAdapter<Integer>() {
//
//        @Override public Integer map(Cursor cursor, int columnIndex) {
//            return cursor.getInt(columnIndex);
//        }
//
//        @Override public void marshal(ContentValues contentValues, String key, Integer value) {
//            contentValues.put(key, value);
//        }
//    };
//    public static final Factory<CoachService> FACTORY = new Factory<>(new CoachServiceModel.Creator<CoachService>() {
//        @Override public CoachService create(@Nullable String id, @Nullable String model, @Nullable String gym_id, @Nullable Integer type,
//            @Nullable String name, @Nullable String color, @Nullable String photo, @Nullable String host, @Nullable String brand_name,
//            @Nullable String shop_id, @Nullable Integer courses_count, @Nullable Integer users_count, @Nullable String brand_id,
//            @Nullable String system_end, @Nullable String phone, @Nullable String address, @Nullable Boolean can_trial,
//            @Nullable String district, @Nullable String position) {
//
//            return new Builder().id(id)
//                .model(model)
//                .gym_id(gym_id)
//                .type(type)
//                .name(name)
//                .color(color)
//                .photo(photo)
//                .host(host)
//                .brand_name(brand_name)
//                .shop_id(shop_id)
//                .courses_count(courses_count)
//                .users_count(users_count)
//                .brand_id(brand_id)
//                .system_end(system_end)
//                .phone(phone)
//                .address(address)
//                .can_trial(can_trial)
//                .position(position)
//                .district(district)
//                .build();
//        }
//    }, CALENDAR_ADAPTER_INTEGER, CALENDAR_ADAPTER_INTEGER, CALENDAR_ADAPTER_INTEGER);
//
//    public static final RowMapper<CoachService> MAPPER = new Mapper<>(FACTORY);
//
//    private String autoId;
//    private String id;
//    private String model;
//    private String gym_id;
//    private int type;
//    private String name;
//    private String color;
//    private String photo;
//    private String host;
//    private String brand_name;
//    private String shop_id;
//    private int courses_count;
//    private int users_count;
//    private String brand_id;
//    private String system_end;
//    private String phone;
//    private String address;
//    private String position;
//    @SerializedName("gd_district") private District district;
//    private boolean can_trial = false;
//
//    public String getPosition() {
//        return position;
//    }
//
//    public void setPosition(String position) {
//        this.position = position;
//    }
//
//    public CoachService() {
//    }
//
//    private CoachService(Builder builder) {
//        setAutoId(builder.autoId);
//        setId(builder.id);
//        setModel(builder.model);
//        setGym_id(builder.gym_id);
//        setType(builder.type);
//        setName(builder.name);
//        setColor(builder.color);
//        setPhoto(builder.photo);
//        setHost(builder.host);
//        setBrand_name(builder.brand_name);
//        setShop_id(builder.shop_id);
//        setCourses_count(builder.courses_count);
//        setUsers_count(builder.users_count);
//        setBrand_id(builder.brand_id);
//        setSystem_end(builder.system_end);
//        setPhone(builder.phone);
//        setAddress(builder.address);
//        setPosition(builder.position);
//        district = builder.district;
//        setCan_trial(builder.can_trial);
//    }
//
//    public String getGym_id() {
//        return gym_id;
//    }
//
//    public void setGym_id(String gym_id) {
//        this.gym_id = gym_id;
//    }
//
//    public String getAutoId() {
//        return autoId;
//    }
//
//    public void setAutoId(String autoId) {
//        this.autoId = autoId;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getAddress() {
//        //if (district != null){
//        //    String ret = "";
//        //    if (district.getProvince() != null){
//        //        ret = ret.concat(district.getProvince().getName());
//        //    }
//        //    if (district.getCity() != null){
//        //        ret = ret.concat(district.getCity().getName());
//        //    }
//        //    return ret.concat(address);
//        //}
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getSystem_end() {
//        return system_end;
//    }
//
//    public void setSystem_end(String system_end) {
//        this.system_end = system_end;
//    }
//
//    public String getShop_id() {
//        return shop_id;
//    }
//
//    public void setShop_id(String shop_id) {
//        this.shop_id = shop_id;
//    }
//
//    public String getBrand_id() {
//        return brand_id;
//    }
//
//    public void setBrand_id(String brand_id) {
//        this.brand_id = brand_id;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getModel() {
//        return model;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public String getHost() {
//        return host;
//    }
//
//    public void setHost(String host) {
//        this.host = host;
//    }
//
//    public String getBrand_name() {
//        return brand_name;
//    }
//
//    public void setBrand_name(String brand_name) {
//        this.brand_name = brand_name;
//    }
//
//    public int getCourses_count() {
//        return courses_count;
//    }
//
//    public void setCourses_count(int courses_count) {
//        this.courses_count = courses_count;
//    }
//
//    public int getUsers_count() {
//        return users_count;
//    }
//
//    public void setUsers_count(int users_count) {
//        this.users_count = users_count;
//    }
//
//    public boolean isCan_trial() {
//        return can_trial;
//    }
//
//    public void setCan_trial(boolean can_trial) {
//        this.can_trial = can_trial;
//    }
//
//    public int getcityCode() {
//        if (district != null && district.getCity() != null) {
//            return district.getCity().getCode();
//        } else {
//            return 0;
//        }
//    }
//
//    public String getcityName() {
//        if (district != null && district.getCity() != null) {
//            return district.getCity().getName();
//        } else {
//            return "";
//        }
//    }
//
//    @Nullable @Override public String id() {
//        return id;
//    }
//
//    @Nullable @Override public String model() {
//        return model;
//    }
//
//    @Nullable @Override public String gym_id() {
//        return gym_id;
//    }
//
//    @Nullable @Override public Integer type() {
//        return type;
//    }
//
//    @Nullable @Override public String name() {
//        return name;
//    }
//
//    @Nullable @Override public String color() {
//        return color;
//    }
//
//    @Nullable @Override public String photo() {
//        return photo;
//    }
//
//    @Nullable @Override public String host() {
//        return host;
//    }
//
//    @Nullable @Override public String brand_name() {
//        return brand_name;
//    }
//
//    @Nullable @Override public String shop_id() {
//        return shop_id;
//    }
//
//    @Nullable @Override public Integer courses_count() {
//        return courses_count;
//    }
//
//    @Nullable @Override public Integer users_count() {
//        return users_count;
//    }
//
//    @Nullable @Override public String brand_id() {
//        return brand_id;
//    }
//
//    @Nullable @Override public String system_end() {
//        return system_end;
//    }
//
//    @Nullable @Override public String phone() {
//        return phone;
//    }
//
//    @Nullable @Override public String address() {
//        return address;
//    }
//
//    @Nullable @Override public Boolean can_trial() {
//        return can_trial;
//    }
//
//    @Nullable @Override public String district() {
//        return new Gson().toJson(district, District.class);
//    }
//
//    @Nullable @Override public String position() {
//        return position;
//    }
//
//    public static final class Builder {
//        private String autoId;
//        private String id;
//        private String model;
//        private String gym_id;
//        private int type;
//        private String name;
//        private String color;
//        private String photo;
//        private String host;
//        private String brand_name;
//        private String shop_id;
//        private int courses_count;
//        private int users_count;
//        private String brand_id;
//        private String system_end;
//        private String phone;
//        private String address;
//        private String position;
//        private District district;
//        private boolean can_trial;
//        private boolean is_superuser;
//
//        public Builder() {
//        }
//
//        public Builder autoId(String val) {
//            autoId = val;
//            return this;
//        }
//
//        public Builder id(String val) {
//            id = val;
//            return this;
//        }
//
//        public Builder model(String val) {
//            model = val;
//            return this;
//        }
//
//        public Builder gym_id(String val) {
//            gym_id = val;
//            return this;
//        }
//
//        public Builder type(int val) {
//            type = val;
//            return this;
//        }
//
//        public Builder name(String val) {
//            name = val;
//            return this;
//        }
//
//        public Builder color(String val) {
//            color = val;
//            return this;
//        }
//
//        public Builder photo(String val) {
//            photo = val;
//            return this;
//        }
//
//        public Builder host(String val) {
//            host = val;
//            return this;
//        }
//
//        public Builder brand_name(String val) {
//            brand_name = val;
//            return this;
//        }
//
//        public Builder shop_id(String val) {
//            shop_id = val;
//            return this;
//        }
//
//        public Builder courses_count(int val) {
//            courses_count = val;
//            return this;
//        }
//
//        public Builder users_count(int val) {
//            users_count = val;
//            return this;
//        }
//
//        public Builder brand_id(String val) {
//            brand_id = val;
//            return this;
//        }
//
//        public Builder system_end(String val) {
//            system_end = val;
//            return this;
//        }
//
//        public Builder phone(String val) {
//            phone = val;
//            return this;
//        }
//
//        public Builder address(String val) {
//            address = val;
//            return this;
//        }
//
//        public Builder position(String val) {
//            position = val;
//            return this;
//        }
//
//        public Builder district(District val) {
//            district = val;
//            return this;
//        }
//
//        public Builder district(String val) {
//            district = new Gson().fromJson(val, District.class);
//            return this;
//        }
//
//        public Builder can_trial(boolean val) {
//            can_trial = val;
//            return this;
//        }
//
//        public CoachService build() {
//            return new CoachService(this);
//        }
//    }
//
//    @Override public int describeContents() {
//        return 0;
//    }
//
//    @Override public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.autoId);
//        dest.writeString(this.id);
//        dest.writeString(this.model);
//        dest.writeString(this.gym_id);
//        dest.writeInt(this.type);
//        dest.writeString(this.name);
//        dest.writeString(this.color);
//        dest.writeString(this.photo);
//        dest.writeString(this.host);
//        dest.writeString(this.brand_name);
//        dest.writeString(this.shop_id);
//        dest.writeInt(this.courses_count);
//        dest.writeInt(this.users_count);
//        dest.writeString(this.brand_id);
//        dest.writeString(this.system_end);
//        dest.writeString(this.phone);
//        dest.writeString(this.address);
//        dest.writeString(this.position);
//        dest.writeParcelable(this.district, flags);
//        dest.writeByte(this.can_trial ? (byte) 1 : (byte) 0);
//    }
//
//    protected CoachService(Parcel in) {
//        this.autoId = in.readString();
//        this.id = in.readString();
//        this.model = in.readString();
//        this.gym_id = in.readString();
//        this.type = in.readInt();
//        this.name = in.readString();
//        this.color = in.readString();
//        this.photo = in.readString();
//        this.host = in.readString();
//        this.brand_name = in.readString();
//        this.shop_id = in.readString();
//        this.courses_count = in.readInt();
//        this.users_count = in.readInt();
//        this.brand_id = in.readString();
//        this.system_end = in.readString();
//        this.phone = in.readString();
//        this.address = in.readString();
//        this.position = in.readString();
//        this.district = in.readParcelable(District.class.getClassLoader());
//        this.can_trial = in.readByte() != 0;
//    }
//
//    public static final Parcelable.Creator<CoachService> CREATOR = new Parcelable.Creator<CoachService>() {
//        @Override public CoachService createFromParcel(Parcel source) {
//            return new CoachService(source);
//        }
//
//        @Override public CoachService[] newArray(int size) {
//            return new CoachService[size];
//        }
//    };
//}
