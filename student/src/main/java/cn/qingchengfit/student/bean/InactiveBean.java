package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InactiveBean implements Parcelable {
  private int count;
  @SerializedName("time_period")
  private String period;

  @SerializedName("time_period_id")
  private int id;

  private List<SellerStat> seller_stat;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<SellerStat> getSeller_stat() {
    return seller_stat;
  }

  public void setSeller_stat(List<SellerStat> seller_stat) {
    this.seller_stat = seller_stat;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.count);
    dest.writeString(this.period);
    dest.writeInt(this.id);
    dest.writeTypedList(this.seller_stat);
  }

  public InactiveBean() {
  }

  protected InactiveBean(Parcel in) {
    this.count = in.readInt();
    this.period = in.readString();
    this.id = in.readInt();
    this.seller_stat = in.createTypedArrayList(SellerStat.CREATOR);
  }

  public static final Parcelable.Creator<InactiveBean> CREATOR =
      new Parcelable.Creator<InactiveBean>() {
        @Override public InactiveBean createFromParcel(Parcel source) {
          return new InactiveBean(source);
        }

        @Override public InactiveBean[] newArray(int size) {
          return new InactiveBean[size];
        }
      };
}
