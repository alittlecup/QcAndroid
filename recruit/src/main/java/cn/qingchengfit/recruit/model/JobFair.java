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
 * Created by Paper on 2017/6/27.
 */

public class JobFair implements Parcelable {
  public static final Parcelable.Creator<JobFair> CREATOR = new Parcelable.Creator<JobFair>() {
    @Override public JobFair createFromParcel(Parcel source) {
      return new JobFair(source);
    }

    @Override public JobFair[] newArray(int size) {
      return new JobFair[size];
    }
  };
  public String banner;
  public String id;
  public String name;
  public String start;
  public String end;
  public boolean published;
  public String created_at;
  public Organization organization;
  public String created_by;
  public String description;
  public int status = -1;

  public JobFair() {
  }

  protected JobFair(Parcel in) {
    this.banner = in.readString();
    this.id = in.readString();
    this.name = in.readString();
    this.start = in.readString();
    this.end = in.readString();
    this.published = in.readByte() != 0;
    this.created_at = in.readString();
    this.organization = in.readParcelable(Organization.class.getClassLoader());
    this.created_by = in.readString();
    this.description = in.readString();
    this.status = in.readInt();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.banner);
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.start);
    dest.writeString(this.end);
    dest.writeByte(this.published ? (byte) 1 : (byte) 0);
    dest.writeString(this.created_at);
    dest.writeParcelable(this.organization, flags);
    dest.writeString(this.created_by);
    dest.writeString(this.description);
    dest.writeInt(this.status);
  }
}
