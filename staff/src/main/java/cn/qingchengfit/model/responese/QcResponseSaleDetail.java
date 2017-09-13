package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
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
 * <p/>
 * Created by Paper on 15/10/13 2015.
 */

public class QcResponseSaleDetail extends QcResponse {
    @SerializedName("data") public DetailData data;

    public static class DetailData {
        @SerializedName("total_cost") public float total_cost;
        @SerializedName("total_account") public float total_account;

        @SerializedName("histories") public List<History> histories;
    }

    public static class History {
        @SerializedName("created_at") public String created_at;
        @SerializedName("remarks") public String remarks;
        @SerializedName("card") public Card card;
        @SerializedName("cost") public float account;
        @SerializedName("account") public float cost;
        @SerializedName("charge_type") public int charge_type;
        @SerializedName("type") public int type;
        //        @SerializedName("card_tpl")
        //        public CardTpl card_tpl;
        @SerializedName("seller") public Staff saler;
        @SerializedName("users") public List<QcStudentBean> students;

        public List<String> getStudentIds() {
            List<String> ret = new ArrayList<>();
            if (students != null) {
                for (int i = 0; i < students.size(); i++) {
                    ret.add(students.get(i).getId());
                }
            }
            return ret;
        }
    }

    public static class Card implements Parcelable {
        public static final Creator<Card> CREATOR = new Creator<Card>() {
            @Override public Card createFromParcel(Parcel source) {
                return new Card(source);
            }

            @Override public Card[] newArray(int size) {
                return new Card[size];
            }
        };
        @SerializedName("name") public String name;
        @SerializedName("id") public String id;
        @SerializedName("card_tpl_id") public String card_tpl_id;
        @SerializedName("card_type") public int card_type;

        public Card() {
        }

        protected Card(Parcel in) {
            this.name = in.readString();
            this.id = in.readString();
            this.card_tpl_id = in.readString();
            this.card_type = in.readInt();
        }

        @Override public boolean equals(Object o) {
            if (o instanceof Card) {
                return ((Card) o).card_tpl_id.equals(this.card_tpl_id);
            } else {
                return super.equals(o);
            }
        }

        @Override public int hashCode() {
            //            if (card_tpl_id != null)
            return card_tpl_id.hashCode();
            //            return super.hashCode();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.id);
            dest.writeString(this.card_tpl_id);
            dest.writeInt(this.card_type);
        }
    }
}
