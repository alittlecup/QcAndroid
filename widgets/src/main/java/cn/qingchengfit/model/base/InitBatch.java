package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
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
 * Created by Paper on 16/11/15.
 */

public class InitBatch implements Parcelable {

    public static final Creator<InitBatch> CREATOR = new Creator<InitBatch>() {
        @Override public InitBatch createFromParcel(Parcel source) {
            return new InitBatch(source);
        }

        @Override public InitBatch[] newArray(int size) {
            return new InitBatch[size];
        }
    };
    public String course_name;
    public int capacity;
    public String from_date;
    public String to_date;
    public String teacher_phone;
    public String teacher_code_area;
    public String space_name;
    public List<TimeRepeat> time_repeats;

    private InitBatch(Builder builder) {
        course_name = builder.course_name;
        capacity = builder.capacity;
        from_date = builder.from_date;
        to_date = builder.to_date;
        teacher_phone = builder.teacher_phone;
        teacher_code_area = builder.teacher_code_area;
        space_name = builder.space_name;
        time_repeats = builder.time_repeats;
    }

    public InitBatch() {
    }

    protected InitBatch(Parcel in) {
        this.course_name = in.readString();
        this.capacity = in.readInt();
        this.from_date = in.readString();
        this.to_date = in.readString();
        this.teacher_phone = in.readString();
        this.teacher_code_area = in.readString();
        this.space_name = in.readString();
        this.time_repeats = in.createTypedArrayList(TimeRepeat.CREATOR);
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.course_name);
        dest.writeInt(this.capacity);
        dest.writeString(this.from_date);
        dest.writeString(this.to_date);
        dest.writeString(this.teacher_phone);
        dest.writeString(this.teacher_code_area);
        dest.writeString(this.space_name);
        dest.writeTypedList(this.time_repeats);
    }

    public static final class Builder {
        private String course_name;
        private int capacity;
        private String from_date;
        private String to_date;
        private String teacher_phone;
        private String teacher_code_area;
        private String space_name;
        private List<TimeRepeat> time_repeats;

        public Builder() {
        }

        public Builder course_name(String val) {
            course_name = val;
            return this;
        }

        public Builder capacity(int val) {
            capacity = val;
            return this;
        }

        public Builder from_date(String val) {
            from_date = val;
            return this;
        }

        public Builder to_date(String val) {
            to_date = val;
            return this;
        }

        public Builder teacher_phone(String val) {
            teacher_phone = val;
            return this;
        }

        public Builder teacher_code_area(String val) {
            teacher_code_area = val;
            return this;
        }

        public Builder space_name(String val) {
            space_name = val;
            return this;
        }

        public Builder time_repeats(List<TimeRepeat> val) {
            time_repeats = val;
            return this;
        }

        public InitBatch build() {
            return new InitBatch(this);
        }
    }
}
