package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by peggy on 16/6/3.
 */

public class GymDetail {

  @SerializedName("hint_url") public String hint_url;
  @SerializedName("welcome_url") public String welcome_url;
  @SerializedName("gym") public HomeDetailGym gym;
  @SerializedName("stat") public HomeStatement stat;
  @SerializedName("banners") public List<Banner> banners;
  @SerializedName("recharge") public Recharge recharge;
  @SerializedName("qingcheng_activity_count") public int qingcheng_activity_count;
  public boolean has_team;
  public boolean has_private;

  public Staff superuser;

  public static class Recharge implements Parcelable {
    public static final Creator<Recharge> CREATOR = new Creator<Recharge>() {
      @Override public Recharge createFromParcel(Parcel source) {
        return new Recharge(source);
      }

      @Override public Recharge[] newArray(int size) {
        return new Recharge[size];
      }
    };
    @SerializedName("recharge_price") public String recharge_price;
    @SerializedName("is_recharged") public boolean is_recharged;
    @SerializedName("is_first_shop") public boolean is_first_shop;
    public List<ChargeRule> charge_rule;
    public int trial_date;
    public String system_end;

    public Recharge() {
    }

    protected Recharge(Parcel in) {
      this.recharge_price = in.readString();
      this.is_recharged = in.readByte() != 0;
      this.is_first_shop = in.readByte() != 0;
      this.charge_rule = in.createTypedArrayList(ChargeRule.CREATOR);
      this.trial_date = in.readInt();
      this.system_end = in.readString();
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.recharge_price);
      dest.writeByte(this.is_recharged ? (byte) 1 : (byte) 0);
      dest.writeByte(this.is_first_shop ? (byte) 1 : (byte) 0);
      dest.writeTypedList(this.charge_rule);
      dest.writeInt(this.trial_date);
      dest.writeString(this.system_end);
    }
  }

  public static class ChargeRule implements Parcelable {
    public static final Parcelable.Creator<ChargeRule> CREATOR =
        new Parcelable.Creator<ChargeRule>() {
          @Override public ChargeRule createFromParcel(Parcel source) {
            return new ChargeRule(source);
          }

          @Override public ChargeRule[] newArray(int size) {
            return new ChargeRule[size];
          }
        };
    public String key;
    public String value;

    public ChargeRule() {
    }

    protected ChargeRule(Parcel in) {
      this.key = in.readString();
      this.value = in.readString();
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.key);
      dest.writeString(this.value);
    }
  }
}
