package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.utils.CmStringUtils;
import java.util.ArrayList;
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
 * Created by Paper on 16/4/6 2016.
 */
public class User_Student implements Parcelable, Cloneable {

    private String username;
    private String id;
    private String date_of_birth;
    private String phone;
    private String address;
    private String joined_at;
    private String avatar;
    private String checkin_avatar;
    private int gender;
    private List<Staff> sellers;
    private String seller_ids;
    private List<Staff> coaches;
    private String coach_ids;
    private String shops;
    private String recommend_by_id;
    private String recommend_by;
    private String origin;
    private String remarks;
    private int status;
    private String area_code;
    private User cloud_user;

    public User getCloud_user() {
        return cloud_user;
    }

    public void setCloud_user(User cloud_user) {
        this.cloud_user = cloud_user;
    }

    private User_Student(Builder builder) {
        setUsername(builder.username);
        setId(builder.id);
        setDate_of_birth(builder.date_of_birth);
        setPhone(builder.phone);
        setAddress(builder.address);
        setJoined_at(builder.joined_at);
        setAvatar(builder.avatar);
        setCheckin_avatar(builder.checkin_avatar);
        setGender(builder.gender);
        setSellers(builder.sellers);
        setSeller_ids(builder.seller_ids);
        setShops(builder.shops);
        setRecommend_by_id(builder.recommend_by_id);
        setRecommend_by(recommend_by);
        setOrigin(builder.origin);
        setOrigin(builder.remarks);
        status = builder.status;
        setStatus(builder.status);
        area_code = builder.area_code;
    }

    public User_Student() {
    }

    public StudentBean toStudentBean() {
        StudentBean bean = new StudentBean();
        bean.setId(id);
        bean.setUsername(username);
        bean.avatar = avatar;
        bean.gender = gender == 0;
        bean.phone = phone;
        return bean;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShops() {
        return shops;
    }

    public void setShops(String shops) {
        this.shops = shops;
    }

    public String getRecommend_by_id() {
        return recommend_by_id;
    }

    public void setRecommend_by_id(String recommend_by_id) {
        this.recommend_by_id = recommend_by_id;
    }

    public String getRecommend_by() {
        return recommend_by;
    }

    public void setRecommend_by(String recommend_by) {
        this.recommend_by = recommend_by;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Staff> getCoaches() {
        return coaches;
    }

    public void setCoaches(List<Staff> coaches) {
        this.coaches = coaches;
    }

    public void setCoach_ids(String coach_ids) {
        this.coach_ids = coach_ids;
    }

    public String getSeller_ids() {
        if (CmStringUtils.isEmpty(seller_ids)) {
            List<String> sid = new ArrayList<>();
            if (sellers != null) {
                for (int i = 0; i < sellers.size(); i++) {
                    sid.add(sellers.get(i).id);
                }
            }
            return CmStringUtils.List2Str(sid);
        }
        return seller_ids;
    }

    public void setSeller_ids(String seller_ids) {
        this.seller_ids = seller_ids;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(String joined_at) {
        this.joined_at = joined_at;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCheckin_avatar() {
        return checkin_avatar;
    }

    public void setCheckin_avatar(String checkin_avatar) {
        this.checkin_avatar = checkin_avatar;
    }

    public List<Staff> getSellers() {
        return sellers;
    }

    public void setSellers(List<Staff> sellers) {
        this.sellers = sellers;
    }

    @Override protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static final class Builder {
        private String username;
        private String id;
        private String date_of_birth;
        private String phone;
        private String address;
        private String joined_at;
        private String avatar;
        private String checkin_avatar;
        private int gender;
        private List<Staff> sellers;
        private String seller_ids;
        private String shops;
        private String recommend_by_id;
        private String recommend_by;
        private String origin;
        private String remarks;
        private int status;
        private String area_code;

        public Builder() {
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder date_of_birth(String val) {
            date_of_birth = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder joined_at(String val) {
            joined_at = val;
            return this;
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }

        public Builder checkin_avatar(String val) {
            checkin_avatar = val;
            return this;
        }

        public Builder gender(int val) {
            gender = val;
            return this;
        }

        public Builder sellers(List<Staff> val) {
            sellers = val;
            return this;
        }

        public Builder seller_ids(String val) {
            seller_ids = val;
            return this;
        }

        public Builder shops(String val) {
            shops = val;
            return this;
        }

        public Builder recommend_by_id(String val) {
            recommend_by_id = val;
            return this;
        }

        public Builder recommend_by(String val) {
            recommend_by = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public Builder origin(String val) {
            origin = val;
            return this;
        }

        public Builder remarks(String val) {
            remarks = val;
            return this;
        }

        public Builder status(int val) {
            status = val;
            return this;
        }

        public User_Student build() {
            return new User_Student(this);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.id);
        dest.writeString(this.date_of_birth);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.joined_at);
        dest.writeString(this.avatar);
        dest.writeString(this.checkin_avatar);
        dest.writeInt(this.gender);
        dest.writeTypedList(this.sellers);
        dest.writeString(this.seller_ids);
        dest.writeTypedList(this.coaches);
        dest.writeString(this.coach_ids);
        dest.writeString(this.shops);
        dest.writeString(this.recommend_by_id);
        dest.writeString(this.recommend_by);
        dest.writeString(this.origin);
        dest.writeString(this.remarks);
        dest.writeInt(this.status);
        dest.writeString(this.area_code);
        dest.writeParcelable(this.cloud_user, flags);
    }

    protected User_Student(Parcel in) {
        this.username = in.readString();
        this.id = in.readString();
        this.date_of_birth = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.joined_at = in.readString();
        this.avatar = in.readString();
        this.checkin_avatar = in.readString();
        this.gender = in.readInt();
        this.sellers = in.createTypedArrayList(Staff.CREATOR);
        this.seller_ids = in.readString();
        this.coaches = in.createTypedArrayList(Staff.CREATOR);
        this.coach_ids = in.readString();
        this.shops = in.readString();
        this.recommend_by_id = in.readString();
        this.recommend_by = in.readString();
        this.origin = in.readString();
        this.remarks = in.readString();
        this.status = in.readInt();
        this.area_code = in.readString();
        this.cloud_user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<User_Student> CREATOR = new Creator<User_Student>() {
        @Override public User_Student createFromParcel(Parcel source) {
            return new User_Student(source);
        }

        @Override public User_Student[] newArray(int size) {
            return new User_Student[size];
        }
    };
}
