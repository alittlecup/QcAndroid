package cn.qingchengfit.saasbase.bill.beans;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.User;
import java.util.List;

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
 * Created by Paper on 2017/10/20.
 */

public class BillScheduleOrder implements Parcelable{

  public String course_name;
  public String teacher_name;
  public String start;
  public List<User> users;
  public String price;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.course_name);
    dest.writeString(this.teacher_name);
    dest.writeString(this.start);
    dest.writeTypedList(this.users);
    dest.writeString(this.price);
  }

  public BillScheduleOrder() {
  }

  protected BillScheduleOrder(Parcel in) {
    this.course_name = in.readString();
    this.teacher_name = in.readString();
    this.start = in.readString();
    this.users = in.createTypedArrayList(User.CREATOR);
    this.price = in.readString();
  }

  public static final Creator<BillScheduleOrder> CREATOR = new Creator<BillScheduleOrder>() {
    @Override public BillScheduleOrder createFromParcel(Parcel source) {
      return new BillScheduleOrder(source);
    }

    @Override public BillScheduleOrder[] newArray(int size) {
      return new BillScheduleOrder[size];
    }
  };
}
