package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import java.util.ArrayList;
import java.util.List;

public class SellerStat implements Parcelable {
  private int total_count;
  private List<Integer>user_ids;
  private int inactive_count;
  private Staff seller;

  public int getTotal_count() {
    return total_count;
  }

  public void setTotal_count(int total_count) {
    this.total_count = total_count;
  }

  public List<Integer> getUser_ids() {
    return user_ids;
  }

  public void setUser_ids(List<Integer> user_ids) {
    this.user_ids = user_ids;
  }

  public int getInactive_count() {
    return inactive_count;
  }

  public void setInactive_count(int inactive_count) {
    this.inactive_count = inactive_count;
  }

  public Staff getSeller() {
    return seller;
  }

  public void setSeller(Staff seller) {
    this.seller = seller;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.total_count);
    dest.writeList(this.user_ids);
    dest.writeInt(this.inactive_count);
    dest.writeParcelable(this.seller, flags);
  }

  public SellerStat() {
  }

  protected SellerStat(Parcel in) {
    this.total_count = in.readInt();
    this.user_ids = new ArrayList<Integer>();
    in.readList(this.user_ids, Integer.class.getClassLoader());
    this.inactive_count = in.readInt();
    this.seller = in.readParcelable(Staff.class.getClassLoader());
  }

  public static final Parcelable.Creator<SellerStat> CREATOR =
      new Parcelable.Creator<SellerStat>() {
        @Override public SellerStat createFromParcel(Parcel source) {
          return new SellerStat(source);
        }

        @Override public SellerStat[] newArray(int size) {
          return new SellerStat[size];
        }
      };
}
