//package cn.qingchengfit.model.base;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import cn.qingchengfit.model.responese.District;
//import cn.qingchengfit.model.responese.Personage;
//import cn.qingchengfit.model.responese.StaffPosition;
//import cn.qingchengfit.model.responese.StudentReferrerBean;
//
///**
// * //  ┏┓　　　┏┓
// * //┏┛┻━━━┛┻┓
// * //┃　　　　　　　┃
// * //┃　　　━　　　┃
// * //┃　┳┛　┗┳　┃
// * //┃　　　　　　　┃
// * //┃　　　┻　　　┃
// * //┃　　　　　　　┃
// * //┗━┓　　　┏━┛
// * //   ┃　　　┃   神兽保佑
// * //   ┃　　　┃   没有bug
// * //   ┃　　　┗━━━┓
// * //   ┃　　　　　　　┣┓
// * //   ┃　　　　　　　┏┛
// * //   ┗┓┓┏━┳┓┏┛
// * //     ┃┫┫　┃┫┫
// * //     ┗┻┛　┗┻┛
// * //工作人员 bean
// * //Created by yangming on 16/11/18.
// */
//
//public class Staff extends Personage implements Parcelable {
//
//    public String gd_district_id;//地区id
//    public District gd_district;//地区
//    public StaffPosition postion;//
//    public long count;// # 作为推荐人 已推荐人总数
//    public String position_str;
//    public String user_id;
//
//    public Staff() {
//    }
//
//    public Staff(Personage personage) {
//        this.user_id = personage.id;
//        this.avatar = personage.avatar;
//        this.username = personage.username;
//        this.gender = personage.gender;
//    }
//
//    public Staff(String username, String phone, String avatar, int gender) {
//        super(username, phone, avatar, gender);
//    }
//
//    public Staff(String username, String phone, String avatar, String area_code, int gender) {
//        super(username, phone, avatar, area_code, gender);
//    }
//
//    public StudentReferrerBean toReferrerBean() {
//        StudentReferrerBean studentReferrerBean = new StudentReferrerBean();
//        studentReferrerBean.username = this.username;
//        studentReferrerBean.phone = this.phone;
//        studentReferrerBean.id = this.id;
//        studentReferrerBean.avatar = this.avatar;
//        studentReferrerBean.referrerCount = String.valueOf(this.count);
//        return studentReferrerBean;
//    }
//
//    @Override public boolean equals(Object obj) {
//        return super.equals(obj);
//    }
//
//    @Override public int hashCode() {
//        return super.hashCode();
//    }
//
//    public String getGd_district_id() {
//        return gd_district_id;
//    }
//
//    public void setGd_district_id(String gd_district_id) {
//        this.gd_district_id = gd_district_id;
//    }
//
//    public District getGd_district() {
//        return gd_district;
//    }
//
//    public void setGd_district(District gd_district) {
//        this.gd_district = gd_district;
//    }
//
//    public StaffPosition getPostion() {
//        return postion;
//    }
//
//    public String getPosition_str() {
//        return position_str;
//    }
//
//    public void setPostion(StaffPosition postion) {
//        this.postion = postion;
//    }
//
//    public long getCount() {
//        return count;
//    }
//
//    public void setCount(long count) {
//        this.count = count;
//    }
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        super.writeToParcel(dest, flags);
//        dest.writeString(this.gd_district_id);
//        dest.writeParcelable(this.gd_district, flags);
//        dest.writeParcelable(this.postion, flags);
//        dest.writeLong(this.count);
//        dest.writeString(this.position_str);
//        dest.writeString(this.user_id);
//    }
//
//    protected Staff(Parcel in) {
//        super(in);
//        this.gd_district_id = in.readString();
//        this.gd_district = in.readParcelable(District.class.getClassLoader());
//        this.postion = in.readParcelable(StaffPosition.class.getClassLoader());
//        this.count = in.readLong();
//        this.position_str = in.readString();
//        this.user_id = in.readString();
//    }
//
//    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
//        @Override
//        public Staff createFromParcel(Parcel source) {
//            return new Staff(source);
//        }
//
//        @Override
//        public Staff[] newArray(int size) {
//            return new Staff[size];
//        }
//    };
//}
