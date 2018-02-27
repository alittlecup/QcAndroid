package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import java.util.List;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃
 * //   ┃　　　┃
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/28.
 */

public class ScoreRule implements Parcelable {

    public static final Creator<ScoreRule> CREATOR = new Creator<ScoreRule>() {
        @Override public ScoreRule createFromParcel(Parcel source) {
            return new ScoreRule(source);
        }

        @Override public ScoreRule[] newArray(int size) {
            return new ScoreRule[size];
        }
    };
    public String id;
    public String updated_at;
    public Staff updated_by;
    public Boolean teamarrange_enable;
    public String teamarrange;
    public Boolean priarrange_enable;
    public String priarrange;
    public Boolean checkin_enable;
    public String checkin;
    public Boolean buycard_enable;
    public List<ScoreRuleCard> buycard;
    public Boolean chargecard_enable;
    public List<ScoreRuleCard> chargecard;

    public ScoreRule() {
    }

    protected ScoreRule(Parcel in) {
        this.id = in.readString();
        this.updated_at = in.readString();
        this.updated_by = in.readParcelable(Staff.class.getClassLoader());
        this.teamarrange_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.teamarrange = in.readString();
        this.priarrange_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.priarrange = in.readString();
        this.checkin_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.checkin = in.readString();
        this.buycard_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.buycard = in.createTypedArrayList(ScoreRuleCard.CREATOR);
        this.chargecard_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.chargecard = in.createTypedArrayList(ScoreRuleCard.CREATOR);
    }

    public boolean allDisable() {
        return !(teamarrange_enable || priarrange_enable || checkin_enable || buycard_enable || chargecard_enable);
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.updated_by, flags);
        dest.writeValue(this.teamarrange_enable);
        dest.writeString(this.teamarrange);
        dest.writeValue(this.priarrange_enable);
        dest.writeString(this.priarrange);
        dest.writeValue(this.checkin_enable);
        dest.writeString(this.checkin);
        dest.writeValue(this.buycard_enable);
        dest.writeTypedList(this.buycard);
        dest.writeValue(this.chargecard_enable);
        dest.writeTypedList(this.chargecard);
    }
}
