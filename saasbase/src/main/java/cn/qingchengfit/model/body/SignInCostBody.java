package cn.qingchengfit.model.body;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by yangming on 16/9/2.
 */
public class SignInCostBody {

    private String shop_id;
    private List<CardCost> card_costs;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public List<CardCost> getCard_costs() {
        return card_costs;
    }

    public void setCard_costs(List<CardCost> card_costs) {
        this.card_costs = card_costs;
    }

    public static class CardCost implements Parcelable {
        public static final Parcelable.Creator<CardCost> CREATOR = new Parcelable.Creator<CardCost>() {
            @Override public CardCost createFromParcel(Parcel source) {
                return new CardCost(source);
            }

            @Override public CardCost[] newArray(int size) {
                return new CardCost[size];
            }
        };
        private String card_tpl_id;
        private int cost;

        public CardCost(String card_tpl_id, int cost) {
            this.card_tpl_id = card_tpl_id;
            this.cost = cost;
        }

        protected CardCost(Parcel in) {
            this.card_tpl_id = in.readString();
            this.cost = in.readInt();
        }

        public String getCard_tpl_id() {
            return card_tpl_id;
        }

        public void setCard_tpl_id(String card_tpl_id) {
            this.card_tpl_id = card_tpl_id;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.card_tpl_id);
            dest.writeInt(this.cost);
        }
    }
}
