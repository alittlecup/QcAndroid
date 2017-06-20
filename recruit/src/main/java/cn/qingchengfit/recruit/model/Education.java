package cn.qingchengfit.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

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
 * Created by Paper on 2017/6/13.
 */

public class Education implements Cloneable, Parcelable {
  public static final Parcelable.Creator<Education> CREATOR = new Parcelable.Creator<Education>() {
    @Override public Education createFromParcel(Parcel source) {
      return new Education(source);
    }

    @Override public Education[] newArray(int size) {
      return new Education[size];
    }
  };
  public String id;
  public String name;
  public String major;
  public String start;
  public String end;
  public int education;

  private Education(Builder builder) {
    id = builder.id;
    name = builder.name;
    major = builder.major;
    start = builder.start;
    end = builder.end;
    education = builder.education;
  }

  protected Education(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.major = in.readString();
    this.start = in.readString();
    this.end = in.readString();
    this.education = in.readInt();
  }

  public Education getPostBody() throws CloneNotSupportedException {
    Education e = (Education) this.clone();
    e.id = null;
    return e;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.major);
    dest.writeString(this.start);
    dest.writeString(this.end);
    dest.writeInt(this.education);
  }

  public static final class Builder {
    private String id;
    private String name;
    private String major;
    private String start;
    private String end;
    private int education;

    public Builder() {
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder major(String val) {
      major = val;
      return this;
    }

    public Builder start(String val) {
      start = val;
      return this;
    }

    public Builder end(String val) {
      end = val;
      return this;
    }

    public Builder education(int val) {
      education = val;
      return this;
    }

    public Education build() {
      return new Education(this);
    }
  }
}
