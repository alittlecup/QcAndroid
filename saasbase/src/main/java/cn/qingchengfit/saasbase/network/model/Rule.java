package cn.qingchengfit.saasbase.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

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
 * 排期支付的规则，卡种类对应支付金额
 */
public class Rule implements Parcelable {
    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override public Rule createFromParcel(Parcel source) {
            return new Rule(source);
        }

        @Override public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };
    @SerializedName("from_number") public int from_number;
    @SerializedName("to_number") public int to_number;
    @SerializedName("cost") public String cost;
    public String card_tpl_name;
    @SerializedName("card_tpl_id") public String card_tpl_id;
    public String channel;
    public OnlineLimit limits;

    public Rule() {
    }

    private Rule(Builder builder) {
        from_number = builder.from_number;
        to_number = builder.to_number;
        cost = builder.cost;
        card_tpl_name = builder.card_tpl_name;
        card_tpl_id = builder.card_tpl_id;
        channel = builder.channel;
        limits = builder.limits;
    }

    protected Rule(Parcel in) {
        this.from_number = in.readInt();
        this.to_number = in.readInt();
        this.cost = in.readString();
        this.card_tpl_name = in.readString();
        this.card_tpl_id = in.readString();
        this.channel = in.readString();
        this.limits = in.readParcelable(OnlineLimit.class.getClassLoader());
    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof Rule) {
            return ((Rule) obj).card_tpl_id.equalsIgnoreCase(this.card_tpl_id);
        } else {
            return false;
        }
    }

    @Override public int hashCode() {
        return card_tpl_id.hashCode();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.from_number);
        dest.writeInt(this.to_number);
        dest.writeString(this.cost);
        dest.writeString(this.card_tpl_name);
        dest.writeString(this.card_tpl_id);
        dest.writeString(this.channel);
        dest.writeParcelable(this.limits, flags);
    }

    public static final class Builder {
        private int from_number;
        private int to_number;
        private String cost;
        private String card_tpl_name;
        private String card_tpl_id;
        private String channel;
        private OnlineLimit limits;

        public Builder() {
        }

        public Builder from_number(int val) {
            from_number = val;
            return this;
        }

        public Builder to_number(int val) {
            to_number = val;
            return this;
        }

        public Builder cost(String val) {
            cost = val;
            return this;
        }

        public Builder card_tpl_name(String val) {
            card_tpl_name = val;
            return this;
        }

        public Builder card_tpl_id(String val) {
            card_tpl_id = val;
            return this;
        }

        public Builder channel(String val) {
            channel = val;
            return this;
        }

        public Builder limits(OnlineLimit val) {
            limits = val;
            return this;
        }

        public Rule build() {
            return new Rule(this);
        }
    }
}
