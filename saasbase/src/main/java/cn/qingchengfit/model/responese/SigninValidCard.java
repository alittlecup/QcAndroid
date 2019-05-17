package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by yangming on 16/9/18.
 */
public class SigninValidCard {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * check_valid : true
         * account : 76
         * valid_from : 2016-09-02T00:00:00
         * end : 2016-08-13T10:51:27
         * name : 青橙储值卡
         * is_locked : false
         * valid_to : 2016-12-01T00:00:00
         * times : 0
         * start : 2016-08-13T10:51:27
         * is_enable : true
         * card_tpl_id : 206
         * lock_start : 2016-08-13T10:51:27
         * lock_end : 2016-08-13T10:51:27
         * balance : 76
         * users : [{"name":"学民","gender":0,"sellers":"志恒","phone":"18614029017","avatar":"https://img.qingchengfit.cn/ea39d72f2da36d972047c1ee26af31cd.jpg!120x120","id":3531}]
         * modify_at : 2016-09-18T11:24:31
         * type : 1
         * id : 2435
         * is_auto_start : false
         * card_no :
         */

        private List<CardsBean> cards;

        public List<CardsBean> getCards() {
            return cards;
        }

        public void setCards(List<CardsBean> cards) {
            this.cards = cards;
        }

        public static class CardsBean implements Parcelable {
            public static final Creator<CardsBean> CREATOR = new Creator<CardsBean>() {
                @Override public CardsBean createFromParcel(Parcel in) {
                    return new CardsBean(in);
                }

                @Override public CardsBean[] newArray(int size) {
                    return new CardsBean[size];
                }
            };
            private boolean check_valid;
            private float account;
            private String valid_from;
            private String end;
            private String name;
            private boolean is_locked;
            private String valid_to;
            private int times;
            private String start;
            private boolean is_enable;
            private int card_tpl_id;
            private String lock_start;
            private String lock_end;
            private float balance;
            private String modify_at;
            private int type;
            private int id;
            private boolean is_auto_start;
            private String card_no;
            /**
             * name : 学民
             * gender : 0
             * sellers : 志恒
             * phone : 18614029017
             * avatar : https://img.qingchengfit.cn/ea39d72f2da36d972047c1ee26af31cd.jpg!120x120
             * id : 3531
             */

            private List<UsersBean> users;

            protected CardsBean(Parcel in) {
                check_valid = in.readByte() != 0;
                account = in.readFloat();
                valid_from = in.readString();
                end = in.readString();
                name = in.readString();
                is_locked = in.readByte() != 0;
                valid_to = in.readString();
                times = in.readInt();
                start = in.readString();
                is_enable = in.readByte() != 0;
                card_tpl_id = in.readInt();
                lock_start = in.readString();
                lock_end = in.readString();
                balance = in.readInt();
                modify_at = in.readString();
                type = in.readInt();
                id = in.readInt();
                is_auto_start = in.readByte() != 0;
                card_no = in.readString();
            }

            public boolean isCheck_valid() {
                return check_valid;
            }

            public void setCheck_valid(boolean check_valid) {
                this.check_valid = check_valid;
            }

            public float getAccount() {
                return account;
            }

            public void setAccount(float account) {
                this.account = account;
            }

            public String getValid_from() {
                return valid_from;
            }

            public void setValid_from(String valid_from) {
                this.valid_from = valid_from;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isIs_locked() {
                return is_locked;
            }

            public void setIs_locked(boolean is_locked) {
                this.is_locked = is_locked;
            }

            public String getValid_to() {
                return valid_to;
            }

            public void setValid_to(String valid_to) {
                this.valid_to = valid_to;
            }

            public int getTimes() {
                return times;
            }

            public void setTimes(int times) {
                this.times = times;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

            public boolean isIs_enable() {
                return is_enable;
            }

            public void setIs_enable(boolean is_enable) {
                this.is_enable = is_enable;
            }

            public int getCard_tpl_id() {
                return card_tpl_id;
            }

            public void setCard_tpl_id(int card_tpl_id) {
                this.card_tpl_id = card_tpl_id;
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

            public float getBalance() {
                return balance;
            }

            public void setBalance(int balance) {
                this.balance = balance;
            }

            public String getModify_at() {
                return modify_at;
            }

            public void setModify_at(String modify_at) {
                this.modify_at = modify_at;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public boolean isIs_auto_start() {
                return is_auto_start;
            }

            public void setIs_auto_start(boolean is_auto_start) {
                this.is_auto_start = is_auto_start;
            }

            public String getCard_no() {
                return card_no;
            }

            public void setCard_no(String card_no) {
                this.card_no = card_no;
            }

            public List<UsersBean> getUsers() {
                return users;
            }

            public void setUsers(List<UsersBean> users) {
                this.users = users;
            }

            @Override public int describeContents() {
                return 0;
            }

            @Override public void writeToParcel(Parcel parcel, int i) {
                parcel.writeByte((byte) (check_valid ? 1 : 0));
                parcel.writeFloat(account);
                parcel.writeString(valid_from);
                parcel.writeString(end);
                parcel.writeString(name);
                parcel.writeByte((byte) (is_locked ? 1 : 0));
                parcel.writeString(valid_to);
                parcel.writeInt(times);
                parcel.writeString(start);
                parcel.writeByte((byte) (is_enable ? 1 : 0));
                parcel.writeInt(card_tpl_id);
                parcel.writeString(lock_start);
                parcel.writeString(lock_end);
                parcel.writeFloat(balance);
                parcel.writeString(modify_at);
                parcel.writeInt(type);
                parcel.writeInt(id);
                parcel.writeByte((byte) (is_auto_start ? 1 : 0));
                parcel.writeString(card_no);
            }

            public static class UsersBean {
                private String name;
                private int gender;
                private String sellers;
                private String phone;
                private String avatar;
                private int id;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getGender() {
                    return gender;
                }

                public void setGender(int gender) {
                    this.gender = gender;
                }

                public String getSellers() {
                    return sellers;
                }

                public void setSellers(String sellers) {
                    this.sellers = sellers;
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

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }
            }
        }
    }
}
