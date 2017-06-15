package cn.qingchengfit.model.responese;

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
 * Created by Paper on 16/8/29.
 */
public class Locker implements Parcelable {

    public static final Creator<Locker> CREATOR = new Creator<Locker>() {
        @Override public Locker createFromParcel(Parcel source) {
            return new Locker(source);
        }

        @Override public Locker[] newArray(int size) {
            return new Locker[size];
        }
    };
    public String name;
    public int gender;
    public Long shop_id;
    public boolean is_used;
    public Long id;
    public String start;
    public String end;
    public boolean is_long_term_borrow;
    public LockerRegion region;
    public User_Student user;

    public Locker() {
    }

    protected Locker(Parcel in) {

        this.name = in.readString();
        this.gender = in.readInt();
        this.shop_id = in.readLong();
        this.is_used = in.readInt() != 0;
        this.id = in.readLong();
        this.start = in.readString();
        this.end = in.readString();
        this.is_long_term_borrow = in.readInt() != 0;
        this.region = in.readParcelable(LockerRegion.class.getClassLoader());
        this.user = in.readParcelable(User_Student.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeInt(this.gender);
        dest.writeLong(this.shop_id);
        dest.writeInt(this.is_used ? 1 : 0);
        dest.writeLong(this.id);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeInt(this.is_long_term_borrow ? 1 : 0);

        dest.writeParcelable(this.region, flags);
        dest.writeParcelable(this.user, flags);
    }
}
