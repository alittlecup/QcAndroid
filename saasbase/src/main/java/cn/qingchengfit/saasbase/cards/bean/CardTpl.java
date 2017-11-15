package cn.qingchengfit.saasbase.cards.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.Shop;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/8/14.
 */

public class CardTpl implements Parcelable {
  public static final Creator<CardTpl> CREATOR = new Creator<CardTpl>() {
    @Override public CardTpl createFromParcel(Parcel source) {
      return new CardTpl(source);
    }

    @Override public CardTpl[] newArray(int size) {
      return new CardTpl[size];
    }
  };
  public String name;
  public String type_name;
  public int type; //1 是储值卡 2次卡 3期限卡
  public int tpl_type;    //1 是储值卡 2次卡 3期限卡  (筛选用)
  public String description;
  public List<String> costs;
  public boolean isChoosen;
  public String id;
  public String limit;
  public String color;
  public boolean is_limit;
  public int month_times;
  public int day_times;
  public int week_times;
  public int pre_times;
  public int buy_limit;
  public boolean is_enable = true; //是否停用
  public String gymid;
  public String gymModel;
  public List<Shop> shops;
  public List<CardTplOption> options;

  public CardTpl() {
  }




  public CardTpl(String name, int type, String description, String id, String limit) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.id = id;
    this.limit = limit;
  }

  protected CardTpl(Parcel in) {
    this.name = in.readString();
    this.type_name = in.readString();
    this.type = in.readInt();
    this.tpl_type = in.readInt();
    this.description = in.readString();
    this.costs = in.createStringArrayList();
    this.isChoosen = in.readByte() != 0;
    this.id = in.readString();
    this.limit = in.readString();
    this.color = in.readString();
    this.is_limit = in.readByte() != 0;
    this.month_times = in.readInt();
    this.day_times = in.readInt();
    this.week_times = in.readInt();
    this.pre_times = in.readInt();
    this.buy_limit = in.readInt();
    this.is_enable = in.readByte() != 0;
    this.gymid = in.readString();
    this.gymModel = in.readString();
    this.shops = in.createTypedArrayList(Shop.CREATOR);
    this.options = in.createTypedArrayList(CardTplOption.CREATOR);
  }

  private CardTpl(Builder builder) {
    setName(builder.name);
    type_name = builder.type_name;
    setType(builder.type);
    tpl_type = builder.tpl_type;
    setDescription(builder.description);
    setCosts(builder.costs);
    isChoosen = builder.isChoosen;
    setId(builder.id);
    setLimit(builder.limit);
    setColor(builder.color);
    setIs_limit(builder.is_limit);
    setMonth_times(builder.month_times);
    setDay_times(builder.day_times);
    setWeek_times(builder.week_times);
    setPre_times(builder.pre_times);
    setBuy_limit(builder.buy_limit);
    is_enable = builder.is_enable;
    setGymid(builder.gymid);
    setGymModel(builder.gymModel);
    setShops(builder.shops);
    setOptions(builder.options);
  }

  @Override public boolean equals(Object o) {
    return this.id.equalsIgnoreCase(((CardTpl) o).id);
  }

  @Override public int hashCode() {
    return id.hashCode();
  }

  public int getCardTypeInt() {
    return type;
  }

  public List<CardTplOption> getOptions() {
    return options;
  }

  public void setOptions(List<CardTplOption> options) {
    this.options = options;
  }

  public List<Shop> getShops() {
    return shops;
  }

  public void setShops(List<Shop> shops) {
    this.shops = shops;
  }

  public String getGymid() {
    return gymid;
  }

  public void setGymid(String gymid) {
    this.gymid = gymid;
  }

  public String getGymModel() {
    return gymModel;
  }

  public void setGymModel(String gymModel) {
    this.gymModel = gymModel;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public boolean is_limit() {
    return is_limit;
  }

  public void setIs_limit(boolean is_limit) {
    this.is_limit = is_limit;
  }

  public int getMonth_times() {
    return month_times;
  }

  public void setMonth_times(int month_times) {
    this.month_times = month_times;
  }

  public int getDay_times() {
    return day_times;
  }

  public void setDay_times(int day_times) {
    this.day_times = day_times;
  }

  public int getWeek_times() {
    return week_times;
  }

  public void setWeek_times(int week_times) {
    this.week_times = week_times;
  }

  public int getPre_times() {
    return pre_times;
  }

  public void setPre_times(int pre_times) {
    this.pre_times = pre_times;
  }

  public List<String> getShopIds() {
    List<String> ret = new ArrayList<>();
    if (shops != null && shops.size() > 0) {
      for (Shop shop : shops) {
        ret.add(shop.id);
      }
    }
    return ret;
  }

  public String getShopNames() {
    String ret = "";
    if (shops != null && shops.size() > 0) {
      for (Shop shop : shops) {
        ret = TextUtils.concat(ret, shop.name, "、").toString();
      }
    }
    if (ret.endsWith("、")) ret = ret.substring(0, ret.length() - 1);
    return ret;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getCosts() {
    return costs;
  }

  public void setCosts(List<String> costs) {
    this.costs = costs;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLimit() {
    if (is_limit()) {
      StringBuffer ss = new StringBuffer();
      if (getPre_times() != 0) {
        ss.append("限制: 可提前预约");
        ss.append(getPre_times());
        ss.append("节课");
      }
      if (getDay_times() != 0) {
        if (getPre_times() != 0) ss.append(",");
        ss.append("每天共计可上").append(getDay_times()).append("节课");
      } else if (getWeek_times() != 0) {
        if (getPre_times() != 0) ss.append(",");
        ss.append("每周共计可上").append(getWeek_times()).append("节课");
      } else if (getMonth_times() != 0) {
        if (getPre_times() != 0) ss.append(",");
        ss.append("每月共计可上").append(getMonth_times()).append("节课");
      }
      if (getBuy_limit() != 0) {
        if ((ss.toString() != null && ss.toString().equalsIgnoreCase(""))) ss.append(",");
        ss.append("每个会员限购").append(getBuy_limit()).append("张");
      }
      if (TextUtils.isEmpty(ss.toString())) {
        return "限制: 无";
      } else {
        return ss.toString();
      }
    }else return "限制: 无";
  }

  public void setLimit(String limit) {
    this.limit = limit;
  }

  public int getBuy_limit() {
    return buy_limit;
  }

  public void setBuy_limit(int buy_limit) {
    this.buy_limit = buy_limit;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.type_name);
    dest.writeInt(this.type);
    dest.writeInt(this.tpl_type);
    dest.writeString(this.description);
    dest.writeStringList(this.costs);
    dest.writeByte(this.isChoosen ? (byte) 1 : (byte) 0);
    dest.writeString(this.id);
    dest.writeString(this.limit);
    dest.writeString(this.color);
    dest.writeByte(this.is_limit ? (byte) 1 : (byte) 0);
    dest.writeInt(this.month_times);
    dest.writeInt(this.day_times);
    dest.writeInt(this.week_times);
    dest.writeInt(this.pre_times);
    dest.writeInt(this.buy_limit);
    dest.writeByte(this.is_enable ? (byte) 1 : (byte) 0);
    dest.writeString(this.gymid);
    dest.writeString(this.gymModel);
    dest.writeTypedList(this.shops);
    dest.writeTypedList(this.options);
  }


  public static final class Builder {
    private String name;
    private String type_name;
    private int type;
    private int tpl_type;
    private String description;
    private List<String> costs;
    private boolean isChoosen;
    private String id;
    private String limit;
    private String color;
    private boolean is_limit;
    private int month_times;
    private int day_times;
    private int week_times;
    private int pre_times;
    private int buy_limit;
    private boolean is_enable;
    private String gymid;
    private String gymModel;
    private List<Shop> shops;
    private List<CardTplOption> options;

    public Builder() {
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder type_name(String val) {
      type_name = val;
      return this;
    }

    public Builder type(int val) {
      type = val;
      return this;
    }

    public Builder tpl_type(int val) {
      tpl_type = val;
      return this;
    }

    public Builder description(String val) {
      description = val;
      return this;
    }

    public Builder costs(List<String> val) {
      costs = val;
      return this;
    }

    public Builder isChoosen(boolean val) {
      isChoosen = val;
      return this;
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder limit(String val) {
      limit = val;
      return this;
    }

    public Builder color(String val) {
      color = val;
      return this;
    }

    public Builder is_limit(boolean val) {
      is_limit = val;
      return this;
    }

    public Builder month_times(int val) {
      month_times = val;
      return this;
    }

    public Builder day_times(int val) {
      day_times = val;
      return this;
    }

    public Builder week_times(int val) {
      week_times = val;
      return this;
    }

    public Builder pre_times(int val) {
      pre_times = val;
      return this;
    }

    public Builder buy_limit(int val) {
      buy_limit = val;
      return this;
    }

    public Builder is_enable(boolean val) {
      is_enable = val;
      return this;
    }

    public Builder gymid(String val) {
      gymid = val;
      return this;
    }

    public Builder gymModel(String val) {
      gymModel = val;
      return this;
    }

    public Builder shops(List<Shop> val) {
      shops = val;
      return this;
    }

    public Builder options(List<CardTplOption> val) {
      options = val;
      return this;
    }

    public CardTpl build() {
      return new CardTpl(this);
    }
  }
}
