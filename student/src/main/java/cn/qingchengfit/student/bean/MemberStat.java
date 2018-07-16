package cn.qingchengfit.student.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MemberStat implements Parcelable {
  @SerializedName(value = "member_users_count",alternate = {"registered_users_count","following_users_count"})
  private int count;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<UnAttacked> getUnattacked() {
    return unattacked;
  }

  public void setUnattacked(List<UnAttacked> unattacked) {
    this.unattacked = unattacked;
  }

  @SerializedName(value = "unfollowed",alternate = {"unattacked"})
  private List<UnAttacked> unattacked;

  public  static class UnAttacked implements Parcelable {
    @SerializedName("time_period_id")
    private int id;
    @SerializedName("count")
    private int count;
    @SerializedName("time_period")
    private String desc;


    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.id);
      dest.writeInt(this.count);
      dest.writeString(this.desc);
    }

    public UnAttacked() {
    }

    protected UnAttacked(Parcel in) {
      this.id = in.readInt();
      this.count = in.readInt();
      this.desc = in.readString();
    }

    public static final Parcelable.Creator<UnAttacked> CREATOR =
        new Parcelable.Creator<UnAttacked>() {
          @Override public UnAttacked createFromParcel(Parcel source) {
            return new UnAttacked(source);
          }

          @Override public UnAttacked[] newArray(int size) {
            return new UnAttacked[size];
          }
        };
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.count);
    dest.writeTypedList(this.unattacked);
  }

  public MemberStat() {
  }

  protected MemberStat(Parcel in) {
    this.count = in.readInt();
    this.unattacked = in.createTypedArrayList(UnAttacked.CREATOR);
  }

  public static final Parcelable.Creator<MemberStat> CREATOR =
      new Parcelable.Creator<MemberStat>() {
        @Override public MemberStat createFromParcel(Parcel source) {
          return new MemberStat(source);
        }

        @Override public MemberStat[] newArray(int size) {
          return new MemberStat[size];
        }
      };
}
