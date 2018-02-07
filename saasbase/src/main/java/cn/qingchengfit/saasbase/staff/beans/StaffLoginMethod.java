package cn.qingchengfit.saasbase.staff.beans;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.utils.CmStringUtils;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/12/30.
 */

public class StaffLoginMethod implements Parcelable {
  public String phone;
  public boolean phone_active;
  public String wx;
  public boolean wx_active;

  private StaffLoginMethod(Builder builder) {
    if (!CmStringUtils.isEmpty(builder.phone) && builder.phone.length() >= 7) {
      phone = builder.phone.substring(0,3)+"****"+builder.phone.substring(6);
    }
    phone_active = builder.phone_active;
    wx = builder.wx == null?"":builder.wx;
    wx_active = builder.wx_active;
  }

  public static final class Builder {
    private String phone;
    private boolean phone_active;
    private String wx;
    private boolean wx_active;

    public Builder() {
    }

    public Builder phone(String val) {
      phone = val;
      return this;
    }

    public Builder phone_active(boolean val) {
      phone_active = val;
      return this;
    }

    public Builder wx(String val) {
      wx = val;
      return this;
    }

    public Builder wx_active(boolean val) {
      wx_active = val;
      return this;
    }

    public StaffLoginMethod build() {
      return new StaffLoginMethod(this);
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.phone);
    dest.writeByte(this.phone_active ? (byte) 1 : (byte) 0);
    dest.writeString(this.wx);
    dest.writeByte(this.wx_active ? (byte) 1 : (byte) 0);
  }

  protected StaffLoginMethod(Parcel in) {
    this.phone = in.readString();
    this.phone_active = in.readByte() != 0;
    this.wx = in.readString();
    this.wx_active = in.readByte() != 0;
  }

  public static final Parcelable.Creator<StaffLoginMethod> CREATOR =
    new Parcelable.Creator<StaffLoginMethod>() {
      @Override public StaffLoginMethod createFromParcel(Parcel source) {
        return new StaffLoginMethod(source);
      }

      @Override public StaffLoginMethod[] newArray(int size) {
        return new StaffLoginMethod[size];
      }
    };
}
