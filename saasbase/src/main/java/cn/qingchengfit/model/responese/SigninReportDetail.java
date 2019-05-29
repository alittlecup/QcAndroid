package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by yangming on 16/9/22.
 */

public class SigninReportDetail {

  /**
   * user_count : 99              总的签到人次
   * total_real_price : 2481.6    总的实际收入
   * <p>
   * value_real_price : 2481.6    储值卡实际收入
   * times_real_price : 0         次卡实际收入
   * <p>
   * time_card_count : 14         期限卡签到人数
   * times_card_count : 3         次卡签到人数
   * value_card_count:            储值卡签到人数
   */

  private StatBean stat;
  /**
   * shop : {"id":1,"name":"阳光上东"}
   * status : 4
   * created_at : 2016-09-14T01:06:48
   * real_price : 0.6
   * cost : 1
   * user : {"username":"学民","id":3531}
   * card : {"card_tpl_type":1,"id":2435,"name":"青橙储值卡"}
   */

  private List<CheckinsBean> checkins;

  public StatBean getStat() {
    return stat;
  }

  public void setStat(StatBean stat) {
    this.stat = stat;
  }

  public List<CheckinsBean> getCheckins() {
    return checkins;
  }

  public void setCheckins(List<CheckinsBean> checkins) {
    this.checkins = checkins;
  }

  public static class StatBean {
    private int user_count;
    private double value_real_price;
    private Float times_real_price;
    private int time_card_count;
    private double total_real_price;
    private int times_card_count;
    private int value_card_count;

    public int getValue_card_count() {
      return value_card_count;
    }

    public void setValue_card_count(int value_card_count) {
      this.value_card_count = value_card_count;
    }

    public int getUser_count() {
      return user_count;
    }

    public void setUser_count(int user_count) {
      this.user_count = user_count;
    }

    public double getValue_real_price() {
      return value_real_price;
    }

    public void setValue_real_price(double value_real_price) {
      this.value_real_price = value_real_price;
    }

    public Float getTimes_real_price() {
      return times_real_price;
    }

    public void setTimes_real_price(Float times_real_price) {
      this.times_real_price = times_real_price;
    }

    public int getTime_card_count() {
      return time_card_count;
    }

    public void setTime_card_count(int time_card_count) {
      this.time_card_count = time_card_count;
    }

    public double getTotal_real_price() {
      return total_real_price;
    }

    public void setTotal_real_price(double total_real_price) {
      this.total_real_price = total_real_price;
    }

    public int getTimes_card_count() {
      return times_card_count;
    }

    public void setTimes_card_count(int times_card_count) {
      this.times_card_count = times_card_count;
    }
  }

  public static class CheckinsBean {
    /**
     * id : 1
     * name : 阳光上东
     */

    private ShopBean shop;
    private int status;
    private String created_at;
    private String checkout_at;
    private Float real_price;
    private Float cost;
    /**
     * username : 学民
     * id : 3531
     */

    private UserBean user;
    /**
     * card_tpl_type : 1
     * id : 2435
     * name : 青橙储值卡
     */

    private CardBean card;

    private CheckinOrder order;

    public ShopBean getShop() {
      return shop;
    }

    public void setShop(ShopBean shop) {
      this.shop = shop;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public String getCreated_at() {
      return created_at;
    }

    public void setCreated_at(String created_at) {
      this.created_at = created_at;
    }

    public Float getReal_price() {
      return real_price;
    }

    public void setReal_price(Float real_price) {
      this.real_price = real_price;
    }

    public Float getCost() {
      return cost;
    }

    public void setCost(Float cost) {
      this.cost = cost;
    }

    public UserBean getUser() {
      return user;
    }

    public void setUser(UserBean user) {
      this.user = user;
    }

    public CardBean getCard() {
      return card;
    }

    public void setCard(CardBean card) {
      this.card = card;
    }

    public String getCheckout_at() {
      return checkout_at;
    }

    public void setCheckout_at(String checkout_at) {
      this.checkout_at = checkout_at;
    }

    public CheckinOrder getOrder() {
      return order;
    }

    public void setOrder(CheckinOrder order) {
      this.order = order;
    }

    public static class CheckinOrder {
      private String status;
      private int price;
      private boolean from_trial;
      private String channel;

      public CheckinOrder() {
      }

      public String getChannel() {
        return channel;
      }

      public void setChannel(String channel) {
        this.channel = channel;
      }

      public boolean isFrom_trial() {
        return from_trial;
      }

      public void setFrom_trial(boolean from_trial) {
        this.from_trial = from_trial;
      }

      public int getPrice() {
        return price;
      }

      public void setPrice(int price) {
        this.price = price;
      }

      public String getStatus() {
        return status;
      }

      public void setStatus(String status) {
        this.status = status;
      }
    }

    public static class ShopBean {
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
    }

    public static class UserBean implements Parcelable {
      public static final Parcelable.Creator<UserBean> CREATOR =
          new Parcelable.Creator<UserBean>() {
            @Override public UserBean createFromParcel(Parcel source) {
              return new UserBean(source);
            }

            @Override public UserBean[] newArray(int size) {
              return new UserBean[size];
            }
          };
      private String username;
      private int id;
      private String checkin_avatar;
      private String avatar;

      public UserBean() {
      }

      protected UserBean(Parcel in) {
        this.username = in.readString();
        this.id = in.readInt();
      }

      public String getCheckin_avatar() {
        return checkin_avatar;
      }

      public void setCheckin_avatar(String checkin_avatar) {
        this.checkin_avatar = checkin_avatar;
      }

      public String getAvatar() {
        return avatar;
      }

      public void setAvatar(String avatar) {
        this.avatar = avatar;
      }

      public String getUsername() {
        return username;
      }

      public void setUsername(String username) {
        this.username = username;
      }

      public int getId() {
        return id;
      }

      public void setId(int id) {
        this.id = id;
      }

      @Override public int describeContents() {
        return 0;
      }

      @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeInt(this.id);
      }

      @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBean userBean = (UserBean) o;

        return id == userBean.id;
      }

      @Override public int hashCode() {
        return id;
      }
    }

    public static class CardBean implements Parcelable {

      public static final Creator<CardBean> CREATOR = new Creator<CardBean>() {
        @Override public CardBean createFromParcel(Parcel source) {
          return new CardBean(source);
        }

        @Override public CardBean[] newArray(int size) {
          return new CardBean[size];
        }
      };
      private int card_tpl_type;
      private String card_tpl_id;
      private int id;
      private String name;

      public CardBean() {
      }

      protected CardBean(Parcel in) {
        this.card_tpl_type = in.readInt();
        this.card_tpl_id = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
      }

      public int getCard_tpl_type() {
        return card_tpl_type;
      }

      public void setCard_tpl_type(int card_tpl_type) {
        this.card_tpl_type = card_tpl_type;
      }

      public String getCard_tpl_id() {
        return card_tpl_id;
      }

      public void setCard_tpl_id(String card_tpl_id) {
        this.card_tpl_id = card_tpl_id;
      }

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

      @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardBean cardBean = (CardBean) o;

        return id == cardBean.id;
      }

      @Override public int hashCode() {
        return id;
      }

      @Override public int describeContents() {
        return 0;
      }

      @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.card_tpl_type);
        dest.writeString(this.card_tpl_id);
        dest.writeInt(this.id);
        dest.writeString(this.name);
      }
    }
  }
}
