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
 * //Created by yangming on 16/12/28.
 */

public class ScoreRuleCard implements Parcelable {

    public static final Parcelable.Creator<ScoreRuleCard> CREATOR = new Parcelable.Creator<ScoreRuleCard>() {
        @Override public ScoreRuleCard createFromParcel(Parcel source) {
            return new ScoreRuleCard(source);
        }

        @Override public ScoreRuleCard[] newArray(int size) {
            return new ScoreRuleCard[size];
        }
    };
    public String start;
    public String end;
    public String money;

    public ScoreRuleCard() {
    }

    protected ScoreRuleCard(Parcel in) {
        this.start = in.readString();
        this.end = in.readString();
        this.money = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.money);
    }
}
