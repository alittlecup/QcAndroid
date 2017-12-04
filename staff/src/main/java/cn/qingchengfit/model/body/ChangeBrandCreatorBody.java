package cn.qingchengfit.model.body;

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
 * Created by Paper on 16/7/14.
 */
public class ChangeBrandCreatorBody implements Parcelable{
    public String phone;
    public String username;
    public int gender;
    public String code;
    public String area_code;

    private ChangeBrandCreatorBody(Builder builder) {
        phone = builder.phone;
        username = builder.username;
        gender = builder.gender;
        code = builder.code;
        area_code = builder.area_code;
    }



    public static final class Builder {
        private String phone;
        private String username;
        private int gender;
        private String code;
        private String area_code;

        public Builder() {
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder gender(int val) {
            gender = val;
            return this;
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder area_code(String val) {
            area_code = val;
            return this;
        }

        public ChangeBrandCreatorBody build() {
            return new ChangeBrandCreatorBody(this);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone);
        dest.writeString(this.username);
        dest.writeInt(this.gender);
        dest.writeString(this.code);
        dest.writeString(this.area_code);
    }

    protected ChangeBrandCreatorBody(Parcel in) {
        this.phone = in.readString();
        this.username = in.readString();
        this.gender = in.readInt();
        this.code = in.readString();
        this.area_code = in.readString();
    }

    public static final Creator<ChangeBrandCreatorBody> CREATOR =
        new Creator<ChangeBrandCreatorBody>() {
            @Override public ChangeBrandCreatorBody createFromParcel(Parcel source) {
                return new ChangeBrandCreatorBody(source);
            }

            @Override public ChangeBrandCreatorBody[] newArray(int size) {
                return new ChangeBrandCreatorBody[size];
            }
        };
}
