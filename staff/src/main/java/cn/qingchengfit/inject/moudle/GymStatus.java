package cn.qingchengfit.inject.moudle;

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
 * Created by Paper on 16/8/26.
 */
public class GymStatus implements Parcelable {
    public static final Creator<GymStatus> CREATOR = new Creator<GymStatus>() {
        @Override public GymStatus createFromParcel(Parcel source) {
            return new GymStatus(source);
        }

        @Override public GymStatus[] newArray(int size) {
            return new GymStatus[size];
        }
    };
    Boolean isSingle;
    Boolean isGuide;
    Boolean isSin;

    public GymStatus(Boolean isSingle) {
        this.isSingle = isSingle;
    }

    private GymStatus(Builder builder) {
        isSingle = builder.isSingle;
        isSin = builder.isSin;
        isGuide = builder.isGuide;
    }

    protected GymStatus(Parcel in) {
        this.isSingle = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isSin = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isGuide = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public Boolean getSingle() {
        return isSingle;
    }

    public void setSingle(Boolean single) {
        isSingle = single;
    }

    public Boolean getSin() {
        return isSin;
    }

    public void setSin(Boolean single) {
        isSin = single;
    }

    public Boolean getGuide() {
        return isGuide;
    }

    public void setGuide(Boolean guide) {
        isGuide = guide;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.isSingle);
        dest.writeValue(this.isSin);
        dest.writeValue(this.isGuide);
    }

    public static final class Builder {
        private Boolean isSingle;
        private Boolean isSin;
        private Boolean isGuide;

        public Builder() {
        }

        public Builder isSingle(Boolean val) {
            isSingle = val;
            return this;
        }

        public Builder isSin(Boolean val) {
            isSin = val;
            return this;
        }

        public Builder isGuide(Boolean val) {
            isGuide = val;
            return this;
        }

        public GymStatus build() {
            return new GymStatus(this);
        }
    }
}
