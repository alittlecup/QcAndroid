package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.usecase.bean.QcScheduleBean;
import java.util.ArrayList;
import java.util.List;

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
 * //学员 bean
 * //Created by yangming on 16/11/18.
 */

public class Student extends Personage implements Parcelable {

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override public Student[] newArray(int size) {
            return new Student[size];
        }
    };
    /**
     * 0 # 新注册
     * 1 # 跟进中
     * 2 # 会员
     * 3 # 非会员
     */

    public int status;//会员状态,--student
    public List<Staff> sellers;//所属销售--student
    public String support_gym;//会员所属健身房（all）的名字
    public String supoort_gym_ids;//会员所属健身房（all）的id
    public List<QcScheduleBean.Shop> shops = new ArrayList<>();

    public String date_of_birth;//生日
    public String address;//地址
    public String joined_at;//注册日期
    public String seller_ids;
    public String track_record;
    public String first_card_info;
    public String origin;
    public Staff recommend_by;//推荐人
    public String score;

    public Student() {
    }

    protected Student(Parcel in) {
        super(in);
        status = in.readInt();
        sellers = in.createTypedArrayList(Staff.CREATOR);
        support_gym = in.readString();
        supoort_gym_ids = in.readString();
        shops = in.createTypedArrayList(QcScheduleBean.Shop.CREATOR);
    }

    public Student(Parcel in, int status, List<Staff> sellers, String support_gym, String supoort_gym_ids, List<QcScheduleBean.Shop> shops,
        String date_of_birth, String address, String joined_at, String seller_ids) {
        super(in);
        this.status = status;
        this.sellers = sellers;
        this.support_gym = support_gym;
        this.supoort_gym_ids = supoort_gym_ids;
        this.shops = shops;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.joined_at = joined_at;
        this.seller_ids = seller_ids;
    }

    public StudentBean toStudentBean() {
        StudentBean studentBean = new StudentBean();
        studentBean.id = this.id;
        studentBean.username = this.username;
        studentBean.gender = "0".equals(this.gender);
        studentBean.avatar = this.avatar;
        studentBean.joined_at = this.joined_at;
        studentBean.phone = this.phone;
        studentBean.sellers = this.sellers;
        studentBean.brandid = this.brand_id;
        studentBean.checkin_avatar = this.checkin_avatar;
        String support = "";
        String support_ids = "";
        for (int i = 0; i < shops.size(); i++) {
            QcScheduleBean.Shop shop = shops.get(i);
            support = TextUtils.concat(support, shop.name).toString();
            support_ids = TextUtils.concat(support_ids, shop.id).toString();
            if (i != shops.size() - 1) {
                support = TextUtils.concat(support, "，").toString();
                support_ids = TextUtils.concat(support_ids, "，").toString();
            }
        }
        studentBean.setSupport_shop(support);
        studentBean.setSupport_shop_ids(support_ids);

        return studentBean;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(status);
        parcel.writeTypedList(sellers);
        parcel.writeString(support_gym);
        parcel.writeString(supoort_gym_ids);
        parcel.writeTypedList(shops);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Staff> getSellers() {
        return sellers;
    }

    public void setSellers(List<Staff> sellers) {
        this.sellers = sellers;
    }

    public String getSupport_gym() {
        return support_gym;
    }

    public void setSupport_gym(String support_gym) {
        this.support_gym = support_gym;
    }

    public String getSupoort_gym_ids() {
        return supoort_gym_ids;
    }

    public void setSupoort_gym_ids(String supoort_gym_ids) {
        this.supoort_gym_ids = supoort_gym_ids;
    }

    public List<QcScheduleBean.Shop> getShops() {
        return shops;
    }

    public void setShops(List<QcScheduleBean.Shop> shops) {
        this.shops = shops;
    }

    public String getJoin_at() {
        return joined_at;
    }
}
