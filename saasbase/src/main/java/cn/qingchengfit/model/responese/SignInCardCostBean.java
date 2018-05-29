package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class SignInCardCostBean {

    @SerializedName("data") public Data data;

    public static class CardCost implements Parcelable {


        @SerializedName("selected") private boolean selected;


        @SerializedName("cost") private float cost;
        @SerializedName("type") private int type;
        @SerializedName("id") private Integer id;
        @SerializedName("name") private String name;

        public CardCost() {
        }


        @Override public boolean equals(Object obj) {
            if (obj instanceof CardCost) {
                return ((CardCost) obj).getId().equals(getId());
            } else {
                return false;
            }
        }

        @Override public int hashCode() {
            return getId().hashCode();
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public float getCost() {
            return cost;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The id
         */
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
            dest.writeFloat(this.cost);
            dest.writeInt(this.type);
            dest.writeValue(this.id);
            dest.writeString(this.name);
        }

        protected CardCost(Parcel in) {
            this.selected = in.readByte() != 0;
            this.cost = in.readFloat();
            this.type = in.readInt();
            this.id = (Integer) in.readValue(Integer.class.getClassLoader());
            this.name = in.readString();
        }

        public static final Parcelable.Creator<CardCost> CREATOR =
            new Parcelable.Creator<CardCost>() {
                @Override public CardCost createFromParcel(Parcel source) {
                    return new CardCost(source);
                }

                @Override public CardCost[] newArray(int size) {
                    return new CardCost[size];
                }
            };
    }

    public class Data {
        @SerializedName("card_costs") public List<CardCost> card_costs;
    }
}
