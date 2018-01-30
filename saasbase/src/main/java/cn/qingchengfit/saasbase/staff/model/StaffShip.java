package cn.qingchengfit.saasbase.staff.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StaffPosition;
import cn.qingchengfit.model.common.ICommonUser;
import com.google.gson.annotations.SerializedName;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //工作人员 基本信息（staff） 职位信息（StaffPosition）
 * //Created by yangming on 16/11/18.
 */

public class StaffShip implements Parcelable ,ICommonUser{

    @SerializedName("position") public StaffPosition position;
    @SerializedName("cloud_user") public Staff user;
    public Staff teacher;
    @SerializedName("id") public String id;
    public String username;
    public String area_code;
    public String phone;
    public String avatar;
    public int gender;// 0:man；1：woman
    public boolean is_coach;
    public boolean is_staff;


    public boolean staff_enable;
    /**
     *     STATUS_NEW = 1
     STATUS_USED = 2
     STATUS_DISABLE = 3
     STATUS_CHOICES = (
     (STATUS_NEW, u'邀请中'),
     (STATUS_USED, u'已邀请'),
     (STATUS_DISABLE, u'已撤销'),
     )
     */
    @SerializedName("status") public int  status;
    public boolean is_superuser;

    public StaffShip() {
    }

    public StaffPosition getPosition() {
        return position;
    }

    public void setPosition(StaffPosition position) {
        this.position = position;
    }

    public Staff getUser() {
        return user;
    }

    public void setUser(Staff user) {
        this.user = user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isIs_coach() {
        return is_coach;
    }

    public void setIs_coach(boolean is_coach) {
        this.is_coach = is_coach;
    }

    public boolean isIs_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

    public boolean isStaff_enable() {
        return staff_enable;
    }

    public void setStaff_enable(boolean staff_enable) {
        this.staff_enable = staff_enable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isIs_superuser() {
        return is_superuser;
    }

    public void setIs_superuser(boolean is_superuser) {
        this.is_superuser = is_superuser;
    }

    @Override public String getAvatar() {
        return this.avatar;
    }

    @Override public String getTitle() {
        return username;
    }

    @Override public String getSubTitle() {
        return phone;
    }

    @Override public String getContent() {
        return position == null ?"":position.getName();
    }

    @Override public String getId() {
        return id;
    }

    @Override public String getRight() {
        return "";
    }

    @Override public int getRightColor() {
        return 0;
    }

    @Override public int getGender() {
        return gender;
    }

    @Override public boolean filter(String str) {
        return phone.contains(str) || username.contains(str) ;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.position, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.area_code);
        dest.writeString(this.phone);
        dest.writeString(this.avatar);
        dest.writeInt(this.gender);
        dest.writeByte(this.is_coach ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_staff ? (byte) 1 : (byte) 0);
        dest.writeByte(this.staff_enable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.status);
        dest.writeByte(this.is_superuser ? (byte) 1 : (byte) 0);
    }

    protected StaffShip(Parcel in) {
        this.position = in.readParcelable(StaffPosition.class.getClassLoader());
        this.user = in.readParcelable(Staff.class.getClassLoader());
        this.id = in.readString();
        this.username = in.readString();
        this.area_code = in.readString();
        this.phone = in.readString();
        this.avatar = in.readString();
        this.gender = in.readInt();
        this.is_coach = in.readByte() != 0;
        this.is_staff = in.readByte() != 0;
        this.staff_enable = in.readByte() != 0;
        this.status = in.readInt();
        this.is_superuser = in.readByte() != 0;
    }

    public static final Creator<StaffShip> CREATOR = new Creator<StaffShip>() {
        @Override public StaffShip createFromParcel(Parcel source) {
            return new StaffShip(source);
        }

        @Override public StaffShip[] newArray(int size) {
            return new StaffShip[size];
        }
    };
}
