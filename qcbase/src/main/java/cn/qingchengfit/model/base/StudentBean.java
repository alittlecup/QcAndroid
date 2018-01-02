package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/15 2015.
 */
@Deprecated
public class StudentBean implements Parcelable {

    public String username;
    public String phone;
    public String avatar;
    public String checkin_avatar;
    public boolean gender;
    public String systemUrl;
    public String head;
    public String id;
    public boolean isTag;
    public boolean isChosen;//选择列表中,用来标识item得选中状态
    public boolean isOrigin = false;//判断是否是原有名下会员
    public String color;
    public String modelid;
    public String model;
    public String brandid;
    public String joined_at;
    public int modeltype;
    public String support_shop;
    public String support_shop_ids;
    public int status;//会员状态,
    public List<Staff> sellers;
    public User cloud_user;

    public StudentBean() {
    }

    @Override
    public boolean equals(Object o) {
        return this.id.equalsIgnoreCase(((StudentBean) o).getId());
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getjoined_at() {
        return joined_at;
    }

    public void setjoined_at(String joined_at) {
        this.joined_at = joined_at;
    }

    public String getSupport_shop_ids() {
        return support_shop_ids;
    }

    public void setSupport_shop_ids(String support_shop_ids) {
        this.support_shop_ids = support_shop_ids;
    }

    public List<String> getSupportIdList() {
        if (TextUtils.isEmpty(support_shop_ids)) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(support_shop_ids.split(","));
        }
    }

    public String getCheckin_avatar() {
        return checkin_avatar;
    }

    public void setCheckin_avatar(String checkin_avatar) {
        this.checkin_avatar = checkin_avatar;
    }

    public String getSupport_shop() {
        return support_shop;
    }

    public void setSupport_shop(String support_shop) {
        this.support_shop = support_shop;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(String systemUrl) {
        this.systemUrl = systemUrl;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTag() {
        return isTag;
    }

    public void setIsTag(boolean isTag) {
        this.isTag = isTag;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getModeltype() {
        return modeltype;
    }

    public void setModeltype(int modeltype) {
        this.modeltype = modeltype;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.phone);
        dest.writeString(this.avatar);
        dest.writeString(this.checkin_avatar);
        dest.writeByte(this.gender ? (byte) 1 : (byte) 0);
        dest.writeString(this.systemUrl);
        dest.writeString(this.head);
        dest.writeString(this.id);
        dest.writeByte(this.isTag ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isChosen ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOrigin ? (byte) 1 : (byte) 0);
        dest.writeString(this.color);
        dest.writeString(this.modelid);
        dest.writeString(this.model);
        dest.writeString(this.brandid);
        dest.writeString(this.joined_at);
        dest.writeInt(this.modeltype);
        dest.writeString(this.support_shop);
        dest.writeString(this.support_shop_ids);
        dest.writeInt(this.status);
        dest.writeTypedList(this.sellers);
        dest.writeParcelable(this.cloud_user, flags);
    }

    protected StudentBean(Parcel in) {
        this.username = in.readString();
        this.phone = in.readString();
        this.avatar = in.readString();
        this.checkin_avatar = in.readString();
        this.gender = in.readByte() != 0;
        this.systemUrl = in.readString();
        this.head = in.readString();
        this.id = in.readString();
        this.isTag = in.readByte() != 0;
        this.isChosen = in.readByte() != 0;
        this.isOrigin = in.readByte() != 0;
        this.color = in.readString();
        this.modelid = in.readString();
        this.model = in.readString();
        this.brandid = in.readString();
        this.joined_at = in.readString();
        this.modeltype = in.readInt();
        this.support_shop = in.readString();
        this.support_shop_ids = in.readString();
        this.status = in.readInt();
        this.sellers = in.createTypedArrayList(Staff.CREATOR);
        this.cloud_user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<StudentBean> CREATOR = new Creator<StudentBean>() {
        @Override public StudentBean createFromParcel(Parcel source) {
            return new StudentBean(source);
        }

        @Override public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };
}
