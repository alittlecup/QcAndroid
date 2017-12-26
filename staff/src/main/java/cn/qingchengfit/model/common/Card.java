package cn.qingchengfit.model.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.staffkit.usecase.bean.QcScheduleBean;
import java.util.ArrayList;
import java.util.List;
//import io.realm.RealmObject;
//import io.realm.annotations.Ignore;
//import io.realm.annotations.PrimaryKey;

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
 * Created by Paper on 16/4/16 2016.
 */
public class Card implements Parcelable {
    public int system_id;
    private String type_name;
    private float balance;
    private List<Student> users;
    private CardTpl card_tpl;
    private String bundleUsers;
    private String description;
    private String name;
    private boolean check_valid;
    private String valid_from;
    private String valid_to;
    private int type;
    private String brand_id;
    private boolean is_locked;
    private boolean is_active;
    private String supportIds;
    private String id;
    private String color;
    private String card_tpl_id;
    private float total_cost;
    private float price;
    private String card_no;
    private String start;
    private String end;
    private float total_account;
    private float total_times;
    private boolean is_auto_start;
    private boolean expired;
    private Integer trial_days;
    private String lock_start;
    private String lock_end;
    //    @Ignore
    private List<QcScheduleBean.Shop> shops;
    public boolean is_open_service_term;
    public CardProtocol card_tpl_service_term;



    public Card() {
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Integer getTrial_days() {
        return trial_days;
    }

    public void setTrial_days(Integer trial_days) {
        this.trial_days = trial_days;
    }

    public boolean is_auto_start() {
        return is_auto_start;
    }

    public void setIs_auto_start(boolean is_auto_start) {
        this.is_auto_start = is_auto_start;
    }

    public float getTotal_account() {
        return total_account;
    }

    public void setTotal_account(float total_account) {
        this.total_account = total_account;
    }

    public float getTotal_times() {
        return total_times;
    }

    public void setTotal_times(float total_times) {
        this.total_times = total_times;
    }

    public List<QcScheduleBean.Shop> getShops() {
        return shops;
    }

    public void setShops(List<QcScheduleBean.Shop> shops) {
        this.shops = shops;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getUsersStr() {
        String ret = "";
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                if (i < users.size() - 1) {
                    ret = TextUtils.concat(ret, users.get(i).getUsername(), "、").toString();
                } else {
                    ret = TextUtils.concat(ret, users.get(i).getUsername()).toString();
                }
            }
        }
        return ret;
    }

    public String getSupportGyms() {
        String ret = "";
        if (getShops() != null) {
            for (int i = 0; i < getShops().size(); i++) {
                if (i < getShops().size() - 1) {
                    ret = TextUtils.concat(ret, getShops().get(i).name, "、").toString();
                } else {
                    ret = TextUtils.concat(ret, getShops().get(i).name).toString();
                }
            }
        }
        return ret;
    }

    public List<String> getUserIds() {
        List<String> ret = new ArrayList<>();
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                ret.add(users.get(i).getId());
            }
        }
        return ret;
    }

    public float getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(float total_cost) {
        this.total_cost = total_cost;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<String> getShopIds() {
        List<String> ret = new ArrayList<>();
        if (!TextUtils.isEmpty(this.supportIds)) {
            if (supportIds.contains(",")) {
                for (String s : supportIds.split(",")) {
                    ret.add(s);
                }
            } else {
                ret.add(supportIds);
            }
        }
        return ret;
    }

    public void setShopIds(List<String> shops) {
        supportIds = "";
        for (int i = 0; i < shops.size(); i++) {
            if (i != shops.size() - 1) {
                supportIds = TextUtils.concat(supportIds, shops.get(i), ",").toString();
            } else {
                supportIds = TextUtils.concat(supportIds, shops.get(i)).toString();
            }
        }
    }

    public String getSupportIds() {
        return supportIds;
    }

    public void setSupportIds(String supportIds) {
        this.supportIds = supportIds;
    }

    public String getBundleUsers() {
        return bundleUsers;
    }

    public void setBundleUsers(String bundleUsers) {
        this.bundleUsers = bundleUsers;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public boolean is_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public String getCard_tpl_id() {
        return card_tpl_id;
    }

    public void setCard_tpl_id(String card_tpl_id) {
        this.card_tpl_id = card_tpl_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public List<Student> getUsers() {
        return users;
    }

    public void setUsers(List<Student> users) {
        this.users = users;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck_valid() {
        return check_valid;
    }

    public void setCheck_valid(boolean check_valid) {
        this.check_valid = check_valid;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CardTpl getCard_tpl() {
        return card_tpl;
    }

    public void setCard_tpl(CardTpl card_tpl) {
        this.card_tpl = card_tpl;
    }

    public String getLock_start() {
        return lock_start;
    }

    public void setLock_start(String lock_start) {
        this.lock_start = lock_start;
    }

    public String getLock_end() {
        return lock_end;
    }

    public void setLock_end(String lock_end) {
        this.lock_end = lock_end;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.system_id);
        dest.writeString(this.type_name);
        dest.writeFloat(this.balance);
        dest.writeTypedList(this.users);
        dest.writeParcelable(this.card_tpl, flags);
        dest.writeString(this.bundleUsers);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeByte(this.check_valid ? (byte) 1 : (byte) 0);
        dest.writeString(this.valid_from);
        dest.writeString(this.valid_to);
        dest.writeInt(this.type);
        dest.writeString(this.brand_id);
        dest.writeByte(this.is_locked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_active ? (byte) 1 : (byte) 0);
        dest.writeString(this.supportIds);
        dest.writeString(this.id);
        dest.writeString(this.color);
        dest.writeString(this.card_tpl_id);
        dest.writeFloat(this.total_cost);
        dest.writeFloat(this.price);
        dest.writeString(this.card_no);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeFloat(this.total_account);
        dest.writeFloat(this.total_times);
        dest.writeByte(this.is_auto_start ? (byte) 1 : (byte) 0);
        dest.writeByte(this.expired ? (byte) 1 : (byte) 0);
        dest.writeValue(this.trial_days);
        dest.writeString(this.lock_start);
        dest.writeString(this.lock_end);
        dest.writeTypedList(this.shops);
        dest.writeByte(this.is_open_service_term ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.card_tpl_service_term, flags);
    }

    protected Card(Parcel in) {
        this.system_id = in.readInt();
        this.type_name = in.readString();
        this.balance = in.readFloat();
        this.users = in.createTypedArrayList(Student.CREATOR);
        this.card_tpl = in.readParcelable(CardTpl.class.getClassLoader());
        this.bundleUsers = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.check_valid = in.readByte() != 0;
        this.valid_from = in.readString();
        this.valid_to = in.readString();
        this.type = in.readInt();
        this.brand_id = in.readString();
        this.is_locked = in.readByte() != 0;
        this.is_active = in.readByte() != 0;
        this.supportIds = in.readString();
        this.id = in.readString();
        this.color = in.readString();
        this.card_tpl_id = in.readString();
        this.total_cost = in.readFloat();
        this.price = in.readFloat();
        this.card_no = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.total_account = in.readFloat();
        this.total_times = in.readFloat();
        this.is_auto_start = in.readByte() != 0;
        this.expired = in.readByte() != 0;
        this.trial_days = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lock_start = in.readString();
        this.lock_end = in.readString();
        this.shops = in.createTypedArrayList(QcScheduleBean.Shop.CREATOR);
        this.is_open_service_term = in.readByte() != 0;
        this.card_tpl_service_term =
            in.readParcelable(CardProtocol.class.getClassLoader());
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
