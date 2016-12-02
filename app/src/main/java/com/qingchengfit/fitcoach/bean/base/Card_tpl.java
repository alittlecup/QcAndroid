package com.qingchengfit.fitcoach.bean.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
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
public class Card_tpl implements Parcelable {
    private String name;
    private String type; //1 是储值卡 2次卡 3期限卡
    private String description;
    private List<String> costs;
    public boolean isChoosen;
    private String id;
    private String limit;
    private String color;
    private boolean is_limit;
    private int month_times;
    private int day_times;
    private int week_times;
    private int pre_times;
    private int buy_limit;
    private String gymid;
    private String gymModel;
    private List<Shop> shops;
    private List<CardTplOption> options;


    @Override
    public boolean equals(Object o) {
        return this.id.equalsIgnoreCase(((Card_tpl)o).id);
    }

    public int getCardTypeInt(){
        return Integer.parseInt(type);
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

    public String getShopNames(){
        String ret = "";
        if (shops != null && shops.size() > 0) {
            for (Shop shop : shops) {
                ret = TextUtils.concat(ret,shop.name,"、").toString();
            }
        }
        if (ret.endsWith("、"))
            ret = ret.substring(0,ret.length()-1);
        return ret;
    }

    public Card_tpl() {
    }

    public Card_tpl(String name, String type, String description, String id, String limit) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.id = id;
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
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
        dest.writeString(this.gymid);
        dest.writeString(this.gymModel);
        dest.writeTypedList(this.shops);
        dest.writeTypedList(this.options);
    }

    protected Card_tpl(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
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
        this.gymid = in.readString();
        this.gymModel = in.readString();
        this.shops = in.createTypedArrayList(Shop.CREATOR);
        this.options = in.createTypedArrayList(CardTplOption.CREATOR);
    }

    public static final Creator<Card_tpl> CREATOR = new Creator<Card_tpl>() {
        @Override
        public Card_tpl createFromParcel(Parcel source) {
            return new Card_tpl(source);
        }

        @Override
        public Card_tpl[] newArray(int size) {
            return new Card_tpl[size];
        }
    };
}
