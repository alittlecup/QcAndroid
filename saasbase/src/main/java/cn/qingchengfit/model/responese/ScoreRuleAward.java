package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;

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

public class ScoreRuleAward implements Parcelable {
    public static final Creator<ScoreRuleAward> CREATOR = new Creator<ScoreRuleAward>() {
        @Override public ScoreRuleAward createFromParcel(Parcel source) {
            return new ScoreRuleAward(source);
        }

        @Override public ScoreRuleAward[] newArray(int size) {
            return new ScoreRuleAward[size];
        }
    };
    public String id;
    public boolean is_active;
    public String start;
    public String end;
    public Boolean teamarrange_enable;
    public String teamarrange;
    public Boolean priarrange_enable;
    public String priarrange;
    public Boolean checkin_enable;
    public String checkin;
    public Boolean buycard_enable;
    public String buycard;
    public Boolean chargecard_enable;
    public String chargecard;

    public ScoreRuleAward() {
    }

    protected ScoreRuleAward(Parcel in) {
        this.id = in.readString();
        this.is_active = in.readByte() != 0;
        this.start = in.readString();
        this.end = in.readString();
        this.teamarrange_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.teamarrange = in.readString();
        this.priarrange_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.priarrange = in.readString();
        this.checkin_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.checkin = in.readString();
        this.buycard_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.buycard = in.readString();
        this.chargecard_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.chargecard = in.readString();
    }

    public StudentScoreAwardRuleBean toAwardRuleBean() {
        StudentScoreAwardRuleBean awardRuleBean = new StudentScoreAwardRuleBean();
        awardRuleBean.dateStart = this.start;
        awardRuleBean.dateEnd = this.end;
        awardRuleBean.groupTimes_enable = this.teamarrange_enable;
        awardRuleBean.groupTimes = this.teamarrange;
        awardRuleBean.privateTimes_enable = this.priarrange_enable;
        awardRuleBean.privateTimes = this.priarrange;
        awardRuleBean.signinTimes_enable = this.checkin_enable;
        awardRuleBean.signinTimes = this.checkin;
        awardRuleBean.buyTimes_enable = this.buycard_enable;
        awardRuleBean.buyTimes = this.buycard;
        awardRuleBean.chargeTimes_enable = this.chargecard_enable;
        awardRuleBean.chargeTimes = this.chargecard;
        awardRuleBean.id = this.id;
        awardRuleBean.is_active = this.is_active;
        return awardRuleBean;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.is_active ? (byte) 1 : (byte) 0);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeValue(this.teamarrange_enable);
        dest.writeString(this.teamarrange);
        dest.writeValue(this.priarrange_enable);
        dest.writeString(this.priarrange);
        dest.writeValue(this.checkin_enable);
        dest.writeString(this.checkin);
        dest.writeValue(this.buycard_enable);
        dest.writeString(this.buycard);
        dest.writeValue(this.chargecard_enable);
        dest.writeString(this.chargecard);
    }
}
