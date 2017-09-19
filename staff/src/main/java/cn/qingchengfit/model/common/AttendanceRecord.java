package cn.qingchengfit.model.common;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;

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
 * Created by Paper on 2017/3/8.
 *
 * 出勤记录
 */

public class AttendanceRecord implements Parcelable {

    public Shop shop;
    public Course course;
    public User_Student teacher;
    public String start;
    public String end;
    public String url;
    public String id;
    public int type;
    public boolean checked_in;

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shop, flags);
        dest.writeParcelable(this.course, flags);
        dest.writeParcelable(this.teacher, flags);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.url);
        dest.writeString(this.id);
        dest.writeInt(this.type);
        dest.writeByte(this.checked_in ? (byte) 1 : (byte) 0);
    }

    public AttendanceRecord() {
    }

    protected AttendanceRecord(Parcel in) {
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.course = in.readParcelable(Course.class.getClassLoader());
        this.teacher = in.readParcelable(User_Student.class.getClassLoader());
        this.start = in.readString();
        this.end = in.readString();
        this.url = in.readString();
        this.id = in.readString();
        this.type = in.readInt();
        this.checked_in = in.readByte() != 0;
    }

    public static final Creator<AttendanceRecord> CREATOR = new Creator<AttendanceRecord>() {
        @Override public AttendanceRecord createFromParcel(Parcel source) {
            return new AttendanceRecord(source);
        }

        @Override public AttendanceRecord[] newArray(int size) {
            return new AttendanceRecord[size];
        }
    };
}
