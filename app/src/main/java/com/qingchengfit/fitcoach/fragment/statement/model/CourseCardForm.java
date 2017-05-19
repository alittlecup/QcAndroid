package com.qingchengfit.fitcoach.fragment.statement.model;

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
 * Created by Paper on 16/7/8.
 */
public class CourseCardForm implements Parcelable {

    public CourseCardForm(int card_type, int server_count, float course_income, float real_income) {
        this.card_type = card_type;
        this.server_count = server_count;
        this.course_income = course_income;
        this.real_income = real_income;
    }

    public int card_type;
    public int server_count;
    public float course_income;
    public float real_income;

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.card_type);
        dest.writeInt(this.server_count);
        dest.writeFloat(this.course_income);
        dest.writeFloat(this.real_income);
    }

    protected CourseCardForm(Parcel in) {
        this.card_type = in.readInt();
        this.server_count = in.readInt();
        this.course_income = in.readFloat();
        this.real_income = in.readFloat();
    }

    public static final Parcelable.Creator<CourseCardForm> CREATOR = new Parcelable.Creator<CourseCardForm>() {
        @Override public CourseCardForm createFromParcel(Parcel source) {
            return new CourseCardForm(source);
        }

        @Override public CourseCardForm[] newArray(int size) {
            return new CourseCardForm[size];
        }
    };
}
