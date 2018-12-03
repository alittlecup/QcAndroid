package cn.qingchengfit.card.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.User;

public class Coupon implements Parcelable {

  /**
   * shop : {"id":1,"name":"安徒生Feature22"}
   * end : 2019-03-07T16:42:08
   * description : 满100.0抵11.0元
   * discount : 11
   * real_price : 89
   * start : 2018-02-08T10:48:27
   * coupon_tpl : {"account":11,"reach_money":100,"id":12,"use_condition":{"id":12,"course_pay_on_line":true,"charge_cards":true,"description":"全店通用","new_cards":true},"coupon_type":1}
   * id : 165
   * user : {"username":"邹英杰","id":545}
   */

  private ShopBean shop;
  private String end;
  private String description;
  private int discount;
  private int real_price;
  private String start;
  private CouponTplBean coupon_tpl;
  private int id;
  private User user;

  public ShopBean getShop() {
    return shop;
  }

  public void setShop(ShopBean shop) {
    this.shop = shop;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getDiscount() {
    return discount;
  }

  public void setDiscount(int discount) {
    this.discount = discount;
  }

  public int getReal_price() {
    return real_price;
  }

  public void setReal_price(int real_price) {
    this.real_price = real_price;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public CouponTplBean getCoupon_tpl() {
    return coupon_tpl;
  }

  public void setCoupon_tpl(CouponTplBean coupon_tpl) {
    this.coupon_tpl = coupon_tpl;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public static class ShopBean implements Parcelable {
    /**
     * id : 1
     * name : 安徒生Feature22
     */

    private int id;
    private String name;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.id);
      dest.writeString(this.name);
    }

    public ShopBean() {
    }

    protected ShopBean(Parcel in) {
      this.id = in.readInt();
      this.name = in.readString();
    }

    public static final Creator<ShopBean> CREATOR = new Creator<ShopBean>() {
      @Override public ShopBean createFromParcel(Parcel source) {
        return new ShopBean(source);
      }

      @Override public ShopBean[] newArray(int size) {
        return new ShopBean[size];
      }
    };
  }

  public static class CouponTplBean implements Parcelable {
    /**
     * account : 11
     * reach_money : 100
     * id : 12
     * use_condition : {"id":12,"course_pay_on_line":true,"charge_cards":true,"description":"全店通用","new_cards":true}
     * coupon_type : 1
     */

    private int account;
    private int reach_money;
    private int id;
    private UseConditionBean use_condition;
    private int coupon_type;

    public int getAccount() {
      return account;
    }

    public void setAccount(int account) {
      this.account = account;
    }

    public int getReach_money() {
      return reach_money;
    }

    public void setReach_money(int reach_money) {
      this.reach_money = reach_money;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public UseConditionBean getUse_condition() {
      return use_condition;
    }

    public void setUse_condition(UseConditionBean use_condition) {
      this.use_condition = use_condition;
    }

    public int getCoupon_type() {
      return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
      this.coupon_type = coupon_type;
    }

    public static class UseConditionBean implements Parcelable {
      /**
       * id : 12
       * course_pay_on_line : true
       * charge_cards : true
       * description : 全店通用
       * new_cards : true
       */

      private int id;
      private boolean course_pay_on_line;
      private boolean charge_cards;
      private String description;
      private boolean new_cards;

      public int getId() {
        return id;
      }

      public void setId(int id) {
        this.id = id;
      }

      public boolean isCourse_pay_on_line() {
        return course_pay_on_line;
      }

      public void setCourse_pay_on_line(boolean course_pay_on_line) {
        this.course_pay_on_line = course_pay_on_line;
      }

      public boolean isCharge_cards() {
        return charge_cards;
      }

      public void setCharge_cards(boolean charge_cards) {
        this.charge_cards = charge_cards;
      }

      public String getDescription() {
        return description;
      }

      public void setDescription(String description) {
        this.description = description;
      }

      public boolean isNew_cards() {
        return new_cards;
      }

      public void setNew_cards(boolean new_cards) {
        this.new_cards = new_cards;
      }

      @Override public int describeContents() {
        return 0;
      }

      @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(this.course_pay_on_line ? (byte) 1 : (byte) 0);
        dest.writeByte(this.charge_cards ? (byte) 1 : (byte) 0);
        dest.writeString(this.description);
        dest.writeByte(this.new_cards ? (byte) 1 : (byte) 0);
      }

      public UseConditionBean() {
      }

      protected UseConditionBean(Parcel in) {
        this.id = in.readInt();
        this.course_pay_on_line = in.readByte() != 0;
        this.charge_cards = in.readByte() != 0;
        this.description = in.readString();
        this.new_cards = in.readByte() != 0;
      }

      public static final Creator<UseConditionBean> CREATOR = new Creator<UseConditionBean>() {
        @Override public UseConditionBean createFromParcel(Parcel source) {
          return new UseConditionBean(source);
        }

        @Override public UseConditionBean[] newArray(int size) {
          return new UseConditionBean[size];
        }
      };
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.account);
      dest.writeInt(this.reach_money);
      dest.writeInt(this.id);
      dest.writeParcelable(this.use_condition, flags);
      dest.writeInt(this.coupon_type);
    }

    public CouponTplBean() {
    }

    protected CouponTplBean(Parcel in) {
      this.account = in.readInt();
      this.reach_money = in.readInt();
      this.id = in.readInt();
      this.use_condition = in.readParcelable(UseConditionBean.class.getClassLoader());
      this.coupon_type = in.readInt();
    }

    public static final Creator<CouponTplBean> CREATOR = new Creator<CouponTplBean>() {
      @Override public CouponTplBean createFromParcel(Parcel source) {
        return new CouponTplBean(source);
      }

      @Override public CouponTplBean[] newArray(int size) {
        return new CouponTplBean[size];
      }
    };
  }

  public Coupon() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.shop, flags);
    dest.writeString(this.end);
    dest.writeString(this.description);
    dest.writeInt(this.discount);
    dest.writeInt(this.real_price);
    dest.writeString(this.start);
    dest.writeParcelable(this.coupon_tpl, flags);
    dest.writeInt(this.id);
    dest.writeParcelable(this.user, flags);
  }

  protected Coupon(Parcel in) {
    this.shop = in.readParcelable(ShopBean.class.getClassLoader());
    this.end = in.readString();
    this.description = in.readString();
    this.discount = in.readInt();
    this.real_price = in.readInt();
    this.start = in.readString();
    this.coupon_tpl = in.readParcelable(CouponTplBean.class.getClassLoader());
    this.id = in.readInt();
    this.user = in.readParcelable(User.class.getClassLoader());
  }

  public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
    @Override public Coupon createFromParcel(Parcel source) {
      return new Coupon(source);
    }

    @Override public Coupon[] newArray(int size) {
      return new Coupon[size];
    }
  };
}
