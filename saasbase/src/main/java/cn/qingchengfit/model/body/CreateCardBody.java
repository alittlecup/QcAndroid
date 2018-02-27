package cn.qingchengfit.model.body;

import android.os.Parcel;
import android.os.Parcelable;

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
 * Created by Paper on 16/4/28 2016.
 */
public class CreateCardBody implements Parcelable {
    public static final Creator<CreateCardBody> CREATOR = new Creator<CreateCardBody>() {
        @Override public CreateCardBody createFromParcel(Parcel source) {
            return new CreateCardBody(source);
        }

        @Override public CreateCardBody[] newArray(int size) {
            return new CreateCardBody[size];
        }
    };
    public String price;
    public int charge_type; //支付类型
    public String shop_id;
    public String card_no;
    public String account; //储值卡充值
    public String seller_id;
    public String times; //次卡充值
    public String start;
    public String end;
    public boolean check_valid;//是否设置有效期
    public String valid_from;
    public String valid_to;
    public String option_id;
    public String remarks;
    public String card_tpl_id;
    public String user_ids;
    public String user_name;
    public boolean is_auto_start;//是否自动开卡
    public String app_id;

    public CreateCardBody() {
    }

    private CreateCardBody(Builder builder) {
        price = builder.price;
        charge_type = builder.charge_type;
        shop_id = builder.shop_id;
        card_no = builder.card_no;
        account = builder.account;
        seller_id = builder.seller_id;
        times = builder.times;
        start = builder.start;
        end = builder.end;
        check_valid = builder.check_valid;
        valid_from = builder.valid_from;
        valid_to = builder.valid_to;
        option_id = builder.option_id;
        remarks = builder.remarks;
        card_tpl_id = builder.card_tpl_id;
        user_ids = builder.user_ids;
        user_name = builder.user_name;
        is_auto_start = builder.is_auto_start;
        app_id = builder.app_id;
    }

    protected CreateCardBody(Parcel in) {
        this.price = in.readString();
        this.charge_type = in.readInt();
        this.shop_id = in.readString();
        this.card_no = in.readString();
        this.account = in.readString();
        this.seller_id = in.readString();
        this.times = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.check_valid = in.readByte() != 0;
        this.valid_from = in.readString();
        this.valid_to = in.readString();
        this.option_id = in.readString();
        this.remarks = in.readString();
        this.card_tpl_id = in.readString();
        this.user_ids = in.readString();
        this.user_name = in.readString();
        this.is_auto_start = in.readByte() != 0;
        this.app_id = in.readString();
    }

    @Override protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public CreateCardBody cloneSelf() {
        try {
            return (CreateCardBody) this.clone();
        } catch (CloneNotSupportedException e) {
            CreateCardBody ret = new CreateCardBody();

            return ret;
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeInt(this.charge_type);
        dest.writeString(this.shop_id);
        dest.writeString(this.card_no);
        dest.writeString(this.account);
        dest.writeString(this.seller_id);
        dest.writeString(this.times);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeByte(this.check_valid ? (byte) 1 : (byte) 0);
        dest.writeString(this.valid_from);
        dest.writeString(this.valid_to);
        dest.writeString(this.option_id);
        dest.writeString(this.remarks);
        dest.writeString(this.card_tpl_id);
        dest.writeString(this.user_ids);
        dest.writeString(this.user_name);
        dest.writeByte(this.is_auto_start ? (byte) 1 : (byte) 0);
        dest.writeString(this.app_id);
    }

    public static final class Builder {
        private String price;
        private int charge_type;
        private String shop_id;
        private String card_no;
        private String account;
        private String seller_id;
        private String times;
        private String start;
        private String end;
        private boolean check_valid;
        private String valid_from;
        private String valid_to;
        private String option_id;
        private String remarks;
        private String card_tpl_id;
        private String user_ids;
        private String user_name;
        private boolean is_auto_start;
        private String app_id;

        public Builder() {
        }

        public Builder price(String val) {
            price = val;
            return this;
        }

        public Builder charge_type(int val) {
            charge_type = val;
            return this;
        }

        public Builder shop_id(String val) {
            shop_id = val;
            return this;
        }

        public Builder card_no(String val) {
            card_no = val;
            return this;
        }

        public Builder account(String val) {
            account = val;
            return this;
        }

        public Builder seller_id(String val) {
            seller_id = val;
            return this;
        }

        public Builder times(String val) {
            times = val;
            return this;
        }

        public Builder start(String val) {
            start = val;
            return this;
        }

        public Builder end(String val) {
            end = val;
            return this;
        }

        public Builder check_valid(boolean val) {
            check_valid = val;
            return this;
        }

        public Builder valid_from(String val) {
            valid_from = val;
            return this;
        }

        public Builder valid_to(String val) {
            valid_to = val;
            return this;
        }

        public Builder option_id(String val) {
            option_id = val;
            return this;
        }

        public Builder remarks(String val) {
            remarks = val;
            return this;
        }

        public Builder card_tpl_id(String val) {
            card_tpl_id = val;
            return this;
        }

        public Builder user_ids(String val) {
            user_ids = val;
            return this;
        }

        public Builder user_name(String val) {
            user_name = val;
            return this;
        }

        public Builder is_auto_start(boolean val) {
            is_auto_start = val;
            return this;
        }

        public Builder app_id(String val) {
            app_id = val;
            return this;
        }

        public CreateCardBody build() {
            return new CreateCardBody(this);
        }
    }
}
