package cn.qingchengfit.inject.moudle;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;

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
 * Created by Paper on 16/8/28.
 */
public class QcContext implements Parcelable {
    public static final Parcelable.Creator<QcContext> CREATOR = new Parcelable.Creator<QcContext>() {
        @Override public QcContext createFromParcel(Parcel source) {
            return new QcContext(source);
        }

        @Override public QcContext[] newArray(int size) {
            return new QcContext[size];
        }
    };
    private CoachService coachService;
    private Brand brand;
    private GymStatus gymStatus;

    private QcContext(Builder builder) {
        coachService = builder.coachService;
        brand = builder.brand;
        gymStatus = builder.gymStatus;
    }

    protected QcContext(Parcel in) {
        this.coachService = in.readParcelable(CoachService.class.getClassLoader());
        this.brand = in.readParcelable(Brand.class.getClassLoader());
        this.gymStatus = in.readParcelable(GymStatus.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.coachService, flags);
        dest.writeParcelable(this.brand, flags);
        dest.writeParcelable(this.gymStatus, flags);
    }

    public static final class Builder {
        private CoachService coachService;
        private Brand brand;
        private GymStatus gymStatus;

        public Builder() {
        }

        public Builder coachService(CoachService val) {
            coachService = val;
            return this;
        }

        public Builder brand(Brand val) {
            brand = val;
            return this;
        }

        public Builder gymStatus(GymStatus val) {
            gymStatus = val;
            return this;
        }

        public QcContext build() {
            return new QcContext(this);
        }
    }
}
