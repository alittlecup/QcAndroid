package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.qingchengfit.staffkit.usecase.bean.QcScheduleBean;
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
 * Created by Paper on 16/2/23 2016.
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
    public List<QcScheduleBean.Shop> shops;
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
        this.shops = in.createTypedArrayList(QcScheduleBean.Shop.CREATOR);
        this.options = in.createTypedArrayList(CardTplOption.CREATOR);
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

    public List<QcScheduleBean.Shop> getShops() {
        return shops;
    }

    public void setShops(List<QcScheduleBean.Shop> shops) {
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
            for (QcScheduleBean.Shop shop : shops) {
                ret.add(shop.id);
            }
        }
        return ret;
    }

    public String getShopNames() {
        String ret = "";
        if (shops != null && shops.size() > 0) {
            for (QcScheduleBean.Shop shop : shops) {
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
        return limit;
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
}
