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
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/26.
 */

public class StudentScoreRuleBean implements Parcelable {

    public static final Parcelable.Creator<StudentScoreRuleBean> CREATOR = new Parcelable.Creator<StudentScoreRuleBean>() {
        @Override public StudentScoreRuleBean createFromParcel(Parcel source) {
            return new StudentScoreRuleBean(source);
        }

        @Override public StudentScoreRuleBean[] newArray(int size) {
            return new StudentScoreRuleBean[size];
        }
    };
    public String amountStart;
    public String amountEnd;
    public String perScore;

    public StudentScoreRuleBean() {
    }

    protected StudentScoreRuleBean(Parcel in) {
        this.amountStart = in.readString();
        this.amountEnd = in.readString();
        this.perScore = in.readString();
    }

    public ScoreRuleCard toScoreRuleCard() {
        ScoreRuleCard scoreRuleCard = new ScoreRuleCard();
        scoreRuleCard.start = this.amountStart;
        scoreRuleCard.end = this.amountEnd;
        scoreRuleCard.money = this.perScore;
        return scoreRuleCard;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amountStart);
        dest.writeString(this.amountEnd);
        dest.writeString(this.perScore);
    }
}
