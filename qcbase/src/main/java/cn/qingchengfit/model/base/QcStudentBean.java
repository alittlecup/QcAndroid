package cn.qingchengfit.model.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.Constants;
import cn.qingchengfit.widgets.AlphabetView;
import com.google.gson.annotations.SerializedName;
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
 * 学员model -- database 存储 bean ；view display ；
 * <p/>
 * Created by Paper on 15/10/14 2015.
 */
@Entity(tableName = "menbers", primaryKeys = { "id" }) public class QcStudentBean extends Personage
    implements Parcelable {//extends RealmObject

  /**
   * 0 # 新注册
   * 1 # 跟进中
   * 2 # 会员
   * 3 # 非会员
   */
  public int status;//会员状态,--student
  @SerializedName(value = "sellers", alternate = { "users" }) @Ignore public List<Staff> sellers;
  //所属销售--student
  @Ignore public List<Staff> coaches;//所属教练--student

  public String joined_at;
  private String join_at;
  private String support_gym;//student
  private String supoort_gym_ids;//student
  private String district_id;//地区--Staff
  @Ignore private District district;//--Staff
  @Ignore private StaffPosition postion;//--Staff
  @Ignore private List<Shop> shops;
  @Ignore @SerializedName(value = "cloud_user", alternate = "user") private User cloud_user;
  @Ignore @SerializedName(value = "ship_id") private String shipId;//--Staff

  public QcStudentBean() {
  }

  public QcStudentBean(Personage personage) {
    this.username = personage.username;
    this.gender = personage.gender;
    this.id = personage.id;
    this.avatar = personage.avatar;
    this.phone = personage.phone;
  }

  public QcStudentBean(String id, String username, int status, String phone, String avatar,
      String checkin_avatar, String gender, String head, String brand_id, String join_at,
      String support_gym, String supoort_gym_ids, String district_id, District district) {
    this.id = id;
    this.username = username;
    this.status = status;
    this.phone = phone;
    this.avatar = avatar;
    this.checkin_avatar = checkin_avatar;
    this.gender = Integer.parseInt(gender);
    this.head = head;
    this.brand_id = brand_id;
    this.join_at = join_at;
    this.support_gym = support_gym;
    this.supoort_gym_ids = supoort_gym_ids;
    this.district_id = district_id;
    this.district = district;
  }

  private QcStudentBean(Builder builder) {
    setTag(builder.tag);
    setId(builder.id);
    setUsername(builder.username);
    setArea_code(builder.area_code);
    setPhone(builder.phone);
    setAvatar(builder.avatar);
    setCheckin_avatar(builder.checkin_avatar);
    setGender(builder.gender);
    setHead(builder.head);
    setBrand_id(builder.brand_id);
    is_superuser = builder.is_superuser;
    setStatus(builder.status);
    setSellers(builder.sellers);
    joined_at = builder.joined_at;
    setJoin_at(builder.join_at);
    setSupport_gym(builder.support_gym);
    setSupoort_gym_ids(builder.supoort_gym_ids);
    setDistrict_id(builder.district_id);
    setDistrict(builder.district);
    postion = builder.postion;
    setShops(builder.shops);
  }

  protected QcStudentBean(Parcel in) {
    status = in.readInt();
    sellers = in.createTypedArrayList(Staff.CREATOR);
    coaches = in.createTypedArrayList(Staff.CREATOR);
    tag = in.readString();
    id = in.readString();
    username = in.readString();
    phone = in.readString();
    avatar = in.readString();
    checkin_avatar = in.readString();
    gender = in.readInt();
    head = in.readString();
    brand_id = in.readString();
    join_at = in.readString();
    support_gym = in.readString();
    supoort_gym_ids = in.readString();
    district_id = in.readString();
    district = in.readParcelable(District.class.getClassLoader());
    postion = in.readParcelable(StaffPosition.class.getClassLoader());
    shops = in.createTypedArrayList(Shop.CREATOR);
  }

  @Ignore public static final Parcelable.Creator<QcStudentBean> CREATOR =
      new Parcelable.Creator<QcStudentBean>() {
        @Override public QcStudentBean createFromParcel(Parcel in) {
          return new QcStudentBean(in);
        }

        @Override public QcStudentBean[] newArray(int size) {
          return new QcStudentBean[size];
        }
      };

  public StudentBean toStudentBean(String brand_id, String gymid, String gymmodel) {
    String curHead = "";
    StudentBean bean = new StudentBean();
    bean.status = this.status;
    bean.avatar = this.avatar;
    bean.username = this.username;
    bean.systemUrl = "后台无数据";
    bean.id = this.id;
    bean.color = "";
    bean.support_shop = this.support_gym;
    bean.support_shop_ids = this.supoort_gym_ids;

    bean.brandid = brand_id;
    bean.joined_at = TextUtils.isEmpty(this.joined_at) ? "" : this.joined_at;
    bean.modelid = gymid;
    bean.model = gymmodel;
    if (TextUtils.isEmpty(this.getHead()) || !AlphabetView.Alphabet.contains(this.getHead())) {
      bean.head = "~";
    } else {
      bean.head = this.getHead().toUpperCase();
    }
    if (!curHead.equalsIgnoreCase(bean.head)) {
      bean.setIsTag(true);
      curHead = bean.head;
    }

    StringBuffer sb = new StringBuffer();
    sb.append("手机：").append(this.phone());
    bean.phone = sb.toString();
    if (this.getGender() == 0) {
      bean.gender = true;
    } else {
      bean.gender = false;
    }
    return bean;
  }

  @Override public boolean equals(Object obj) {
    if (obj instanceof Personage) {
      return ((Personage) obj).getId().equals(this.getId());
    } else {
      return false;
    }
  }

  @Override public int hashCode() {
    return getId().hashCode();
  }

  public District getDistrict() {
    return district;
  }

  public void setDistrict(District district) {
    this.district = district;
  }

  public String getDistrict_id() {
    return district_id;
  }

  public void setDistrict_id(String district_id) {
    this.district_id = district_id;
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

  public String getSellersStr() {
    if (sellers == null || sellers.size() == 0) {
      return "--";
    } else {
      String ret = "--";
      for (int i = 0; i < sellers.size(); i++) {
        if (i < sellers.size() - 1) {
          ret = ret.concat(sellers.get(i).getUsername()).concat(Constants.SEPARATOR_EN);
        } else {
          ret = ret.concat(sellers.get(i).getUsername());
        }
      }
      return ret;
    }
  }

  public String getCheckin_avatar() {
    return checkin_avatar;
  }

  public void setCheckin_avatar(String checkin_avatar) {
    this.checkin_avatar = checkin_avatar;
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

  public String getBrand_id() {
    return brand_id;
  }

  public void setBrand_id(String brand_id) {
    this.brand_id = brand_id;
  }

  public String getJoin_at() {
    return join_at;
  }

  public void setJoin_at(String join_at) {
    this.join_at = join_at;
  }

  public List<Shop> getShops() {
    return shops;
  }

  public void setShops(List<Shop> shops) {
    this.shops = shops;
  }

  public String getId() {
    return id;
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
  //
  //public String getGender() {
  //    return Integer.toString(gender);
  //}
  //
  //
  //public void setGender(String gender) {
  //    this.gender = gender;
  //}

  public String getHead() {
    return head;
  }

  public void setHead(String head) {
    this.head = head;
  }

  @Ignore @Nullable public String id() {
    return id;
  }

  @Ignore @Nullable public String username() {
    return username;
  }

  @Ignore @Nullable public String status() {
    return String.valueOf(status);
  }

  @Ignore @Nullable public String phone() {
    return phone;
  }

  @Ignore @Nullable public String avatar() {
    return avatar;
  }

  @Ignore @Nullable public String checkin_avatar() {
    return checkin_avatar;
  }

  @Ignore @Nullable public Integer gender() {
    return gender;
  }

  @Ignore @Nullable public String head() {
    return head;
  }

  @Ignore @Nullable public String brand_id() {
    return brand_id;
  }

  @Ignore @Nullable public String join_at() {
    return join_at;
  }

  @Ignore @Nullable public String joined_at() {
    return joined_at;
  }

  @Ignore @Nullable public String support_gym() {
    return support_gym;
  }

  public String getJoined_at() {
    return joined_at;
  }

  public StaffPosition getPostion() {
    return postion;
  }

  public User getCloud_user() {
    return cloud_user;
  }

  @Ignore @Nullable

  public String supoort_gym_ids() {
    return supoort_gym_ids;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(status);
    parcel.writeTypedList(sellers);
    parcel.writeTypedList(coaches);
    parcel.writeString(tag);
    parcel.writeString(id);
    parcel.writeString(username);
    parcel.writeString(phone);
    parcel.writeString(avatar);
    parcel.writeString(checkin_avatar);
    parcel.writeInt(gender);
    parcel.writeString(head);
    parcel.writeString(brand_id);
    parcel.writeString(join_at);
    parcel.writeString(support_gym);
    parcel.writeString(supoort_gym_ids);
    parcel.writeString(district_id);
    parcel.writeParcelable(district, i);
    parcel.writeParcelable(postion, i);
    parcel.writeTypedList(shops);
  }

  public String getShipId() {
    return shipId;
  }

  public void setShipId(String shipId) {
    this.shipId = shipId;
  }

  public static class Builder {
    private String tag;
    private String id;
    private String username;
    private String area_code;
    private String phone;
    private String avatar;
    private String checkin_avatar;
    private int gender;
    private String head;
    private String brand_id;
    private boolean is_superuser;
    private String join_at;
    private String joined_at;
    private String support_gym;
    private String supoort_gym_ids;
    private String district_id;
    private District district;
    private StaffPosition postion;
    private List<Shop> shops;
    private List<Staff> sellers;
    private int status;

    public Builder() {
    }

    public Builder sellers(List<Staff> sellers) {
      this.sellers = sellers;
      return this;
    }

    public Builder tag(String tag) {
      this.tag = tag;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder area_code(String val) {
      area_code = val;
      return this;
    }

    public Builder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder avatar(String avatar) {
      this.avatar = avatar;
      return this;
    }

    public Builder checkin_avatar(String checkin_avatar) {
      this.checkin_avatar = checkin_avatar;
      return this;
    }

    public Builder gender(int gender) {
      this.gender = gender;
      return this;
    }

    public Builder head(String head) {
      this.head = head;
      return this;
    }

    public Builder brand_id(String brand_id) {
      this.brand_id = brand_id;
      return this;
    }

    public Builder is_superuser(boolean val) {
      is_superuser = val;
      return this;
    }

    public Builder status(int val) {
      status = val;
      return this;
    }

    public Builder join_at(String join_at) {
      this.join_at = join_at;
      return this;
    }

    public Builder joined_at(String val) {
      joined_at = val;
      return this;
    }

    public Builder support_gym(String support_gym) {
      this.support_gym = support_gym;
      return this;
    }

    public Builder supoort_gym_ids(String supoort_gym_ids) {
      this.supoort_gym_ids = supoort_gym_ids;
      return this;
    }

    public Builder district_id(String district_id) {
      this.district_id = district_id;
      return this;
    }

    public Builder district(District district) {
      this.district = district;
      return this;
    }

    public Builder postion(StaffPosition postion) {
      this.postion = postion;
      return this;
    }

    public Builder shops(List<Shop> shops) {
      this.shops = shops;
      return this;
    }

    public QcStudentBean build() {
      return new QcStudentBean(this);
    }

    public Builder status(String val) {
      status = Integer.valueOf(val);
      return this;
    }
  }

  public void setJoined_at(String joined_at) {
    this.joined_at = joined_at;
  }

  public void setPostion(StaffPosition postion) {
    this.postion = postion;
  }

  public void setCloud_user(User cloud_user) {
    this.cloud_user = cloud_user;
  }
}
