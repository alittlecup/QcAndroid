package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class StatData implements Parcelable {
  private int total_count;
  private List<InactiveBean> stat_data;

  public int getTotal_count() {
    return total_count;
  }

  public void setTotal_count(int total_count) {
    this.total_count = total_count;
  }

  public List<InactiveBean> getStat_data() {
    return stat_data;
  }

  public void setStat_data(List<InactiveBean> stat_data) {
    this.stat_data = stat_data;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.total_count);
    dest.writeTypedList(this.stat_data);
  }

  public StatData() {
  }

  protected StatData(Parcel in) {
    this.total_count = in.readInt();
    this.stat_data = in.createTypedArrayList(InactiveBean.CREATOR);
  }

  public static final Parcelable.Creator<StatData> CREATOR = new Parcelable.Creator<StatData>() {
    @Override public StatData createFromParcel(Parcel source) {
      return new StatData(source);
    }

    @Override public StatData[] newArray(int size) {
      return new StatData[size];
    }
  };
}
