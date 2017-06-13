package com.qingchengfit.fitcoach.fragment.statement.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcResponse;
import com.google.gson.annotations.SerializedName;
import com.qingchengfit.fitcoach.bean.CourseTypeSample;
import com.qingchengfit.fitcoach.bean.StudentBean;
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
 * Created by Paper on 15/10/13 2015.
 */

public class QcResponseStatementDetail extends QcResponse {

    @SerializedName("data") public DetailData data;

    public static QcResponseStatementDetail newInstance() {
        QcResponseStatementDetail d = new QcResponseStatementDetail();
        d.setStatus(500);
        d.setMsg("server:network failed or server down");
        return d;
    }

    public static class DetailData {
        @SerializedName("start") public String start;
        @SerializedName("end") public String end;
        @SerializedName("user_id") public String user_id;
        @SerializedName("schedules") public List<StatamentSchedule> schedules;
    }

    //
    //
    //    public static class CardGlance implements Parcelable {
    //        @SerializedName("card_type")
    //        public int card_type;
    //        @SerializedName("count")
    //        public int count;
    //        @SerializedName("price")
    //        public float price;
    //        @SerializedName("account")
    //        public float account;
    //
    //        @Override
    //        public int describeContents() {
    //            return 0;
    //        }
    //
    //        @Override
    //        public void writeToParcel(Parcel dest, int flags) {
    //            dest.writeInt(this.card_type);
    //            dest.writeInt(this.count);
    //            dest.writeFloat(this.price);
    //            dest.writeFloat(this.account);
    //        }
    //
    //        public CardGlance() {
    //        }
    //
    //        protected CardGlance(Parcel in) {
    //            this.card_type = in.readInt();
    //            this.count = in.readInt();
    //            this.price = in.readFloat();
    //            this.account = in.readFloat();
    //        }
    //
    //        public static final Parcelable.Creator<CardGlance> CREATOR = new Parcelable.Creator<CardGlance>() {
    //            @Override
    //            public CardGlance createFromParcel(Parcel source) {
    //                return new CardGlance(source);
    //            }
    //
    //            @Override
    //            public CardGlance[] newArray(int size) {
    //                return new CardGlance[size];
    //            }
    //        };
    //    }
    //
    //
    //  public static class TypeGlance implements Parcelable {
    //        @SerializedName("type")
    //        public int card_type;
    //        @SerializedName("count")
    //        public int count;
    //        @SerializedName("serve_count")
    //        public float serve_count;
    //
    //      @Override
    //      public int describeContents() {
    //          return 0;
    //      }
    //
    //      @Override
    //      public void writeToParcel(Parcel dest, int flags) {
    //          dest.writeInt(this.card_type);
    //          dest.writeInt(this.count);
    //          dest.writeFloat(this.serve_count);
    //      }
    //
    //      public TypeGlance() {
    //      }
    //
    //      protected TypeGlance(Parcel in) {
    //          this.card_type = in.readInt();
    //          this.count = in.readInt();
    //          this.serve_count = in.readFloat();
    //      }
    //
    //      public static final Creator<TypeGlance> CREATOR = new Creator<TypeGlance>() {
    //          @Override
    //          public TypeGlance createFromParcel(Parcel source) {
    //              return new TypeGlance(source);
    //          }
    //
    //          @Override
    //          public TypeGlance[] newArray(int size) {
    //              return new TypeGlance[size];
    //          }
    //      };
    //  }
    //

    public static class StatamentSchedule {
        @SerializedName("id") public String id;
        @SerializedName("start") public String start;
        @SerializedName("end") public String end;
        @SerializedName("course") public CourseTypeSample course;
        @SerializedName("teacher") public Staff teacher;
        @SerializedName("orders") public List<Order> orders;
        @SerializedName("total_account") public float total_account;
        @SerializedName("total_times") public float total_times;
        @SerializedName("shop") public Shop shop;
        public float total_real_price;

        public List<String> getUsersIds() {
            List<String> ret = new ArrayList<>();
            if (orders != null) {
                for (int i = 0; i < orders.size(); i++) {
                    ret.add(orders.get(i).user.id);
                }
            }
            return ret;
        }
    }

    public static class Order implements Parcelable {
        public static final Creator<Order> CREATOR = new Creator<Order>() {
            @Override public Order createFromParcel(Parcel source) {
                return new Order(source);
            }

            @Override public Order[] newArray(int size) {
                return new Order[size];
            }
        };
        @SerializedName("user") public StudentBean user;
        @SerializedName("card") public StatementCard card;
        @SerializedName("count") public int count;
        @SerializedName("total_real_price") public float total_real_price;
        @SerializedName("channel") public String channel;
        public float total_price;

        public Order() {
        }

        protected Order(Parcel in) {
            this.user = in.readParcelable(StudentBean.class.getClassLoader());
            this.card = in.readParcelable(StatementCard.class.getClassLoader());
            this.count = in.readInt();
            this.total_real_price = in.readFloat();
            this.channel = in.readString();
            this.total_price = in.readFloat();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.user, flags);
            dest.writeParcelable(this.card, flags);
            dest.writeInt(this.count);
            dest.writeFloat(this.total_real_price);
            dest.writeString(this.channel);
            dest.writeFloat(this.total_price);
        }
    }

    public static class StatementCard implements Parcelable {
        public static final Parcelable.Creator<StatementCard> CREATOR = new Parcelable.Creator<StatementCard>() {
            @Override public StatementCard createFromParcel(Parcel source) {
                return new StatementCard(source);
            }

            @Override public StatementCard[] newArray(int size) {
                return new StatementCard[size];
            }
        };
        @SerializedName("card_type") public int card_type;
        @SerializedName("card_name") public String card_name;

        public StatementCard() {
        }

        protected StatementCard(Parcel in) {
            this.card_type = in.readInt();
            this.card_name = in.readString();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.card_type);
            dest.writeString(this.card_name);
        }
    }
}
